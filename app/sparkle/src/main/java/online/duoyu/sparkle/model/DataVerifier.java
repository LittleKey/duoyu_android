package online.duoyu.sparkle.model;

import android.text.TextUtils;

import com.squareup.wire.Wire;

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
}
