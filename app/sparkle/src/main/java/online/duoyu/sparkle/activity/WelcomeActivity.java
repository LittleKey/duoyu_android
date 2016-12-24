package online.duoyu.sparkle.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.RequestFuture;

import java.util.HashMap;
import java.util.Map;

import online.duoyu.sparkle.R;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.model.business.CurrentResponse;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.network.SparkleRequest;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.NavigationManager;
import online.duoyu.sparkle.utils.PreferenceUtils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.internal.util.ActionSubscriber;
import rx.schedulers.Schedulers;

/**
 * Created by littlekey on 12/21/16.
 */

public class WelcomeActivity extends BaseActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);
    if (!SparkleApplication.getInstance().getAccountManager().isSignIn()) {
      NavigationManager.navigationTo(WelcomeActivity.this, LoginActivity.class);
      finish();
      return;
    }
    final String token = PreferenceUtils.getString(Const.LAST_ACTION, Const.LAST_TOKEN, null);
    RequestFuture<CurrentResponse> future = RequestFuture.newFuture();
    SparkleRequest<CurrentResponse> request = SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(ApiType.CURRENT_USER, null, CurrentResponse.class, future, future);
    Map<String, String> headers;
    try {
      headers = request.getHeaders();
    } catch (AuthFailureError ignore) {
      headers = new HashMap<>();
    }
    headers.put(Const.KEY_AUTHORIZATION, token);
    request.setHeaders(headers);
    request.setTag(this);
    request.submit();
    Observable.from(future, Schedulers.newThread())
        .compose(this.<CurrentResponse>bindToLifecycle())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ActionSubscriber<>(new Action1<CurrentResponse>() {
          @Override
          public void call(CurrentResponse currentResponse) {
            SparkleApplication.getInstance().getAccountManager().login(currentResponse.user, token);
            NavigationManager.navigationTo(WelcomeActivity.this, HomeActivity.class);
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            SparkleApplication.getInstance().getAccountManager().logout();
            NavigationManager.navigationTo(WelcomeActivity.this, LoginActivity.class);
          }
        }, new Action0() {
          @Override
          public void call() {
            WelcomeActivity.this.finish();
          }
        }));
  }
}
