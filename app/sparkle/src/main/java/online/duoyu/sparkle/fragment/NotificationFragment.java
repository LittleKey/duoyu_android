package online.duoyu.sparkle.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import online.duoyu.sparkle.R;

/**
 * Created by littlekey on 12/20/16.
 */

public class NotificationFragment extends LazyLoadFragment {

  public static NotificationFragment newInstance() {
    return new NotificationFragment();
  }

  @Override
  protected View lazyLoad(LayoutInflater inflater, ViewGroup container) {
    return inflater.inflate(R.layout.fragment_notification, container, false);
  }
}