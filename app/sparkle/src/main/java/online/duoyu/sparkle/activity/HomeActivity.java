package online.duoyu.sparkle.activity;

import android.content.Intent;

import online.duoyu.sparkle.fragment.BaseFragment;
import online.duoyu.sparkle.fragment.HomeFragment;

/**
 * Created by littlekey on 12/19/16.
 */

public class HomeActivity extends SingleFragmentActivity {

  @Override
  protected BaseFragment createFragment(Intent intent) {
    return HomeFragment.newInstance();
  }
}
