package online.duoyu.sparkle.activity;

import android.content.Intent;

import online.duoyu.sparkle.fragment.BaseFragment;
import online.duoyu.sparkle.fragment.DiaryFragment;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.NavigationManager;

/**
 * Created by littlekey on 12/26/16.
 */

public class DiaryActivity extends SingleFragmentActivity {

  @Override
  protected BaseFragment createFragment(Intent intent) {
    Model model = NavigationManager.parseIntent(intent).getParcelable(Const.KEY_MODEL);
    return DiaryFragment.newInstance(model);
  }

  @Override
  protected boolean isDialog() {
    return true;
  }
}
