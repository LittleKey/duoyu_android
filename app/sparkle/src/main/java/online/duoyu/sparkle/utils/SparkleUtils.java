package online.duoyu.sparkle.utils;

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

  public static String formatString(@StringRes int format, Object... args) {
    return formatString(SparkleApplication.getInstance().getString(format), args);
  }

  public static String formatString(String format, Object... args) {
    return String.format(Locale.US, format, args);
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
