package online.duoyu.sparkle.utils;

import android.content.res.Resources;

import de.greenrobot.event.EventBus;

/**
 * Created by littlekey on 12/28/16.
 */

public class ThemeEventBus {

  private static ThemeEventBus sSingleton;

  private EventBus mEventBus;

  private ThemeEventBus() {
    mEventBus = EventBus.builder().build();
  }

  public static EventBus getDefault() {
    if (sSingleton == null) {
      sSingleton = new ThemeEventBus();
    }
    return sSingleton.mEventBus;
  }

  public static void postChangeThemeEvent(ThemeDelegate delegate) {
    getDefault().post(new OnThemeChangeEvent(delegate));
  }

  public static class OnThemeChangeEvent {

    public ThemeDelegate delegate;

    OnThemeChangeEvent(ThemeDelegate delegate) {
      this.delegate = delegate;
    }
  }
}
