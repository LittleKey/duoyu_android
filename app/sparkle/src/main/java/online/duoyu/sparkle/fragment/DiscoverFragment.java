package online.duoyu.sparkle.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import de.greenrobot.event.EventBus;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.event.UpdateMonthEvent;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.ResourcesUtils;
import online.duoyu.sparkle.utils.SparkleUtils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by littlekey on 12/20/16.
 */

public class DiscoverFragment extends LazyLoadFragment implements ViewPager.OnPageChangeListener {

  private ViewPager mViewPager;

  private TextView mBtnTitleFollow;
  private TextView mBtnTitleRecent;
  private TextView mBtnTitleWrite;
  private TextView mMonthView;

  private int mCurrentPage;
  private String mMonth;

  public static DiscoverFragment newInstance() {
    return new DiscoverFragment();
  }

  @Override
  public void onResume() {
    super.onResume();
    EventBus.getDefault().register(this);
  }

  public void onEventMainThread(UpdateMonthEvent event) {
    if (!TextUtils.equals(mMonth, event.month)) {
      mMonth = event.month;
      updateMonth();
    }
  }

  private void updateMonth() {
    if (isLoaded() && mMonthView != null && !TextUtils.isEmpty(mMonth)) {
      mMonthView.setText(mMonth);
    }
  }

  private void updateTitle(String content) {
    if (isLoaded() && mMonthView != null && !TextUtils.isEmpty(content)) {
      mMonthView.setText(content);
    }
  }

  @Override
  public void onPause() {
    EventBus.getDefault().unregister(this);
    super.onPause();
  }

  @Override
  protected View lazyLoad(LayoutInflater inflater, ViewGroup container) {
    View view = inflater.inflate(R.layout.fragment_discover, container, false);
    mMonthView = (TextView) view.findViewById(R.id.month);
    mBtnTitleFollow = (TextView) view.findViewById(R.id.btn_title_follow);
    mBtnTitleRecent = (TextView) view.findViewById(R.id.btn_title_recent);
    mBtnTitleWrite = (TextView) view.findViewById(R.id.btn_title_write);

    Observable.from(new View[] {mBtnTitleFollow, mBtnTitleRecent, mBtnTitleWrite}).cache()
        .subscribe(new Action1<View>() {
          @Override
          public void call(final View view) {
            Observable<Object> title_bar_tab_click_observer = RxView.clicks(view).share()
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleAndroid.bindView(view));
            title_bar_tab_click_observer
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                  @Override
                  public void call(Object o) {
                    switchTitleBarTab(view);
                  }
                });
            title_bar_tab_click_observer
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                  @Override
                  public void call(Object o) {
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
          }
        });
    mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
    FragmentStatePagerAdapter pagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
      @Override
      public Fragment getItem(int position) {
        switch (position) {
          case 0:
            Bundle bundle = new Bundle();
            bundle.putInt(Const.KEY_API_TYPE, ApiType.FOLLOW_USER_DIARY.ordinal());
            return ListFragment.newInstance(bundle);
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
    return view;
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
        updateMonth();
        switchTitleBarTab(mBtnTitleFollow);
        break;
      case 1:
        updateTitle(SparkleUtils.formatString("Diary"));
        switchTitleBarTab(mBtnTitleRecent);
        break;
      case 2:
        updateTitle(SparkleUtils.formatString("Diary"));
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
