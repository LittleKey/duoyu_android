package online.duoyu.sparkle.model.data;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import me.littlekey.base.ReadOnlyList;
import me.littlekey.network.ApiRequest;
import okio.ByteString;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.event.UpdateMonthEvent;
import online.duoyu.sparkle.model.ModelFactory;
import online.duoyu.sparkle.model.business.DiariesResponse;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.model.business.FollowingUserPublishedDiariesRequest;
import online.duoyu.sparkle.model.proto.Action;
import online.duoyu.sparkle.model.proto.Cursor;
import online.duoyu.sparkle.model.proto.Diary;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.utils.CollectionUtils;
import online.duoyu.sparkle.utils.Const;

/**
 * Created by littlekey on 12/21/16.
 */

public class FollowUserDiaryDataGenerator extends SparkleDataGenerator<DiariesResponse> {

  public FollowUserDiaryDataGenerator(ApiType apiType) {
    super(apiType);
  }

  @Override
  protected ApiRequest<DiariesResponse> onCreateRequest(ApiType apiType, Map<String, String> pairs) {
    return SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(apiType, pairs, DiariesResponse.class, mListener, mErrorListener);
  }

  @Override
  public ApiRequest<DiariesResponse> getNextRequestFromResponse(DiariesResponse response) {
    Cursor cursor = new Cursor.Builder()
        .timestamp(response.diaries.get(response.diaries.size() - 1).date)
        .limit(20)
        .build();
    FollowingUserPublishedDiariesRequest request = new FollowingUserPublishedDiariesRequest.Builder()
        .cursor(cursor)
        .build();
    return SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(mApiType,
            ByteString.of(FollowingUserPublishedDiariesRequest.ADAPTER.encode(request)),
            DiariesResponse.class, mListener, mErrorListener);
  }

  @Override
  public boolean getHasMoreFromResponse(DiariesResponse response) {
    return response != null && response.cursor != null
        && response.cursor.has_more != null && response.cursor.has_more;
  }

  @Override
  public List<Model> getItemsFromResponse(@NonNull DiariesResponse response, ReadOnlyList<Model> roProcessedItems) {
    List<Model> models = new ArrayList<>();
    Map<Integer, Action> actions = new HashMap<>();
    actions.put(Const.ACTION_UPDATE_MONTH, new Action.Builder()
        .type(Action.Type.UPDATE_MONTH)
        .clazz(UpdateMonthEvent.class.getName())
        .build());
    for (Diary diary: response.diaries) {
      Model model = ModelFactory.createModelFromDiary(diary, Model.Template.ITEM_DIARY);
      if (model == null) {
        continue;
      }
      if (models.size() > 0 || roProcessedItems.size() > 0) {
        Model last_model;
        if (models.size() > 0) {
          last_model = models.get(models.size() - 1);
        } else {
          last_model = roProcessedItems.getItem(roProcessedItems.size() - 1);
        }
        DateTime last_dt = new DateTime(last_model.date);
        DateTime cur_dt = new DateTime(model.date);
        if (last_dt.getMonthOfYear() != cur_dt.getMonthOfYear()) {
          Model month_model = new Model.Builder()
              .type(Model.Type.DATE)
              .template(Model.Template.ITEM_MONTH)
              .date(cur_dt.getMillis())
              .month(cur_dt.monthOfYear().getAsShortText())
              .actions(actions)
              .build();
          CollectionUtils.add(models, month_model);
        }
      }
      Map<Integer, Action> new_actions = new HashMap<>(model.actions);
      new_actions.putAll(actions);
      CollectionUtils.add(models,
          model.newBuilder()
              .actions(new_actions)
              .build());
    }
    if (roProcessedItems.size() == 0 && models.size() > 0) {
      EventBus.getDefault().post(new UpdateMonthEvent(models.get(0)));
    }
    return models;
  }
}
