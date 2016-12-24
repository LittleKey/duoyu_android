package online.duoyu.sparkle.activity;

import android.content.Intent;

import online.duoyu.sparkle.fragment.BaseFragment;
import online.duoyu.sparkle.fragment.RegisterFragment;

/**
 * Created by littlekey on 12/24/16.
 */

public class RegisterActivity extends SingleFragmentActivity {

  @Override
  protected BaseFragment createFragment(Intent intent) {
    return RegisterFragment.newInstance();
  }
}
