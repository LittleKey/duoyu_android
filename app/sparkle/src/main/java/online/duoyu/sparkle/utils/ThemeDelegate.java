package online.duoyu.sparkle.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.StyleRes;
import android.view.WindowManager;

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
    styleRes = context.getResources().getIdentifier(
        (dialog ? "DialogTheme." : "AppTheme.") +
            (this.themeColor == Colorful.ThemeColor.PRIMARY_BLUE ? "PrimaryBlue" : "PrimaryPink"),
        "style", context.getPackageName());
    Timber.d("ThemeDelegate fetched theme in " + (System.currentTimeMillis() - curTime) + " milliseconds");
  }

  public @StyleRes int getStyle() {
    return styleRes;
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
}