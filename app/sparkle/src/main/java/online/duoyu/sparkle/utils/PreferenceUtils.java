package online.duoyu.sparkle.utils;

import android.content.Context;

import java.util.Set;

import online.duoyu.sparkle.SparkleApplication;

/**
 * Created by littlekey on 12/19/16.
 */

public class PreferenceUtils {
  private PreferenceUtils() {
  }

  public static String getString(final String name, final String key, final String defaultValue) {
    return SparkleApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE).getString(key, defaultValue);
  }

  public static void setString(final String name, final String key, final String value) {
    SparkleApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE).edit().putString(key, value).apply();
  }

  public static int getInt(final String name, final String key, final int defaultValue) {
    return SparkleApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE).getInt(key, defaultValue);
  }

  public static void setInt(final String name, final String key, final int value) {
    SparkleApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE).edit().putInt(key, value).apply();
  }

  public static boolean getBoolean(final String name, final String key, final boolean defaultValue) {
    return SparkleApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE).getBoolean(key, defaultValue);
  }

  public static void setBoolean(final String name, final String key, final boolean value) {
    SparkleApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE).edit().putBoolean(key, value).apply();
  }

  public static void removeString(final String name, final String key) {
    SparkleApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE).edit().remove(key).apply();
  }

  public static void setStringSet(final String name, final String key, final Set<String> value) {
    SparkleApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE).edit().putStringSet(key, value).apply();
  }

  public static Set<String> getStringSet(final String name, final String key, final Set<String> defaultValue) {
    return SparkleApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE).getStringSet(key, defaultValue);
  }
}
