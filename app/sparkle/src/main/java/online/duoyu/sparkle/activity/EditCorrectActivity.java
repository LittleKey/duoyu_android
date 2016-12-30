package online.duoyu.sparkle.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import online.duoyu.sparkle.fragment.BaseFragment;
import online.duoyu.sparkle.fragment.EditCorrectFragment;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.utils.Colorful;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.NavigationManager;

/**
 * Created by littlekey on 12/27/16.
 */

public class EditCorrectActivity extends SingleFragmentActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      getWindow().setStatusBarColor(Colorful.getThemeDelegate().getThemeColor().getPrimaryColor());
    }
  }

  @Override
  protected BaseFragment createFragment(Intent intent) {
    Model model = NavigationManager.parseIntent(intent).getParcelable(Const.KEY_MODEL);
    return EditCorrectFragment.newInstance(model);
  }
}
