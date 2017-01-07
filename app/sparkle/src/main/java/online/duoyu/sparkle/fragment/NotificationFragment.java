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
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import java.util.ArrayList;

import me.littlekey.network.NameValuePair;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.utils.Colorful;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.ResourcesUtils;
import online.duoyu.sparkle.utils.ThemeEventBus;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by littlekey on 12/20/16.
 */

public class NotificationFragment extends LazyLoadFragment implements ViewPager.OnPageChangeListener  {

  private ViewPager mViewPager;
  private TextView mBtnTitleAttention;
  private TextView mBtnTitleLiked;
  private TextView mBtnTitleFollow;

  private View mCurrentTab;

  public static NotificationFragment newInstance() {
    return new NotificationFragment();
  }

  @Override
  protected View lazyLoad(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_notification, container, false);
    mBtnTitleAttention = (TextView) view.findViewById(R.id.btn_title_attention);
    mBtnTitleLiked = (TextView) view.findViewById(R.id.btn_title_liked);
    mBtnTitleFollow = (TextView) view.findViewById(R.id.btn_title_follow);

    Observable.from(new View[] {mBtnTitleAttention, mBtnTitleLiked, mBtnTitleFollow}).cache()
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
                        case R.id.btn_title_attention:
                          mViewPager.setCurrentItem(0);
                          break;
                        case R.id.btn_title_liked:
                          mViewPager.setCurrentItem(1);
                          break;
                        case R.id.btn_title_follow:
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
        Bundle bundle = new Bundle();
        switch (position) {
          case 0:
            bundle.putInt(Const.KEY_API_TYPE, ApiType.ATTENTION_NOTIFICATION.ordinal());
            return ListFragment.newInstance(bundle);
          case 1:
            bundle.putInt(Const.KEY_API_TYPE, ApiType.LIKED_NOTIFICATION.ordinal());
            return ListFragment.newInstance(bundle);
          case 2:
            bundle.putInt(Const.KEY_API_TYPE, ApiType.FOLLOWER.ordinal());
            return ListFragment.newInstance(bundle);
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
    switchTitleBarTab(mCurrentTab = mBtnTitleAttention);
    return view;
  }

  public void onEventMainThread(ThemeEventBus.OnThemeChangeEvent event) {
    super.onEventMainThread(event);
    if (mCurrentTab != null) {
      switchTitleBarTab(mCurrentTab);
    }
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
  }

  @Override
  public void onPageSelected(int position) {
    switchTitleBarTab(position);
  }

  @Override
  public void onPageScrollStateChanged(int state) {
    if (state == ViewPager.SCROLL_STATE_IDLE) {
      switchTitleBarTab(mViewPager.getCurrentItem());
    }
  }

  private void switchTitleBarTab(int position) {
    switch (position) {
      case 0:
        switchTitleBarTab(mBtnTitleAttention);
        break;
      case 1:
        switchTitleBarTab(mBtnTitleLiked);
        break;
      case 2:
        switchTitleBarTab(mBtnTitleFollow);
        break;
    }
  }

  private void switchTitleBarTab(View tab) {
    if (tab != mCurrentTab) {
      mCurrentTab = tab;
    }
    int white = ResourcesUtils.getColor(R.color.white);
    int primary_color = Colorful.getThemeDelegate().getThemeColor().getPrimaryColor();
    Drawable[] title_drawables = Colorful.getThemeDelegate().getThemeColor().getTitleTabDrawables();
    Drawable white_round_left = title_drawables[0];
    Drawable primary_color_left = title_drawables[1];
    Drawable white_mid = title_drawables[2];
    Drawable white_round_right = title_drawables[3];
    Drawable primary_color_right = title_drawables[4];
    if (tab != mBtnTitleAttention) {
      ResourcesUtils.setBackground(mBtnTitleAttention, white_round_left);
      mBtnTitleAttention.setTextColor(primary_color);
    }
    if (tab != mBtnTitleLiked) {
      ResourcesUtils.setBackground(mBtnTitleLiked, white_mid);
      mBtnTitleLiked.setTextColor(primary_color);
    }
    if (tab != mBtnTitleFollow) {
      ResourcesUtils.setBackground(mBtnTitleFollow, white_round_right);
      mBtnTitleFollow.setTextColor(primary_color);
    }
    switch (tab.getId()) {
      case R.id.btn_title_attention:
        ResourcesUtils.setBackground(tab, primary_color_left);
        break;
      case R.id.btn_title_liked:
        tab.setBackgroundColor(primary_color);
        break;
      case R.id.btn_title_follow:
        ResourcesUtils.setBackground(tab, primary_color_right);
        break;
    }
    if (tab instanceof TextView) {
      ((TextView) tab).setTextColor(white);
    }
  }
}
