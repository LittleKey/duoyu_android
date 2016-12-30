package online.duoyu.sparkle.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.WeakReference;

import online.duoyu.sparkle.R;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.utils.Colorful;
import online.duoyu.sparkle.utils.ToastUtils;

/**
 * Created by littlekey on 12/19/16.
 */

public class BaseActivity extends RxAppCompatActivity {

  private ExitHandler mExitHandler;
  private String mCurrentThemeString;

  protected boolean isDialog() {
    return false;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    if (isDialog() != Colorful.getThemeDelegate().isDialog()) {
      Colorful.config(this)
          .dialog(isDialog())
          .apply();
    }
    mCurrentThemeString = Colorful.getThemeString();
    setTheme(Colorful.getThemeDelegate().getStyle());
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      if (Colorful.getThemeDelegate().isTranslucent()) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      }
      ActivityManager.TaskDescription tDesc = new ActivityManager.TaskDescription(null, null,
          Colorful.getThemeDelegate().getThemeColor().getPrimaryColor());
      setTaskDescription(tDesc);
    }
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void onResume() {
    super.onResume();
//    if (!TextUtils.equals(Colorful.getThemeString(), mCurrentThemeString)) {
//      recreate();
//    }
    //    ZhugeSDK.getInstance().init(getApplicationContext());
    //    MobclickAgent.onResume(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    //    MobclickAgent.onPause(this);
    //    HuPuMountInterface.onPause(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    //    HuPuMountInterface.onStop(this);
  }

  @Override
  protected void onDestroy() {
    SparkleApplication.getInstance().getRequestManager().cancel(this);
    super.onDestroy();
    //    HuPuMountInterface.onDestroy(this);
  }

  @Override
  public void onBackPressed() {
    if (isTaskRoot()) {
      if (!getSupportFragmentManager().popBackStackImmediate()) {
        if (mExitHandler == null) {
          mExitHandler = new ExitHandler(this);
        }
        mExitHandler.tryExit();
      }
    } else {
      super.onBackPressed();
    }
  }

  // 关闭软键盘
  public void closeKeyboard() {
    View view = getWindow().peekDecorView();
    if (view != null) {
      InputMethodManager inputManager =
          (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }

  public void showKeyBoard() {
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
  }

  private static class ExitHandler extends Handler {

    private final static int MSG_TRY_EXIT = 0;
    private final static int MSG_CANCEL = 1;

    private final static int TIME_OUT = 2 * 1000;

    private WeakReference<BaseActivity> mWeakActivity;
    private boolean mFlag;

    public ExitHandler(BaseActivity activity) {
      super();
      mWeakActivity = new WeakReference<>(activity);
      mFlag = false;
    }

    @Override
    public void handleMessage(Message msg) {
      BaseActivity activity = mWeakActivity.get();
      if (activity == null || activity.isFinishing()) {
        return;
      }
      switch (msg.what) {
        case MSG_TRY_EXIT:
          if (mFlag) {
            activity.finish();
          } else {
            ToastUtils.toast(R.string.more_press_exit);
          }
          mFlag = true;
          sendMessageDelayed(obtainMessage(MSG_CANCEL), TIME_OUT);
          break;
        case MSG_CANCEL:
          mFlag = false;
          break;
      }
    }

    public void tryExit() {
      Message.obtain(this, MSG_TRY_EXIT).sendToTarget();
    }
  }
}
