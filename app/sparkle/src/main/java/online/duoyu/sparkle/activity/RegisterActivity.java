package online.duoyu.sparkle.activity;

import android.content.Intent;

import online.duoyu.sparkle.R;
import online.duoyu.sparkle.fragment.BaseFragment;
import online.duoyu.sparkle.fragment.RegisterStep1Fragment;
import online.duoyu.sparkle.utils.SparkleUtils;

/**
 * Created by littlekey on 12/24/16.
 */

public class RegisterActivity extends SingleFragmentActivity {

  @Override
  protected BaseFragment createFragment(Intent intent) {
    return RegisterStep1Fragment.newInstance();
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
    return SparkleUtils.formatString(R.string.register);
  }
}
