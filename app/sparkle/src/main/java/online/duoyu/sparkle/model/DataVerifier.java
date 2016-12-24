package online.duoyu.sparkle.model;

import android.text.TextUtils;

import com.squareup.wire.Wire;

import online.duoyu.sparkle.model.proto.Diary;
import online.duoyu.sparkle.model.proto.User;
import online.duoyu.sparkle.utils.Const;

/**
 * Created by littlekey on 12/19/16.
 */

public class DataVerifier {
  private DataVerifier() {
  }

  public static User verify(User user) {
    if (user == null) {
      return null;
    }
    if (TextUtils.isEmpty(user.nickname)) {
      return null;
    }
    if (TextUtils.isEmpty(user.user_id)) {
      return null;
    }
    User.Builder builder = user.newBuilder();
    builder.nickname(Wire.get(user.nickname, Const.EMPTY_STRING));
    return builder.build();
  }

  public static Diary verify(Diary diary) {
    if (diary == null) {
      return null;
    }
    if (TextUtils.isEmpty(diary.diary_id)) {
      return null;
    }
    if (TextUtils.isEmpty(diary.title)) {
      return null;
    }
    if (TextUtils.isEmpty(diary.content)) {
      return null;
    }
    if (verify(diary.author) == null) {
      return null;
    }
    Diary.Builder builder = diary.newBuilder();
    builder.likes(Wire.get(diary.likes, 0));
    builder.followers(Wire.get(diary.followers, 0));
    return builder.build();
  }
}
