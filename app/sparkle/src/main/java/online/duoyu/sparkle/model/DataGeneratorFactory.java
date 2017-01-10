package online.duoyu.sparkle.model;

import android.os.Bundle;

import java.util.List;

import me.littlekey.network.NameValuePair;
import online.duoyu.sparkle.model.data.FollowersDataGenerator;
import online.duoyu.sparkle.model.data.GetUserCorrectsDataGenerator;
import online.duoyu.sparkle.model.data.GetUserDiariesDataGenerator;
import online.duoyu.sparkle.model.data.GetUserTimelineDataGenerator;
import online.duoyu.sparkle.model.data.NotificationDataGenerator;
import online.duoyu.sparkle.model.data.FollowUserDiaryDataGenerator;
import online.duoyu.sparkle.model.data.GetCommentsByDiaryDataGenerator;
import online.duoyu.sparkle.model.data.GetCorrectsByDiaryDataGenerator;
import online.duoyu.sparkle.model.data.GetCorrectsSentenceByDiaryDataGenerator;
import online.duoyu.sparkle.model.data.RecentDiaryDataGenerator;
import online.duoyu.sparkle.model.data.SparkleDataGenerator;
import online.duoyu.sparkle.network.ApiType;

/**
 * Created by littlekey on 12/19/16.
 */

public class DataGeneratorFactory {
  private DataGeneratorFactory() {}

  public static SparkleDataGenerator<?>
  createDataGenerator(ApiType apiType, List<String> paths, Bundle bundle, NameValuePair... pairs) {
    switch (apiType) {
      case RECENT_DIARY:
        return new RecentDiaryDataGenerator(apiType);
      case FOLLOW_USER_DIARY:
        return new FollowUserDiaryDataGenerator(apiType);
      case GET_CORRECTS_SENTENCE_BY_DIARY:
        return new GetCorrectsSentenceByDiaryDataGenerator(apiType, bundle);
      case GET_CORRECTS_BY_DIARY:
        return new GetCorrectsByDiaryDataGenerator(apiType, bundle);
      case GET_COMMENTS_BY_DIARY:
        return new GetCommentsByDiaryDataGenerator(apiType, bundle);
      case LIKED_NOTIFICATION:
      case ATTENTION_NOTIFICATION:
        return new NotificationDataGenerator(apiType);
      case FOLLOWER:
        return new FollowersDataGenerator(apiType, pairs);
      case GET_USER_TIMELINE:
        return new GetUserTimelineDataGenerator(apiType, pairs);
      case GET_USER_PUBLISHED_CORRECTS:
        return new GetUserCorrectsDataGenerator(apiType, pairs);
      case GET_USER_PUBLISHED_DIARIES:
        return new GetUserDiariesDataGenerator(apiType, pairs);
      default:
        throw new IllegalStateException("Unknown api type:" + apiType.name());
    }
  }
}
