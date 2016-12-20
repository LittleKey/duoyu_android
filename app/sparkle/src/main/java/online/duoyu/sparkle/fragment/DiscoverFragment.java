package online.duoyu.sparkle.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import online.duoyu.sparkle.R;
import online.duoyu.sparkle.utils.ResourcesUtils;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by littlekey on 12/20/16.
 */

public class DiscoverFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

  private ViewPager mViewPager;

  private TextView mBtnTitleFollow;
  private TextView mBtnTitleRecent;
  private TextView mBtnTitleWrite;

  private CompositeSubscription mCompositeSubscription;
  private int mCurrentPage;

  public static DiscoverFragment newInstance() {
    return new DiscoverFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    Timber.d("onCreateView");
    return inflater.inflate(R.layout.fragment_discover, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    Timber.d("onViewCreated");
    mCompositeSubscription = new CompositeSubscription();
    mBtnTitleFollow = (TextView) view.findViewById(R.id.btn_title_follow);
    mBtnTitleRecent = (TextView) view.findViewById(R.id.btn_title_recent);
    mBtnTitleWrite = (TextView) view.findViewById(R.id.btn_title_write);

    Subscription title_bar_subscription = Observable
        .from(new View[] {mBtnTitleFollow, mBtnTitleRecent, mBtnTitleWrite}).cache()
        .subscribe(new Action1<View>() {
          @Override
          public void call(final View view) {
            Subscription title_bar_btn_subscription = RxView.clicks(view).share()
                .debounce(300, TimeUnit.MICROSECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                  @Override
                  public void call(Void aVoid) {
                    switchTitleBarTab(view);
                    if (mViewPager != null) {
                      switch (view.getId()) {
                        case R.id.btn_title_follow:
                          mViewPager.setCurrentItem(0);
                          break;
                        case R.id.btn_title_recent:
                          mViewPager.setCurrentItem(1);
                          break;
                        case R.id.btn_title_write:
                          mViewPager.setCurrentItem(2);
                          break;
                      }
                    }
                  }
                });
            mCompositeSubscription.add(title_bar_btn_subscription);
          }
        });
    mCompositeSubscription.add(title_bar_subscription);
    mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
    FragmentStatePagerAdapter pagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
      @Override
      public Fragment getItem(int position) {
        switch (position) {
          case 0:
            return FollowUserDiaryFragment.newInstance("12 Dec.");
          case 1:
            return FollowUserDiaryFragment.newInstance("10 Oct.");
          case 2:
            return FollowUserDiaryFragment.newInstance("01 Jan");
          default:
            return null;
        }
      }

      @Override
      public int getCount() {
        return 3;
      }
    };
    mViewPager.setAdapter(pagerAdapter);
    mViewPager.setOffscreenPageLimit(2);
    mViewPager.addOnPageChangeListener(this);
    mViewPager.setCurrentItem(mCurrentPage = 0);
  }

  @Override
  public void setMenuVisibility(boolean menuVisible) {
    super.setMenuVisibility(menuVisible);
    Timber.d("setMenuVisibility: %b", menuVisible);
  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    Timber.d("setUserVisibleHint: %b", isVisibleToUser);
  }

  @Override
  public void onDestroyView() {
    mCompositeSubscription.unsubscribe();
    super.onDestroyView();
  }


  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    if (positionOffset == 0) {
      mCurrentPage = position;
    }
  }

  @Override
  public void onPageSelected(int position) {
    if (position != mCurrentPage) {
      switchTitleBarTab(mCurrentPage = position);
    }
  }

  @Override
  public void onPageScrollStateChanged(int state) {
    if (state == ViewPager.SCROLL_STATE_IDLE) {
      switchTitleBarTab(mCurrentPage);
    }
  }

  private void switchTitleBarTab(int position) {
    switch (position) {
      case 0:
        switchTitleBarTab(mBtnTitleFollow);
        break;
      case 1:
        switchTitleBarTab(mBtnTitleRecent);
        break;
      case 2:
        switchTitleBarTab(mBtnTitleWrite);
        break;
    }
  }

  private void switchTitleBarTab(View tab) {
    int white = ResourcesUtils.getColor(R.color.white);
    int primary_blue = ResourcesUtils.getColor(R.color.primary_blue);
    Drawable white_round_left = ResourcesUtils.getDrawable(R.drawable.bg_white_round_title_bar_left);
    Drawable white_round_right = ResourcesUtils.getDrawable(R.drawable.bg_white_round_title_bar_right);
    Drawable white_mid = ResourcesUtils.getDrawable(R.drawable.bg_white_round_title_bar_mid);
    Drawable primary_blue_left = ResourcesUtils.getDrawable(R.drawable.bg_primary_blue_round_title_bar_left);
    Drawable primary_blue_right = ResourcesUtils.getDrawable(R.drawable.bg_primary_blue_round_title_bar_right);
    if (tab != mBtnTitleFollow) {
      ResourcesUtils.setBackground(mBtnTitleFollow, white_round_left);
      mBtnTitleFollow.setTextColor(primary_blue);
    }
    if (tab != mBtnTitleRecent) {
      ResourcesUtils.setBackground(mBtnTitleRecent, white_mid);
      mBtnTitleRecent.setTextColor(primary_blue);
    }
    if (tab != mBtnTitleWrite) {
      ResourcesUtils.setBackground(mBtnTitleWrite, white_round_right);
      mBtnTitleWrite.setTextColor(primary_blue);
    }
    switch (tab.getId()) {
      case R.id.btn_title_follow:
        ResourcesUtils.setBackground(tab, primary_blue_left);
        break;
      case R.id.btn_title_recent:
        tab.setBackgroundColor(primary_blue);
        break;
      case R.id.btn_title_write:
        ResourcesUtils.setBackground(tab, primary_blue_right);
        break;
    }
    if (tab instanceof TextView) {
      ((TextView) tab).setTextColor(white);
    }
  }
}
