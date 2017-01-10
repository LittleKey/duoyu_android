package online.duoyu.sparkle.activity;

import android.content.Intent;
import android.os.Bundle;

import com.squareup.wire.Wire;

import online.duoyu.sparkle.R;
import online.duoyu.sparkle.fragment.BaseFragment;
import online.duoyu.sparkle.fragment.CommentsFragment;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.NavigationManager;
import online.duoyu.sparkle.utils.SparkleUtils;

/**
 * Created by littlekey on 12/27/16.
 */

public class CommentsActivity extends SingleFragmentActivity {

  @Override
  protected BaseFragment createFragment(Intent intent) {
    Bundle bundle = new Bundle();
    bundle.putInt(Const.KEY_API_TYPE, ApiType.GET_COMMENTS_BY_DIARY.ordinal());
    bundle.putBundle(Const.KEY_EXTRA, NavigationManager.parseIntent(intent));
    return CommentsFragment.newInstance(bundle);
  }

  @Override
  protected boolean hasToolbar() {
    return true;
  }

  @Override
  protected boolean hasBackBtn() {
    return true;
  }

  @Override
  protected String activityTitle() {
    Bundle bundle = NavigationManager.parseIntent(getIntent());
    Model model = bundle.getParcelable(Const.KEY_MODEL);
    return SparkleUtils.formatString(R.string.all_comments,
        model == null ? 0 : model.count == null ? 0 : Wire.get(model.count.comments, 0));
  }
}
