package online.duoyu.sparkle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import online.duoyu.sparkle.R;
import online.duoyu.sparkle.utils.Colorful;

/**
 * Created by littlekey on 12/20/16.
 */

public abstract class LazyLoadFragment extends BaseFragment {

  private boolean mShouldLoad = false;
  private boolean mLoaded = false;
  private Bundle mSavedInstanceState;

  protected abstract View lazyLoad(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState);

  private void lazyLoad() {
    if (mShouldLoad && !mLoaded && getView() != null) {
      ViewGroup container = (ViewGroup) getView();
      container.removeAllViews();
      container.addView(lazyLoad(getLayoutInflater(mSavedInstanceState), container, mSavedInstanceState),
          new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
      mLoaded = true;
      Colorful.changeTheme(container);
    }
  }

  protected boolean isLoaded() {
    return mLoaded;
  }

  @Override
  public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
    mSavedInstanceState= savedInstanceState;
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
    lazyLoad();
    super.onViewCreated(view, savedInstanceState);
    mSavedInstanceState = savedInstanceState;
  }
}
