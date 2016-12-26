package online.duoyu.sparkle.activity;

import android.content.Intent;

import online.duoyu.sparkle.fragment.BaseFragment;
import online.duoyu.sparkle.fragment.CorrectsFragment;
import online.duoyu.sparkle.utils.ToastUtils;

/**
 * Created by littlekey on 12/27/16.
 */

public class CorrectsActivity extends SingleFragmentActivity {

  @Override
  protected BaseFragment createFragment(Intent intent) {
    ToastUtils.toast(CorrectsActivity.class.getSimpleName());
    return CorrectsFragment.newInstance();
  }
}
