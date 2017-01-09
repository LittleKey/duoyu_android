package online.duoyu.sparkle.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import online.duoyu.sparkle.R;
import online.duoyu.sparkle.widget.StatefulButton;
import timber.log.Timber;

/**
 * Created by littlekey on 12/28/16.
 */

public class Colorful {
  private static ThemeDelegate delegate;
  private static ThemeColor primaryColor = ThemeColor.PRIMARY_BLUE;
  private static boolean isTranslucent = false;
  private static boolean isDark = false;
  private static boolean isDialog = false;
  private static String themeString;

  public static void init(Context context) {
    themeString = PreferenceManager.getDefaultSharedPreferences(context).getString(Const.LAST_THEME, null);
    if (themeString == null) {
      primaryColor = Defaults.primaryColor;
      isTranslucent = Defaults.trans;
      isDark = Defaults.darkTheme;
      isDialog = Defaults.dialogTheme;
      themeString = generateThemeString();
    }
    try {
      initValues();
    } catch (Exception e) {
      Timber.d("Format theme string shared preferences: " + e.toString());
    }
    delegate = new ThemeDelegate(context, primaryColor, isTranslucent, isDark, isDialog);
  }

  private static void initValues() {
    String [] colors = themeString.split(":");
    isDark = Boolean.parseBoolean(colors[0]);
    isTranslucent = Boolean.parseBoolean(colors[1]);
    isDialog = Boolean.parseBoolean(colors[2]);
    primaryColor = Colorful.ThemeColor.values()[Integer.parseInt(colors[3])];
  }

  private static String generateThemeString() {
    return isDark+ ":" + isTranslucent + ":" + isDialog + ":" + primaryColor.ordinal();
  }

  public static ThemeDelegate getThemeDelegate() {
    return delegate;
  }

  public static String getThemeString() {
    return themeString;
  }

  public static void changeTheme(View view) {
    setTheme(view);
    if (view instanceof ViewGroup) {
      int count = ((ViewGroup) view).getChildCount();
      for (int i = 0; i < count; ++i) {
        View child = ((ViewGroup) view).getChildAt(i);
        changeTheme(child);
      }
    }
  }

  public static void setTheme(View view) {
    if (view == null) {
      return;
    }
    setTheme(view.getId(), view);
  }

  public static void setTheme(@IdRes int id, View view) {
    ThemeDelegate themeDelegate = Colorful.getThemeDelegate();
    switch (id) {
      case R.id.theme_title:
      case R.id.theme_week:
      case R.id.theme_day:
      case R.id.theme_month:
      case R.id.theme_title_bar:
        if (view instanceof TextView) {
          ((TextView) view).setTextColor(themeDelegate.getThemeColor().getTextColor());
        }
        break;
      case R.id.theme_tab_bar:
      case R.id.theme_date_background:
        view.setBackgroundColor(Colorful.getThemeDelegate().getThemeColor().getPrimaryColor());
        break;
      case R.id.theme_btn_follow:
      case R.id.theme_btn_publish:
      case R.id.theme_btn_sure:
        if (view instanceof StatefulButton) {
          ((StatefulButton) view).updateState();
        }
        break;
    }
  }

  public enum ThemeColor {
    PRIMARY_BLUE(
        R.color.primary_blue,
        R.color.primary_blue,
        TitleTabTheme.PRIMARY_BLUE
    ),
    PRIMARY_PINK(
        R.color.primary_pink,
        R.color.primary_pink,
        TitleTabTheme.PRIMARY_PINK
    );
    public enum TitleTabTheme {
      PRIMARY_BLUE(
          R.drawable.bg_primary_blue_white_round_title_bar_left,
          R.drawable.bg_primary_blue_round_title_bar_left,
          R.drawable.bg_primary_blue_white_round_title_bar_mid,
          R.drawable.bg_primary_blue_white_round_title_bar_right,
          R.drawable.bg_primary_blue_round_title_bar_right
      ),
      PRIMARY_PINK(
          R.drawable.bg_primary_pink_white_round_title_bar_left,
          R.drawable.bg_primary_pink_round_title_bar_left,
          R.drawable.bg_primary_pink_white_round_title_bar_mid,
          R.drawable.bg_primary_pink_white_round_title_bar_right,
          R.drawable.bg_primary_pink_round_title_bar_right
      );

      @DrawableRes private int[] mTitleTabDrawablesRes;

      TitleTabTheme(@DrawableRes int leftWhiteDrawableRes, @DrawableRes int leftDrawableRes,
                    @DrawableRes int midWhiteDrawableRes,
                    @DrawableRes int rightWhiteDrawableRes, @DrawableRes int rightDrawableRes) {
        mTitleTabDrawablesRes = new int[] {
            leftWhiteDrawableRes, leftDrawableRes,
            midWhiteDrawableRes,
            rightWhiteDrawableRes, rightDrawableRes
        };
      }

      public Drawable getLeftWhiteDrawable() {
        return ResourcesUtils.getDrawable(mTitleTabDrawablesRes[0]);
      }

      public Drawable getLeftDrawable() {
        return ResourcesUtils.getDrawable(mTitleTabDrawablesRes[1]);
      }

      public Drawable getMidWhiteDrawable() {
        return ResourcesUtils.getDrawable(mTitleTabDrawablesRes[2]);
      }

      public Drawable getRightWhiteDrawable() {
        return ResourcesUtils.getDrawable(mTitleTabDrawablesRes[3]);
      }

      public Drawable getRightDrawable() {
        return ResourcesUtils.getDrawable(mTitleTabDrawablesRes[4]);
      }
    }

    @ColorRes private int mPrimaryColorRes;
    @ColorRes private int mTextColorRes;
    private TitleTabTheme mTitleTabTheme;

    ThemeColor(@ColorRes int primaryColorRes, @ColorRes int textColorRes, TitleTabTheme titleTab) {
      this.mPrimaryColorRes = primaryColorRes;
      this.mTextColorRes = textColorRes;
      this.mTitleTabTheme = titleTab;
    }

    public @ColorInt int getPrimaryColor() {
      return ResourcesUtils.getColor(mPrimaryColorRes);
    }

    public @ColorInt int getTextColor() {
      return ResourcesUtils.getColor(mTextColorRes);
    }

    public TitleTabTheme getTitleTabTheme() {
      return mTitleTabTheme;
    }

  }

  public static Config config(Context context) {
    return new Config(context);
  }

  public static Defaults defaults() {
    return new Defaults();
  }

  public static class Defaults {

    private static ThemeColor primaryColor = ThemeColor.PRIMARY_BLUE;
    private static boolean trans = false;
    private static boolean darkTheme = false;
    private static boolean dialogTheme = false;

    public Defaults primaryColor(ThemeColor primary) {
      primaryColor = primary;
      return this;
    }

    public Defaults translucent(boolean translucent) {
      trans = translucent;
      return this;
    }

    public Defaults dark(boolean dark) {
      darkTheme = dark;
      return this;
    }

    public Defaults dialog(boolean dialog) {
      dialogTheme = dialog;
      return this;
    }
  }

  public static class Config {
    private Context context;

    private Config(Context context) {
      this.context = context;
    }

    public Config primaryColor(ThemeColor primary) {
      primaryColor = primary;
      return this;
    }

    public Config translucent(boolean translucent) {
      isTranslucent = translucent;
      return this;
    }

    public Config dark(boolean dark) {
      isDark = dark;
      return this;
    }

    public Config dialog(boolean dialog) {
      isDialog = dialog;
      return this;
    }

    public void apply() {
      themeString = generateThemeString();
      PreferenceManager.getDefaultSharedPreferences(context).edit().putString(Const.LAST_THEME, themeString).apply();
      delegate = new ThemeDelegate(context, primaryColor, isTranslucent, isDark, isDialog);
      ThemeEventBus.postChangeThemeEvent(delegate);
    }

    public void apply(View view) {
      apply();
      changeTheme(view);
    }
  }

}