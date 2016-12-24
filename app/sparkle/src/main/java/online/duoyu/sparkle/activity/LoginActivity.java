package online.duoyu.sparkle.activity;

import android.content.Intent;

import online.duoyu.sparkle.fragment.BaseFragment;
import online.duoyu.sparkle.fragment.LoginFragment;

/**
 * Created by littlekey on 12/21/16.
 */

public class LoginActivity extends SingleFragmentActivity {

  @Override
  protected BaseFragment createFragment(Intent intent) {
    return LoginFragment.newInstance();
  }
}
