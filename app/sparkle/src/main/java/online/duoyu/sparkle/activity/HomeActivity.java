package online.duoyu.sparkle.activity;

import android.app.Activity;
import android.content.Intent;

import de.greenrobot.event.EventBus;
import online.duoyu.sparkle.event.OnEditContentEvent;
import online.duoyu.sparkle.fragment.BaseFragment;
import online.duoyu.sparkle.fragment.HomeFragment;
import online.duoyu.sparkle.utils.Const;

/**
 * Created by littlekey on 12/19/16.
 */

public class HomeActivity extends SingleFragmentActivity {

  @Override
  protected BaseFragment createFragment(Intent intent) {
    return HomeFragment.newInstance();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode != Activity.RESULT_OK) {
      return;
    }
    switch (requestCode) {
      case Const.REQUEST_CODE_EDIT:
        EventBus.getDefault().post(
            new OnEditContentEvent(data.getCharSequenceExtra(Const.KEY_CONTENT)));
        break;
    }
  }
}
