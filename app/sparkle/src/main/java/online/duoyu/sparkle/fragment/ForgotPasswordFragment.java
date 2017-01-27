package online.duoyu.sparkle.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.android.volley.toolbox.RequestFuture;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.squareup.wire.Wire;
import com.trello.rxlifecycle.android.FragmentEvent;

import java.util.concurrent.TimeUnit;

import okio.ByteString;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.activity.LoginActivity;
import online.duoyu.sparkle.model.business.ChangePasswordRequest;
import online.duoyu.sparkle.model.business.ChangePasswordResponse;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.network.SparkleRequest;
import online.duoyu.sparkle.utils.NavigationManager;
import online.duoyu.sparkle.utils.SendVerifyCodeHandler;
import online.duoyu.sparkle.utils.SparkleUtils;
import online.duoyu.sparkle.widget.StatefulButton;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.functions.Func1;
import rx.internal.util.ActionSubscriber;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by littlekey on 1/27/17.
 */

public class ForgotPasswordFragment extends BaseFragment {

  private CharSequence mEmail;
  private CharSequence mPassword;
  private CharSequence mVerifyCode;
  private SendVerifyCodeHandler mHandler;

  public static ForgotPasswordFragment newInstance() {
    Bundle args = new Bundle();
    ForgotPasswordFragment fragment = new ForgotPasswordFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_forgot_password, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    final StatefulButton btn_send_verify_code = (StatefulButton) view.findViewById(R.id.btn_send_verify_code);
    EditText verify_code_view = (EditText) view.findViewById(R.id.input_verify_code);
    final EditText input_password = (EditText) view.findViewById(R.id.input_password);
    EditText input_email = (EditText) view.findViewById(R.id.input_email);
    final StatefulButton btn_forgot_password = (StatefulButton) view.findViewById(R.id.btn_forgot_password);
    mHandler = new SendVerifyCodeHandler(btn_send_verify_code);
    Observable.just(input_email, input_password, verify_code_view)
        .flatMap(new Func1<EditText, Observable<Boolean>>() {
          @Override
          public Observable<Boolean> call(final EditText editText) {
            return RxTextView.textChanges(editText)
                .compose(ForgotPasswordFragment.this.<CharSequence>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<CharSequence, Boolean>() {
                  @Override
                  public Boolean call(CharSequence charSequence) {
                    switch (editText.getId()) {
                      case R.id.input_email:
                        mEmail = charSequence;
                        break;
                      case R.id.input_password:
                        mPassword = charSequence;
                        break;
                      case R.id.input_verify_code:
                        mVerifyCode = charSequence;
                        break;
                    }
                    return !TextUtils.isEmpty(mEmail)
                        && !TextUtils.isEmpty(mPassword)
                        && !TextUtils.isEmpty(mVerifyCode);
                  }
                });
          }
        })
        .subscribe(new Action1<Boolean>() {
          @Override
          public void call(Boolean aBoolean) {
            btn_forgot_password.setEnabled(aBoolean);
            btn_forgot_password.setState(aBoolean ? StatefulButton.STATE_DONE : StatefulButton.STATE_CANCELED);
          }
        });
    RxView.clicks(btn_forgot_password)
        .compose(this.<Void>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Void>() {
          @Override
          public void call(Void aVoid) {
            RequestFuture<ChangePasswordResponse> future = RequestFuture.newFuture();
            ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest.Builder()
                .email(mEmail.toString())
                .email_verification_code(mVerifyCode.toString())
                .password(SparkleUtils.getPrehashedPassword(mPassword.toString()))
                .build();
            SparkleRequest<ChangePasswordResponse> request = SparkleApplication.getInstance().getRequestManager()
                .newSparkleRequest(ApiType.CHANGE_PASSWORD,
                    ByteString.of(ChangePasswordRequest.ADAPTER.encode(changePasswordRequest)),
                    ChangePasswordResponse.class, future, future);
            request.setTag(ForgotPasswordFragment.this);
            request.submit();
            Observable.from(future, Schedulers.newThread())
                .compose(ForgotPasswordFragment.this.<ChangePasswordResponse>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new ActionSubscriber<>(new Action1<ChangePasswordResponse>() {
                  @Override
                  public void call(ChangePasswordResponse changePasswordResponse) {
                    if (Wire.get(changePasswordResponse.success, false)) {
                      NavigationManager.navigationTo(getActivity(), LoginActivity.class,
                          Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    } else {
                      Timber.e("change password error");
                    }
                  }
                }, new Action1<Throwable>() {
                  @Override
                  public void call(Throwable throwable) {
                    Timber.e(throwable, "change password error");
                  }
                }, Actions.empty()));
          }
        });
    RxView.touches(input_password, new Func1<MotionEvent, Boolean>() {
      @Override
      public Boolean call(MotionEvent motionEvent) {
        return motionEvent.getAction() == MotionEvent.ACTION_UP
            && motionEvent.getRawX() >= input_password.getRight() - input_password.getTotalPaddingRight();
      }
    })
        .compose(this.<MotionEvent>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .map(new Func1<MotionEvent, Boolean>() {
          @Override
          public Boolean call(MotionEvent motionEvent) {
            return input_password.getInputType() == EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
          }
        })
        .subscribe(new Action1<Boolean>() {
          @Override
          public void call(Boolean visible) {
            Typeface tf = input_password.getTypeface();
            input_password.setInputType(visible ?
                EditorInfo.TYPE_CLASS_TEXT|EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
                : EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            input_password.setTypeface(tf);
          }
        });
    RxView.clicks(btn_send_verify_code)
        .compose(this.<Void>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .observeOn(AndroidSchedulers.mainThread())
        .doOnUnsubscribe(new Action0() {
          @Override
          public void call() {
            mHandler.reset();
          }
        })
        .filter(new Func1<Void, Boolean>() {
          @Override
          public Boolean call(Void aVoid) {
            return !TextUtils.isEmpty(mEmail);
          }
        })
        .subscribe(new Action1<Void>() {
          @Override
          public void call(Void aVoid) {
            mHandler.send(mEmail.toString());
          }
        });
  }
}
