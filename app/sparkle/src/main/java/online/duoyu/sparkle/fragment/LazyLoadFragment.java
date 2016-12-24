package online.duoyu.sparkle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import online.duoyu.sparkle.R;

/**
 * Created by littlekey on 12/20/16.
 */

public abstract class LazyLoadFragment extends BaseFragment {

  private boolean mShouldLoad = false;
  private boolean mLoaded = false;

  protected abstract View lazyLoad(LayoutInflater inflater, ViewGroup container);

  private void lazyLoad() {
    if (mShouldLoad && !mLoaded && getView() != null) {
      ViewGroup container = (ViewGroup) getView();
      container.addView(lazyLoad(getLayoutInflater(null), container));
      mLoaded = true;
    }
  }

  protected boolean isLoaded() {
    return mLoaded;
  }

  @Override
  public void setMenuVisibility(boolean menuVisible) {
    super.setMenuVisibility(menuVisible);
    if (menuVisible) {
      mShouldLoad = true;
      lazyLoad();
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_lazy_load, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    lazyLoad();
  }
}
