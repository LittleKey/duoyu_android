package online.duoyu.sparkle.model.data;

import android.support.annotation.NonNull;

import com.squareup.wire.Wire;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.littlekey.base.ReadOnlyList;
import me.littlekey.network.ApiRequest;
import okio.ByteString;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.model.ModelFactory;
import online.duoyu.sparkle.model.business.DiariesResponse;
import online.duoyu.sparkle.model.business.RecentRequest;
import online.duoyu.sparkle.model.proto.Cursor;
import online.duoyu.sparkle.model.proto.Diary;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.utils.CollectionUtils;

/**
 * Created by littlekey on 12/25/16.
 */

public class RecentDiaryDataGenerator extends SparkleDataGenerator<DiariesResponse> {

  public RecentDiaryDataGenerator(ApiType apiType) {
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
    RecentRequest request = new RecentRequest.Builder()
        .cursor(cursor)
        .build();
    return SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(mApiType, ByteString.of(RecentRequest.ADAPTER.encode(request)),
            DiariesResponse.class, mListener, mErrorListener);
  }

  @Override
  public boolean getHasMoreFromResponse(DiariesResponse response) {
    return response != null && response.cursor != null && Wire.get(response.cursor.has_more, false);
  }

  @Override
  public List<Model> getItemsFromResponse(@NonNull DiariesResponse response, ReadOnlyList<Model> roProcessedItems) {
    List<Model> models = new ArrayList<>();
    for (Diary diary: response.diaries) {
      CollectionUtils.add(models,
          ModelFactory.createModelFromDiary(diary, Model.Template.ITEM_DIARY_WITH_MONTH));
    }
    return models;
  }
}
