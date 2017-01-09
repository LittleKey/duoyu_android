package online.duoyu.sparkle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import online.duoyu.sparkle.R;
import online.duoyu.sparkle.fragment.BaseFragment;

/**
 * Created by littlekey on 12/19/16.
 */

public abstract class SingleFragmentActivity extends BaseActivity {

  private TextView mTitleView;
  private BaseFragment mFragment;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayout());
    if (hasToolbar()) {
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      if (toolbar == null) {
        return;
      }
      setSupportActionBar(toolbar);
      if (hasBackBtn()) {
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            onBackPressed();
          }
        });
      } else if (hasCancelBtn()) {
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            finish();
          }
        });
      }
      mTitleView = (TextView) toolbar.findViewById(R.id.title);
      if (!TextUtils.isEmpty(activityTitle())) {
        mTitleView.setText(activityTitle());
      } else {
        mTitleView.setVisibility(View.GONE);
      }
    }
    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.fragment_container);
    if (fragment == null) {
      fm.beginTransaction()
          .add(R.id.fragment_container, mFragment = createFragment(getIntent()))
          .commit();
    }
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    FragmentManager fm = getSupportFragmentManager();
    fm.beginTransaction()
        .replace(R.id.fragment_container, mFragment = createFragment(intent))
        .commit();
  }

  @Override
  public void onBackPressed() {
    if (!getFragment().getChildFragmentManager().popBackStackImmediate()) {
      super.onBackPressed();
    }
  }

  protected boolean hasToolbar() {
    return false;
  }

  protected boolean hasBackBtn() {
    return hasToolbar();
  }

  protected boolean hasCancelBtn() {
    return hasToolbar() && !hasBackBtn();
  }

  protected String activityTitle() {
    return null;
  }

  protected void updateTitle(String title) {
    mTitleView.setText(title);
  }

  protected @LayoutRes
  int getLayout() {
    return hasToolbar() ? R.layout.activity_single_fragment : R.layout.activity_without_toolbar;
  }

  protected abstract BaseFragment createFragment(Intent intent);

  protected BaseFragment getFragment() {
    return mFragment;
  }
}
