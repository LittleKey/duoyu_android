package online.duoyu.sparkle.model.data;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

import me.littlekey.network.ApiRequest;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.model.business.DiariesResponse;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.network.ApiType;

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
    return null;
  }

  @Override
  public boolean getHasMoreFromResponse(DiariesResponse response) {
    return false;
  }

  @Override
  public List<Model> getItemsFromResponse(@NonNull DiariesResponse response) {
    return null;
  }
}
