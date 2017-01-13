package online.duoyu.sparkle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.littlekey.mvp.presenter.ViewGroupPresenter;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.presenter.SparklePresenterFactory;
import online.duoyu.sparkle.widget.StatefulButton;

/**
 * Created by littlekey on 12/20/16.
 */

public class UserCenterFragment extends LazyLoadFragment {

  private ViewGroupPresenter mPresenterGroup;
  private Model mModel;

  public static UserCenterFragment newInstance() {
    return new UserCenterFragment();
  }

  @Override
  protected View lazyLoad(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
    mPresenterGroup = SparklePresenterFactory.createUserInfoPresenter(
        (ViewGroup) inflater.inflate(R.layout.fragment_user_center, container, false));
    mModel = SparkleApplication.getInstance().getAccountManager().getUser();
    if (mModel != null) {
      mPresenterGroup.bind(mModel);
    }
    StatefulButton btn_edit_profile = (StatefulButton) mPresenterGroup
        .view.findViewById(R.id.theme_btn_edit_profile);
    btn_edit_profile.setState(StatefulButton.STATE_EDIT_PROFILE);
    return mPresenterGroup.view;
  }
}
