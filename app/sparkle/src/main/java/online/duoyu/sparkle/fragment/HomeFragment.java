package online.duoyu.sparkle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import online.duoyu.sparkle.R;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by littlekey on 12/19/16.
 */

public class HomeFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

  private ViewPager mViewPager;
  private View mBtnDiscover;
  private View mBtnNotification;
  private View mBtnUserCenter;

  public static HomeFragment newInstance() {
    return new HomeFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_home, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
    mBtnDiscover = view.findViewById(R.id.btn_discover);
    mBtnNotification = view.findViewById(R.id.btn_notification);
    mBtnUserCenter = view.findViewById(R.id.btn_user_center);
    Observable.just(mBtnDiscover, mBtnNotification, mBtnUserCenter)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<View>() {
          @Override
          public void call(final View view) {
            RxView.clicks(view).share()
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleAndroid.<Void>bindView(view))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                  @Override
                  public void call(Void aVoid) {
                    // TODO : update bottom tab state
                    if (mViewPager != null) {
                      switch (view.getId()) {
                        case R.id.btn_discover:
                          mViewPager.setCurrentItem(0);
                          break;
                        case R.id.btn_notification:
                          mViewPager.setCurrentItem(1);
                          break;
                        case R.id.btn_user_center:
                          mViewPager.setCurrentItem(2);
                          break;
                      }
                    }
                  }
                });
          }
        });
    FragmentStatePagerAdapter pagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
      @Override
      public Fragment getItem(int position) {
        switch (position) {
          case 0:
            return DiscoverFragment.newInstance();
          case 1:
            return NotificationFragment.newInstance();
          case 2:
            return UserCenterFragment.newInstance();
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
    // TODO : update bottom tab state
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

  }

  @Override
  public void onPageSelected(int position) {
    // TODO : update bottom tab state
  }

  @Override
  public void onPageScrollStateChanged(int state) {
    if (state == ViewPager.SCROLL_STATE_IDLE) {
      // TODO : update bottom tab state
    }
  }
}
