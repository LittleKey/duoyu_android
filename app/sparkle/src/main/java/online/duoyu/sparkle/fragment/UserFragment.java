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
import java.util.List;

import me.littlekey.mvp.presenter.ViewGroupPresenter;
import me.littlekey.network.NameValuePair;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.model.ModelFactory;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.presenter.SparklePresenterFactory;
import online.duoyu.sparkle.utils.Colorful;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.ResourcesUtils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by littlekey on 12/27/16.
 */

public class UserFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

  private ViewGroupPresenter mViewGroupPresenter;
  private Model mModel;
  private ViewPager mViewPager;
  private TextView mBtnTimeline;
  private TextView mBtnMyDiary;
  private TextView mBtnMyCorrect;
  private View mCurrentTab;

  public static UserFragment newInstance(Model model) {
    Bundle args = new Bundle();
    args.putParcelable(Const.KEY_MODEL, model);
    UserFragment fragment = new UserFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mViewGroupPresenter = SparklePresenterFactory.createUserInfoPresenter(
        (ViewGroup) inflater.inflate(R.layout.fragment_user, container, false));
    return mViewGroupPresenter.view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    Model model = getArguments().getParcelable(Const.KEY_MODEL);
    mModel = ModelFactory.createModelFromUser(model != null ? model.user : null, Model.Template.DATA);
    if (mModel == null) {
      return;
    }
    mViewGroupPresenter.bind(mModel);
    mBtnTimeline = (TextView) view.findViewById(R.id.btn_timeline);
    mBtnMyDiary = (TextView) view.findViewById(R.id.btn_my_diary);
    mBtnMyCorrect = (TextView) view.findViewById(R.id.btn_my_correct);

    Observable.just(mBtnTimeline, mBtnMyDiary, mBtnMyCorrect).cache()
        .observeOn(AndroidSchedulers.mainThread())
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
                        case R.id.btn_timeline:
                          mViewPager.setCurrentItem(0);
                          break;
                        case R.id.btn_my_diary:
                          mViewPager.setCurrentItem(1);
                          break;
                        case R.id.btn_my_correct:
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
        ArrayList<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new NameValuePair(Const.KEY_USER_ID, mModel.identity));
        bundle.putSerializable(Const.KEY_API_QUERY, pairs);
        switch (position) {
          case 0:
            bundle.putInt(Const.KEY_API_TYPE, ApiType.GET_USER_TIMELINE.ordinal());
            break;
          case 1:
            bundle.putInt(Const.KEY_API_TYPE, ApiType.GET_USER_PUBLISHED_DIARIES.ordinal());
            break;
          case 2:
            bundle.putInt(Const.KEY_API_TYPE, ApiType.GET_USER_PUBLISHED_CORRECTS.ordinal());
            break;
          default:
            return null;
        }
        return ListFragment.newInstance(bundle);
      }

      @Override
      public int getCount() {
        return 3;
      }
    };
    mViewPager.setAdapter(pagerAdapter);
    mViewPager.setOffscreenPageLimit(2);
    mViewPager.addOnPageChangeListener(this);
    switchTitleBarTab(mCurrentTab = mBtnTimeline);
  }

  @Override
  public void onDestroyView() {
    mViewGroupPresenter.unbind();
    super.onDestroyView();
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
        switchTitleBarTab(mBtnTimeline);
        break;
      case 1:
        switchTitleBarTab(mBtnMyDiary);
        break;
      case 2:
        switchTitleBarTab(mBtnMyCorrect);
        break;
    }
  }

  private void switchTitleBarTab(View tab) {
    if (tab != mCurrentTab) {
      mCurrentTab = tab;
    }
    int white = ResourcesUtils.getColor(R.color.white);
    int primary_color = Colorful.getThemeDelegate().getThemeColor().getPrimaryColor();
    Colorful.ThemeColor.TitleTabTheme titleTabTheme = Colorful.getThemeDelegate().getThemeColor().getTitleTabTheme();
    Drawable white_round_left = titleTabTheme.getLeftWhiteDrawable();
    Drawable primary_color_left = titleTabTheme.getLeftDrawable();
    Drawable white_mid = titleTabTheme.getMidWhiteDrawable();
    Drawable white_round_right = titleTabTheme.getRightWhiteDrawable();
    Drawable primary_color_right = titleTabTheme.getRightDrawable();
    if (tab != mBtnTimeline) {
      ResourcesUtils.setBackground(mBtnTimeline, white_round_left);
      mBtnTimeline.setTextColor(primary_color);
    }
    if (tab != mBtnMyDiary) {
      ResourcesUtils.setBackground(mBtnMyDiary, white_mid);
      mBtnMyDiary.setTextColor(primary_color);
    }
    if (tab != mBtnMyCorrect) {
      ResourcesUtils.setBackground(mBtnMyCorrect, white_round_right);
      mBtnMyCorrect.setTextColor(primary_color);
    }
    switch (tab.getId()) {
      case R.id.btn_timeline:
        ResourcesUtils.setBackground(tab, primary_color_left);
        break;
      case R.id.btn_my_diary:
        tab.setBackgroundColor(primary_color);
        break;
      case R.id.btn_my_correct:
        ResourcesUtils.setBackground(tab, primary_color_right);
        break;
    }
    if (tab instanceof TextView) {
      ((TextView) tab).setTextColor(white);
    }
  }
}
