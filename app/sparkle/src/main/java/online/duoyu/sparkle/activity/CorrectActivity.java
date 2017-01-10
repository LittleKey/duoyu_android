package online.duoyu.sparkle.activity;

import android.content.Intent;

import online.duoyu.sparkle.fragment.BaseFragment;
import online.duoyu.sparkle.fragment.CorrectFragment;

/**
 * Created by littlekey on 1/7/17.
 */

public class CorrectActivity extends SingleFragmentActivity {

  @Override
  protected BaseFragment createFragment(Intent intent) {
    return CorrectFragment.newInstance();
  }
}
