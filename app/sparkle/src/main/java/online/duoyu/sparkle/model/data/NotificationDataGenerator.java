package online.duoyu.sparkle.model.data;

import android.support.annotation.NonNull;

import com.squareup.wire.Wire;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.littlekey.base.ReadOnlyList;
import me.littlekey.base.utils.CollectionUtils;
import me.littlekey.network.ApiRequest;
import okio.ByteString;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.model.ModelFactory;
import online.duoyu.sparkle.model.business.CurrentUserNotificationRequest;
import online.duoyu.sparkle.model.business.CurrentUserNotificationResponse;
import online.duoyu.sparkle.model.proto.Cursor;
import online.duoyu.sparkle.model.proto.Notification;
import online.duoyu.sparkle.network.ApiType;

/**
 * Created by littlekey on 1/7/17.
 */

public class NotificationDataGenerator extends SparkleDataGenerator<CurrentUserNotificationResponse> {

  private List<Notification.Event> mEvents;

  public NotificationDataGenerator(ApiType apiType) {
    super(apiType);
    mEvents = new ArrayList<>();
    switch (apiType) {
      case ATTENTION_NOTIFICATION:
        mEvents.add(Notification.Event.COMMENT_MY_ARTICLE);
        mEvents.add(Notification.Event.CORRECT_ATTENTION_DIARY);
        break;
      case LIKED_NOTIFICATION:
        mEvents.add(Notification.Event.LIKE_MY_ARTICLE);
        break;
    }
  }

  @Override
  protected ApiRequest<CurrentUserNotificationResponse> onCreateRequest(ApiType apiType, Map<String, String> pairs) {
    CurrentUserNotificationRequest request = new CurrentUserNotificationRequest.Builder()
        .events(mEvents)
        .build();
    return SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(mApiType,
            ByteString.of(CurrentUserNotificationRequest.ADAPTER.encode(request)),
            CurrentUserNotificationResponse.class, mListener, mErrorListener);
  }

  @Override
  public ApiRequest<CurrentUserNotificationResponse> getNextRequestFromResponse(CurrentUserNotificationResponse response) {
    Cursor cursor = new Cursor.Builder()
        .timestamp(response.notifications.get(response.notifications.size() - 1).date)
        .limit(20)
        .build();
    CurrentUserNotificationRequest request = new CurrentUserNotificationRequest.Builder()
        .events(mEvents)
        .cursor(cursor)
        .build();
    return SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(mApiType,
            ByteString.of(CurrentUserNotificationRequest.ADAPTER.encode(request)),
            CurrentUserNotificationResponse.class, mListener, mErrorListener);
  }

  @Override
  public boolean getHasMoreFromResponse(CurrentUserNotificationResponse response) {
    return response != null && response.cursor != null && Wire.get(response.cursor.has_more, false);
  }

  @Override
  public List<Model> getItemsFromResponse(@NonNull CurrentUserNotificationResponse response, ReadOnlyList<Model> roProcessedItems) {
    List<Model> models = new ArrayList<>();
    for (Notification notification: response.notifications) {
      CollectionUtils.add(models,
          ModelFactory.createModelFromNotification(notification, Model.Template.ITEM_NOTIFICATION));
    }
    return models;
  }
}
