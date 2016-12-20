package online.duoyu.sparkle.utils;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.view.View;

import online.duoyu.sparkle.SparkleApplication;

/**
 * Created by littlekey on 12/20/16.
 */

public class ResourcesUtils {

  @SuppressWarnings("deprecation")
  public static @ColorInt
  int getColor(@ColorRes int colorRes) {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
      return SparkleApplication.getInstance().getColor(colorRes);
    } else {
      return SparkleApplication.getInstance().getResources().getColor(colorRes);
    }
  }

  @SuppressWarnings("deprecation")
  public static Drawable getDrawable(@DrawableRes int drawRes) {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
      return SparkleApplication.getInstance().getDrawable(drawRes);
    } else {
      return SparkleApplication.getInstance().getResources().getDrawable(drawRes);
    }
  }

  @SuppressWarnings("deprecation")
  public static void setBackground(View view, Drawable drawable) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      view.setBackground(drawable);
    } else {
      view.setBackgroundDrawable(drawable);
    }
  }

  public static void setBackground(View view, @DrawableRes int drawRes) {
    setBackground(view, getDrawable(drawRes));
  }
}
