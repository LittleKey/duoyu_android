package online.duoyu.sparkle.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import online.duoyu.sparkle.R;
import online.duoyu.sparkle.fragment.BaseFragment;
import online.duoyu.sparkle.fragment.EditFragment;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.NavigationManager;
import online.duoyu.sparkle.utils.SparkleUtils;

/**
 * Created by littlekey on 1/8/17.
 */

public class EditActivity extends SingleFragmentActivity {

  @Override
  protected BaseFragment createFragment(Intent intent) {
    return EditFragment.newInstance(NavigationManager.parseIntent(intent).getCharSequence(Const.KEY_CONTENT));
  }

  @Override
  protected boolean hasToolbar() {
    return true;
  }

  @Override
  protected boolean hasBackBtn() {
    return false;
  }

  @Override
  protected boolean hasCancelBtn() {
    return true;
  }

  @Override
  protected String activityTitle() {
    return SparkleUtils.formatString(R.string.edit_diary);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.edit_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_complete:
        setResult(Activity.RESULT_OK, getIntent());
        finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
