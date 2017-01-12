package online.duoyu.sparkle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.squareup.wire.Wire;

import de.greenrobot.event.EventBus;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.event.OnCorrectsAmountUpdate;
import online.duoyu.sparkle.fragment.BaseFragment;
import online.duoyu.sparkle.fragment.ListFragment;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.NavigationManager;
import online.duoyu.sparkle.utils.SparkleUtils;

/**
 * Created by littlekey on 12/27/16.
 */

public class CorrectsActivity extends SingleFragmentActivity {

  private Model mModel;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    Bundle bundle = NavigationManager.parseIntent(getIntent());
    mModel = bundle.getParcelable(Const.KEY_MODEL);
    super.onCreate(savedInstanceState);
    EventBus.getDefault().register(this);
  }

  public void onEventMainThread(OnCorrectsAmountUpdate event) {
    if (TextUtils.equals(mModel.identity, event.diary_id)) {
      mModel = mModel.newBuilder()
          .count(mModel.count.newBuilder().corrects(event.amount).build())
          .build();
      updateTitle(SparkleUtils.formatString(R.string.all_corrects, event.amount));
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
    bundle.putInt(Const.KEY_API_TYPE, ApiType.GET_CORRECTS_BY_DIARY.ordinal());
    bundle.putBundle(Const.KEY_EXTRA, NavigationManager.parseIntent(intent));
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
    return SparkleUtils.formatString(R.string.all_corrects, Wire.get(mModel.count.corrects, 0));
  }
}
