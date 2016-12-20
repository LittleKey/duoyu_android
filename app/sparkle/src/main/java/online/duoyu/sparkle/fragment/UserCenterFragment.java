package online.duoyu.sparkle.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import online.duoyu.sparkle.R;

/**
 * Created by littlekey on 12/20/16.
 */

public class UserCenterFragment extends LazyLoadFragment {

  public static UserCenterFragment newInstance() {
    return new UserCenterFragment();
  }

  @Override
  protected View lazyLoad(LayoutInflater inflater, ViewGroup container) {
    return inflater.inflate(R.layout.fragment_user_center, container, false);
  }
}
