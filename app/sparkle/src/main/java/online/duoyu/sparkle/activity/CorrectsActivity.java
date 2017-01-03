package online.duoyu.sparkle.activity;

import android.content.Intent;
import android.os.Bundle;

import online.duoyu.sparkle.fragment.BaseFragment;
import online.duoyu.sparkle.fragment.ListFragment;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.NavigationManager;

/**
 * Created by littlekey on 12/27/16.
 */

public class CorrectsActivity extends SingleFragmentActivity {

  @Override
  protected BaseFragment createFragment(Intent intent) {
    Bundle bundle = new Bundle();
    bundle.putInt(Const.KEY_API_TYPE, ApiType.GET_CORRECTS_BY_DIARY.ordinal());
    bundle.putBundle(Const.KEY_EXTRA, NavigationManager.parseIntent(intent));
    return ListFragment.newInstance(bundle).setLazyLoad(false);
  }
}
