package online.duoyu.sparkle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.utils.Colorful;
import online.duoyu.sparkle.utils.ThemeEventBus;

/**
 * Created by littlekey on 12/19/16.
 */

public class BaseFragment extends RxFragment {

  private String mCurrentThemeString;

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ThemeEventBus.getDefault().registerSticky(this);
  }

  public void onEventMainThread(ThemeEventBus.OnThemeChangeEvent event) {
    if (!TextUtils.equals(mCurrentThemeString, Colorful.getThemeString())) {
      mCurrentThemeString = Colorful.getThemeString();
      Colorful.changeTheme(getView());
    }
  }

  @Override
  public void onDestroyView() {
    ThemeEventBus.getDefault().unregister(this);
    SparkleApplication.getInstance().getRequestManager().cancel(this);
    super.onDestroyView();
  }
}
