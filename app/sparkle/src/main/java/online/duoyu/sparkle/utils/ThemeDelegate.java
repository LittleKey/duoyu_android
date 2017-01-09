package online.duoyu.sparkle.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.StyleRes;
import android.support.annotation.StyleableRes;
import android.text.TextUtils;
import android.view.WindowManager;

import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.activity.BaseActivity;
import timber.log.Timber;

/**
 * Created by littlekey on 12/28/16.
 */

public class ThemeDelegate {
  private Colorful.ThemeColor themeColor;
  private boolean translucent;
  private boolean dark;
  private boolean dialog;
  private int styleRes;
  private int styleResForDialog;
  private int styleResForDatePickerDialog;
  private String primary;

  ThemeDelegate(Context context, Colorful.ThemeColor themeColor, boolean translucent, boolean dark, boolean dialog) {
    this.themeColor = themeColor;
    this.translucent = translucent;
    this.dark = dark;
    this.dialog = dialog;
    long curTime = System.currentTimeMillis();
    if (context instanceof BaseActivity) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && translucent) {
        ((BaseActivity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      }
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        ActivityManager.TaskDescription tDesc = new ActivityManager.TaskDescription(null, null,
            this.themeColor.getPrimaryColor());
        ((BaseActivity) context).setTaskDescription(tDesc);
      }
    }
    switch (themeColor) {
      case PRIMARY_BLUE:
        primary = "PrimaryBlue";
        break;
      case PRIMARY_PINK:
        primary = "PrimaryPink";
        break;
    }
    styleRes = ResourcesUtils.getStyleIdentifier((dialog ? "DialogTheme." : "AppTheme.") + primary);
    styleResForDialog = dialog ? styleRes : ResourcesUtils.getStyleIdentifier("DialogTheme." + primary);
    styleResForDatePickerDialog = ResourcesUtils.getStyleIdentifier("DatePickDialogTheme." + primary);
    Timber.d("ThemeDelegate fetched theme in " + (System.currentTimeMillis() - curTime) + " milliseconds");
  }

  public @StyleRes int getStyle() {
    return styleRes;
  }

  public @StyleRes int getDialogStyle() {
    return styleResForDialog;
  }

  public @StyleRes int getDatePickerDialogStyle() {
    return styleResForDatePickerDialog;
  }

  public Colorful.ThemeColor getThemeColor() {
    return themeColor;
  }

  public boolean isTranslucent() {
    return translucent;
  }

  public boolean isDark() {
    return dark;
  }

  public boolean isDialog() {
    return dialog;
  }

  public String getPrimary() {
    return primary;
  }

  public @StyleRes int themeStyle(@StyleRes int styleableRes) {
    String styleableName = ResourcesUtils.getResourceEntryName(styleableRes);
    if (!TextUtils.isEmpty(styleableName)) {
      return ResourcesUtils.getStyleIdentifier(styleableName + "." + primary);
    }
    return 0;
  }
}