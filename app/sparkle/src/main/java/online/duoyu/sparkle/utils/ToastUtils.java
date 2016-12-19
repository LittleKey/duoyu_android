package online.duoyu.sparkle.utils;

import android.support.annotation.StringRes;
import android.widget.Toast;

import online.duoyu.sparkle.SparkleApplication;

/**
 * Created by littlekey on 12/19/16.
 */

public class ToastUtils {

  private static Toast mInstance;

  private ToastUtils() {}

  public static void toast(@StringRes int resId) {
    toast(SparkleApplication.getInstance().getResources().getText(resId));
  }

  public static void toast(CharSequence content) {

    if (mInstance == null) {
      synchronized (ToastUtils.class) {
        mInstance = Toast.makeText(SparkleApplication.getInstance(), content, Toast.LENGTH_SHORT);
      }
    }
    mInstance.setText(content);
    mInstance.show();
  }
}
