package online.duoyu.sparkle.model.data;

import android.support.annotation.NonNull;

import com.squareup.wire.Wire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import me.littlekey.base.ReadOnlyList;
import me.littlekey.base.utils.CollectionUtils;
import me.littlekey.network.ApiRequest;
import me.littlekey.network.NameValuePair;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.event.OnDiariesAmountUpdateEvent;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.model.ModelFactory;
import online.duoyu.sparkle.model.business.DiariesResponse;
import online.duoyu.sparkle.model.proto.Diary;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.utils.Const;

/**
 * Created by littlekey on 1/10/17.
 */

public class DiariesDataGenerator extends SparkleDataGenerator<DiariesResponse> {

  public DiariesDataGenerator(ApiType apiType, NameValuePair... pairs) {
    super(apiType, pairs);
  }

  @Override
  protected ApiRequest<DiariesResponse> onCreateRequest(ApiType apiType, Map<String, String> pairs) {
    return SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(apiType, pairs, DiariesResponse.class, mListener, mErrorListener);
  }

  @Override
  public ApiRequest<DiariesResponse> getNextRequestFromResponse(DiariesResponse response) {
    Map<String, String> pairs = new HashMap<>(mBasePairs);
    pairs.put(Const.KEY_TIME_STAMP, String.valueOf(response.diaries.get(response.diaries.size() - 1).date));
    return SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(mApiType, pairs, DiariesResponse.class, mListener, mErrorListener);
  }

  @Override
  public boolean getHasMoreFromResponse(DiariesResponse response) {
    return response != null && response.cursor != null && Wire.get(response.cursor.has_more, false);
  }

  @Override
  public List<Model> getItemsFromResponse(@NonNull DiariesResponse response, ReadOnlyList<Model> roProcessedItems) {
    List<Model> models = new ArrayList<>();
    EventBus.getDefault().post(
        new OnDiariesAmountUpdateEvent(mApiType, Wire.get(response.cursor.amount, 0)));
    for (Diary diary: response.diaries) {
      CollectionUtils.add(models,
          ModelFactory.createModelFromDiary(diary, Model.Template.ITEM_DIARY_WITH_MONTH));
    }
    return models;
  }
}
