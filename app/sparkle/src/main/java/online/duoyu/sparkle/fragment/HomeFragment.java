package online.duoyu.sparkle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import online.duoyu.sparkle.R;

/**
 * Created by littlekey on 12/19/16.
 */

public class HomeFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

  private ViewPager mViewPager;

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
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

  }

  @Override
  public void onPageSelected(int position) {

  }

  @Override
  public void onPageScrollStateChanged(int state) {

  }
}
