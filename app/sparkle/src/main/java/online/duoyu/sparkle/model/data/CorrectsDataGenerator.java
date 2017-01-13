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
import online.duoyu.sparkle.event.OnCorrectsAmountUpdateEvent;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.model.ModelFactory;
import online.duoyu.sparkle.model.business.CorrectsResponse;
import online.duoyu.sparkle.model.proto.Correct;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.utils.Const;

/**
 * Created by littlekey on 1/10/17.
 */

public class CorrectsDataGenerator extends SparkleDataGenerator<CorrectsResponse> {

  public CorrectsDataGenerator(ApiType apiType, NameValuePair... pairs) {
    super(apiType, pairs);
  }

  @Override
  protected ApiRequest<CorrectsResponse> onCreateRequest(ApiType apiType, Map<String, String> pairs) {
    return SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(apiType, pairs, CorrectsResponse.class, mListener, mErrorListener);
  }

  @Override
  public ApiRequest<CorrectsResponse> getNextRequestFromResponse(CorrectsResponse response) {
    Map<String, String> pairs = new HashMap<>(mBasePairs);
    pairs.put(Const.KEY_TIME_STAMP, String.valueOf(response.corrects.get(response.corrects.size() - 1).date));
    return SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(mApiType, pairs, CorrectsResponse.class, mListener, mErrorListener);
  }

  @Override
  public boolean getHasMoreFromResponse(CorrectsResponse response) {
    return response != null && response.cursor != null && Wire.get(response.cursor.has_more, false);
  }

  @Override
  public List<Model> getItemsFromResponse(@NonNull CorrectsResponse response, ReadOnlyList<Model> roProcessedItems) {
    List<Model> models = new ArrayList<>();
    EventBus.getDefault().post(
        new OnCorrectsAmountUpdateEvent(mApiType, Wire.get(response.cursor.amount, 0)));
    for (Correct correct: response.corrects) {
      CollectionUtils.add(models,
          ModelFactory.createModelFromCorrect(correct, Model.Template.ITEM_CORRECT_WITH_DIARY));
    }
    return models;
  }
}
