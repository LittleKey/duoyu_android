package online.duoyu.sparkle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.squareup.wire.Wire;

import de.greenrobot.event.EventBus;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.event.OnDiariesAmountUpdateEvent;
import online.duoyu.sparkle.fragment.BaseFragment;
import online.duoyu.sparkle.fragment.ListFragment;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.NavigationManager;
import online.duoyu.sparkle.utils.SparkleUtils;

/**
 * Created by littlekey on 1/13/17.
 */

public class PublishedDiariesActivity extends SingleFragmentActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    EventBus.getDefault().register(this);
    super.onCreate(savedInstanceState);
  }

  public void onEventMainThread(OnDiariesAmountUpdateEvent event) {
    if (event.apiType == ApiType.MY_PUBLISHED_DIARIES) {
      updateTitle(SparkleUtils.formatString(R.string.all_diaries, event.amount));
    }
  }

  @Override
  protected void onDestroy() {
    EventBus.getDefault().unregister(this);
    super.onDestroy();
  }

  @Override
  protected BaseFragment createFragment(Intent intent) {
    Bundle bundle = new Bundle();
    bundle.putInt(Const.KEY_API_TYPE, ApiType.MY_PUBLISHED_DIARIES.ordinal());
    return ListFragment.newInstance(bundle).setLazyLoad(false);
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
    Model model = NavigationManager.parseIntent(getIntent()).getParcelable(Const.KEY_MODEL);
    return SparkleUtils.formatString(R.string.all_diaries, model == null ? 0 : Wire.get(model.count.diaries, 0));
  }
}
