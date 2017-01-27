package online.duoyu.sparkle.activity;

import android.content.Intent;

import online.duoyu.sparkle.R;
import online.duoyu.sparkle.fragment.BaseFragment;
import online.duoyu.sparkle.fragment.ForgotPasswordFragment;
import online.duoyu.sparkle.utils.SparkleUtils;

/**
 * Created by littlekey on 1/27/17.
 */

public class ForgotPasswordActivity extends SingleFragmentActivity {

  @Override
  protected BaseFragment createFragment(Intent intent) {
    return ForgotPasswordFragment.newInstance();
  }

  @Override
  protected boolean hasToolbar() {
    return true;
  }

  @Override
  protected boolean hasBackBtn() {
    return true;
  }

  @Override
  protected String activityTitle() {
    return SparkleUtils.formatString(R.string.retrieve_password);
  }
}
