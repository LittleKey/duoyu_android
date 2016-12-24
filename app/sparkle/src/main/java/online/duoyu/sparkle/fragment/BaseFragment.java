package online.duoyu.sparkle.fragment;

import com.trello.rxlifecycle.components.support.RxFragment;

import online.duoyu.sparkle.SparkleApplication;

/**
 * Created by littlekey on 12/19/16.
 */

public class BaseFragment extends RxFragment {

  @Override
  public void onDestroyView() {
    SparkleApplication.getInstance().getRequestManager().cancel(this);
    super.onDestroyView();
  }
}
