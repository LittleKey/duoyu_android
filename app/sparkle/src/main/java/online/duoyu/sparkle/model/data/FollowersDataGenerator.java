package online.duoyu.sparkle.model.data;

import android.support.annotation.NonNull;

import com.squareup.wire.Wire;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.littlekey.base.ReadOnlyList;
import me.littlekey.network.ApiRequest;
import me.littlekey.network.NameValuePair;
import okio.ByteString;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.model.ModelFactory;
import online.duoyu.sparkle.model.business.FollowerRequest;
import online.duoyu.sparkle.model.business.FollowerResponse;
import online.duoyu.sparkle.model.proto.Cursor;
import online.duoyu.sparkle.model.proto.User;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.utils.CollectionUtils;
import online.duoyu.sparkle.utils.Const;

/**
 * Created by littlekey on 1/7/17.
 */

public class FollowersDataGenerator extends SparkleDataGenerator<FollowerResponse> {

  public FollowersDataGenerator(ApiType apiType, NameValuePair... pairs) {
    super(apiType, pairs);
  }

  @Override
  protected ApiRequest<FollowerResponse> onCreateRequest(ApiType apiType, Map<String, String> pairs) {
    return SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(ApiType.FOLLOWER, pairs, FollowerResponse.class, mListener, mErrorListener);
  }

  @Override
  public ApiRequest<FollowerResponse> getNextRequestFromResponse(FollowerResponse response) {
    Cursor cursor = new Cursor.Builder()
        .timestamp(response.cursor.timestamp)
        .limit(20)
        .build();
    FollowerRequest request = new FollowerRequest.Builder()
        .cursor(cursor)
        .user_id(mBasePairs.get(Const.KEY_USER_ID))
        .build();
    return SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(ApiType.FOLLOWER, ByteString.of(FollowerRequest.ADAPTER.encode(request)),
            FollowerResponse.class, mListener, mErrorListener);
  }

  @Override
  public boolean getHasMoreFromResponse(FollowerResponse response) {
    return response != null && response.cursor != null && Wire.get(response.cursor.has_more, false);
  }

  @Override
  public List<Model> getItemsFromResponse(@NonNull FollowerResponse response, ReadOnlyList<Model> roProcessedItems) {
    List<Model> models = new ArrayList<>();
    for (User user: response.users) {
      CollectionUtils.add(models,
          ModelFactory.createModelFromUser(user, Model.Template.ITEM_FOLLOWER));
    }
    return models;
  }
}
