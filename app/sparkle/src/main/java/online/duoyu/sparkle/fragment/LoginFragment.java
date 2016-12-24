package online.duoyu.sparkle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.toolbox.RequestFuture;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import online.duoyu.sparkle.R;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.activity.HomeActivity;
import online.duoyu.sparkle.activity.RegisterActivity;
import online.duoyu.sparkle.model.business.LoginResponse;
import online.duoyu.sparkle.model.business.RegisterRequest;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.network.SparkleRequest;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.NavigationManager;
import online.duoyu.sparkle.utils.SparkleUtils;
import online.duoyu.sparkle.utils.ToastUtils;
import online.duoyu.sparkle.widget.StatefulButton;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.functions.Func1;
import rx.internal.util.ActionSubscriber;
import rx.schedulers.Schedulers;

/**
 * Created by littlekey on 12/21/16.
 */

public class LoginFragment extends BaseFragment {

  private CharSequence mEmail;
  private CharSequence mPassword;

  public static LoginFragment newInstance() {
    Bundle args = new Bundle();
    LoginFragment fragment = new LoginFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    final StatefulButton btn_login = (StatefulButton) view.findViewById(R.id.btn_login);
    btn_login.setState(StatefulButton.STATE_CANCELED);
    btn_login.setEnabled(false);
    Observable
        .just(R.id.edit_email, R.id.edit_password).cache()
        .map(new Func1<Integer, EditText>() {
          @Override
          public EditText call(Integer integer) {
            return (EditText) view.findViewById(integer);
          }
        })
        .filter(new Func1<EditText, Boolean>() {
          @Override
          public Boolean call(EditText editText) {
            return editText != null;
          }
        })
        .flatMap(new Func1<EditText, Observable<Boolean>>() {
          @Override
          public Observable<Boolean> call(final EditText editText) {
            if (editText.getId() == R.id.edit_password) {
              editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                  if ((actionId & EditorInfo.IME_MASK_ACTION) == EditorInfo.IME_ACTION_DONE) {
                    login(mEmail.toString(), mPassword.toString());
                    return true;
                  }
                  return false;
                }
              });
            }
            return RxTextView.textChanges(editText)
                .compose(LoginFragment.this.<CharSequence>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<CharSequence, Boolean>() {
                  @Override
                  public Boolean call(CharSequence charSequence) {
                    switch (editText.getId()) {
                      case R.id.edit_email:
                        mEmail = charSequence;
                        break;
                      case R.id.edit_password:
                        mPassword = charSequence;
                        break;
                    }
                    return !TextUtils.isEmpty(mEmail) && !TextUtils.isEmpty(mPassword);
                  }
                });
          }
        })
        .subscribe(new Action1<Boolean>() {
          @Override
          public void call(Boolean aBoolean) {
            btn_login.setEnabled(aBoolean);
            btn_login.setState(aBoolean ? StatefulButton.STATE_DONE : StatefulButton.STATE_CANCELED);
          }
        });
    RxView.clicks(btn_login)
        .compose(LoginFragment.this.<Void>bindToLifecycle())
        .throttleFirst(1, TimeUnit.SECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Void>() {
          @Override
          public void call(Void aVoid) {
            login(mEmail.toString(), mPassword.toString());
          }
        });
    Observable.just(R.id.register).cache()
        .map(new Func1<Integer, View>() {
          @Override
          public View call(Integer integer) {
            return view.findViewById(integer);
          }
        })
        .filter(new Func1<View, Boolean>() {
          @Override
          public Boolean call(View view) {
            return view != null;
          }
        })
        .flatMap(new Func1<View, Observable<Void>>() {
          @Override
          public Observable<Void> call(View view) {
            return RxView.clicks(view)
                .compose(LoginFragment.this.<Void>bindToLifecycle())
                .throttleFirst(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread());
          }
        })
        .subscribe(new Action1<Void>() {
          @Override
          public void call(Void aVoid) {
            NavigationManager.navigationTo(getActivity(), RegisterActivity.class);
          }
        });
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_login, container, false);
  }

  private void login(String email, String password) {
    Map<String, String> body = new HashMap<>();
    body.put(Const.KEY_EMAIL, email);
    body.put(Const.KEY_PASSWORD, password);
    final RequestFuture<LoginResponse> future = RequestFuture.newFuture();
    SparkleRequest<LoginResponse> request = SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(ApiType.LOGIN, body, LoginResponse.class, future, future);
    request.setTag(this);
    request.submit();
    Observable.from(future, Schedulers.newThread())
        .compose(this.<LoginResponse>bindToLifecycle())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ActionSubscriber<>(new Action1<LoginResponse>() {
          @Override
          public void call(LoginResponse loginResponse) {
            if (loginResponse.success != null && loginResponse.success
                && !TextUtils.isEmpty(loginResponse.token)) {
              SparkleApplication.getInstance().getAccountManager()
                  .login(loginResponse.user, loginResponse.token);
              NavigationManager.navigationTo(getActivity(), HomeActivity.class);
              getActivity().finish();
            } else {
              ToastUtils.toast(R.string.login_error);
            }
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            ToastUtils.toast(R.string.login_error);
          }
        }, Actions.empty()));
  }
}
