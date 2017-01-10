package online.duoyu.sparkle.model.data;

import android.support.annotation.NonNull;

import com.squareup.wire.Wire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.littlekey.base.ReadOnlyList;
import me.littlekey.base.utils.CollectionUtils;
import me.littlekey.network.ApiRequest;
import me.littlekey.network.NameValuePair;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.model.ModelFactory;
import online.duoyu.sparkle.model.business.TimelineResponse;
import online.duoyu.sparkle.model.proto.Happening;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.utils.Const;

/**
 * Created by littlekey on 1/10/17.
 */

public class GetUserTimelineDataGenerator extends SparkleDataGenerator<TimelineResponse> {

  public GetUserTimelineDataGenerator(ApiType apiType, NameValuePair... pairs) {
    super(apiType, pairs);
  }

  @Override
  protected ApiRequest<TimelineResponse> onCreateRequest(ApiType apiType, Map<String, String> pairs) {
    return SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(apiType, pairs, TimelineResponse.class, mListener, mErrorListener);
  }

  @Override
  public ApiRequest<TimelineResponse> getNextRequestFromResponse(TimelineResponse response) {
    Map<String, String> pairs = new HashMap<>(mBasePairs);
    pairs.put(Const.KEY_TIME_STAMP, String.valueOf(response.happenings.get(response.happenings.size() - 1).date));
    return SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(mApiType, pairs, TimelineResponse.class, mListener, mErrorListener);
  }

  @Override
  public boolean getHasMoreFromResponse(TimelineResponse response) {
    return response != null && response.cursor != null && Wire.get(response.cursor.has_more, false);
  }

  @Override
  public List<Model> getItemsFromResponse(@NonNull TimelineResponse response, ReadOnlyList<Model> roProcessedItems) {
    List<Model> models = new ArrayList<>();
    for (Happening happening: response.happenings) {
      CollectionUtils.add(models,
          ModelFactory.createModelFromHappening(happening, Model.Template.ITEM_HAPPENING));
    }
    return models;
  }
}
