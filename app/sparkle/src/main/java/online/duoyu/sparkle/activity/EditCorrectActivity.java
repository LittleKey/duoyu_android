package online.duoyu.sparkle.activity;

import android.content.Intent;

import online.duoyu.sparkle.fragment.BaseFragment;
import online.duoyu.sparkle.fragment.EditCorrectFragment;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.NavigationManager;
import online.duoyu.sparkle.utils.ToastUtils;

/**
 * Created by littlekey on 12/27/16.
 */

public class EditCorrectActivity extends SingleFragmentActivity {

  @Override
  protected BaseFragment createFragment(Intent intent) {
    Model model = NavigationManager.parseIntent(intent).getParcelable(Const.KEY_MODEL);
    return EditCorrectFragment.newInstance(model);
  }
}
