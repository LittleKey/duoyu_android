package online.duoyu.sparkle.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import okio.ByteString;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.model.proto.PrehashedPassword;

/**
 * Created by littlekey on 12/24/16.
 */

public class SparkleUtils {

  private SparkleUtils() {
  }

  public static String[] split_content_v1(@NonNull String content) {
    return content.split(String.format("(?<=(%1$s))",
        "\\u002e|\\u000a|\\u0021|\\u3002|\\uff01|\\u003f|\\uff1f"));
  }

  public static String formatString(@StringRes int format, Object... args) {
    return formatString(SparkleApplication.getInstance().getString(format), args);
  }

  public static String formatString(String format, Object... args) {
    return String.format(Locale.US, format, args);
  }

  public static String formatInteger(int integer) {
    return formatString("%d", integer > 999 ? 999 : integer) + (integer > 999 ? "+" : "");
  }

  public static Drawable setDrawableBounds(Drawable drawable) {
    if (drawable == null) {
      return null;
    }
    int h = drawable.getIntrinsicHeight();
    int w = drawable.getIntrinsicWidth();
    drawable.setBounds(0, 0, w, h);
    return drawable;
  }

   public static PrehashedPassword getPrehashedPassword(String password) {
     ByteString hashedPassword = null;
     int cycle = RandomUtils.getRandom().nextInt(90) + 10;
     if (password != null) {
       try {
         MessageDigest md = MessageDigest.getInstance("SHA-256");
         byte[] bt = password.getBytes();
         for (int i = 0; i < cycle; i++) {
           md.update(bt);
           bt = md.digest();
         }
         hashedPassword = ByteString.of(bt);
       } catch (NoSuchAlgorithmException e) {
         e.printStackTrace();
       }
     }
     return new PrehashedPassword.Builder()
         .prehashed_password(hashedPassword)
         .prehash_cycle(cycle)
         .build();
   }
}
