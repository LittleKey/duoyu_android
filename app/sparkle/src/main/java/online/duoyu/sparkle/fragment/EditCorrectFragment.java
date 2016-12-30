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
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.utils.Const;

/**
 * Created by littlekey on 12/27/16.
 */

public class EditCorrectFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

  private ViewPager mViewPager;

  public static EditCorrectFragment newInstance(Model model) {
    Bundle args = new Bundle();
    args.putParcelable(Const.KEY_MODEL, model);
    EditCorrectFragment fragment = new EditCorrectFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_edit_correct, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    final Model model = getArguments().getParcelable(Const.KEY_MODEL);
    if (model == null) {
      return;
    }
    mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
    FragmentStatePagerAdapter pagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
      @Override
      public Fragment getItem(int position) {
        return EditCorrectChildFragment.newInstance(model, position);
      }

      @Override
      public int getCount() {
        return 30;
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
