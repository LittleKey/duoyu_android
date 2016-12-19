package online.duoyu.sparkle;

import android.graphics.Typeface;
import android.text.TextUtils;

import me.littlekey.base.utils.LogUtils;
import me.littlekey.mvp.BaseApplication;
import me.littlekey.network.ApiContext;
import online.duoyu.sparkle.account.AccountManager;
import online.duoyu.sparkle.network.SparkleRequestManager;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.PreferenceUtils;

/**
 * Created by littlekey on 12/19/16.
 */

public class SparkleApplication extends BaseApplication implements ApiContext {
  private static SparkleApplication sBaseApplicationInstance;
  private SparkleRequestManager mRequestManager;
  //  private UpdateAgent mUpdateAgent;
  private Typeface mIconTypeface;
  private AccountManager mAccountManager;

  public SparkleApplication() {
    sBaseApplicationInstance = this;
  }

  public static SparkleApplication getInstance() {
    return sBaseApplicationInstance;
  }

  @Override
  public void onCreate() {
    super.onCreate();

    LogUtils.init(BuildConfig.DEBUG);
    initializeVolley();
    //    initializeUpdate();
    initializeAccount();
    initializeIconTypeface();
  }


  @Override
  public SparkleRequestManager getRequestManager() {
    return mRequestManager;
  }

  //  public UpdateAgent getUpdateAgent() {
  //    return mUpdateAgent;
  //  }

  public AccountManager getAccountManager() {
    return mAccountManager;
  }

  private void initializeVolley() {
    mRequestManager = new SparkleRequestManager(this);
    for (String key : new String[]{Const.KEY_S, Const.KEY_LV, Const.KEY_IGNEOUS}) {
      String value;
      if (!TextUtils.isEmpty(value = PreferenceUtils.getString(Const.LAST_COOKIE, key, Const.EMPTY_STRING))) {
        mRequestManager.addCookie(key, value);
      }
    }
  }

  //  private void initializeUpdate() {
  //    mUpdateAgent = new UpdateAgent(this, this);
  //  }

  private void initializeIconTypeface() {
    mIconTypeface = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
  }

  public Typeface getIconTypeface() {
    return mIconTypeface;
  }

  private void initializeAccount() {
    mAccountManager = new AccountManager();
  }
}
