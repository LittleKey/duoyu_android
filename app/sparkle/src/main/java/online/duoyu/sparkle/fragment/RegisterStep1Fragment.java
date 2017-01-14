package online.duoyu.sparkle.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okio.ByteString;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.activity.LoginActivity;
import online.duoyu.sparkle.model.business.EmailVerifyRequest;
import online.duoyu.sparkle.model.business.EmailVerifyResponse;
import online.duoyu.sparkle.model.business.RegisterRequest;
import online.duoyu.sparkle.model.business.RegisterResponse;
import online.duoyu.sparkle.model.proto.Language;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.network.SparkleRequest;
import online.duoyu.sparkle.utils.NavigationManager;
import online.duoyu.sparkle.utils.SparkleUtils;
import online.duoyu.sparkle.utils.ToastUtils;
import online.duoyu.sparkle.widget.StatefulButton;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.functions.Func1;
import rx.internal.util.ActionSubscriber;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by littlekey on 12/24/16.
 */

public class RegisterStep1Fragment extends BaseFragment {

  private CharSequence mEmail;
  private CharSequence mPassword;
  private CharSequence mVerifyCode;
  private SendVerifyCodeHandler mHandler;

  public static RegisterStep1Fragment newInstance() {
    Bundle args = new Bundle();
    RegisterStep1Fragment fragment = new RegisterStep1Fragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_register, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    final StatefulButton btn_register = (StatefulButton) view.findViewById(R.id.btn_register);
    StatefulButton btn_send = (StatefulButton) view.findViewById(R.id.btn_send);
    EditText input_email = (EditText) view.findViewById(R.id.input_email);
    final EditText input_password = (EditText) view.findViewById(R.id.input_password);
    final EditText verify_code_view = (EditText) view.findViewById(R.id.input_verify_code);
    mHandler = new SendVerifyCodeHandler(btn_send);
    Observable.just(input_email, input_password, verify_code_view)
        .flatMap(new Func1<EditText, Observable<Boolean>>() {
          @Override
          public Observable<Boolean> call(final EditText editText) {
            return RxTextView.textChanges(editText)
                .compose(RegisterStep1Fragment.this.<CharSequence>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
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
            btn_register.setEnabled(aBoolean);
            btn_register.setState(aBoolean ? StatefulButton.STATE_DONE : StatefulButton.STATE_CANCELED);
          }
        });
    RxView.clicks(btn_register)
        .compose(this.<Void>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Void>() {
          @Override
          public void call(Void aVoid) {
            List<Language> native_languages = new ArrayList<>();
            native_languages.add(Language.CHINESE);
            List<Language> target_languages = new ArrayList<>();
            target_languages.add(Language.JAPANESE);
            RequestFuture<RegisterResponse> future = RequestFuture.newFuture();
            RegisterRequest registerRequest = new RegisterRequest.Builder()
                .email(mEmail.toString())
                .password(SparkleUtils.getPrehashedPassword(mPassword.toString()))
                .email_verification_code(mVerifyCode.toString())
                .nickname("test1")
                .avatar("http://avatar.com")
                .native_languages(native_languages)
                .target_languages(target_languages)
                .build();
            SparkleRequest<RegisterResponse> request = SparkleApplication.getInstance().getRequestManager()
                .newSparkleRequest(ApiType.REGISTER,
                    ByteString.of(RegisterRequest.ADAPTER.encode(registerRequest)),
                    RegisterResponse.class, future, future);
            request.setTag(RegisterStep1Fragment.this);
            request.submit();
            Observable.from(future, Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ActionSubscriber<>(new Action1<RegisterResponse>() {
                  @Override
                  public void call(RegisterResponse registerResponse) {
                    if (Wire.get(registerResponse.success, false)) {
                      NavigationManager.navigationTo(getActivity(), LoginActivity.class);
                      getActivity().finish();
                    } else {
                      Timber.e("register error");
                    }
                  }
                }, new Action1<Throwable>() {
                  @Override
                  public void call(Throwable throwable) {
                    Timber.e(throwable, "register error");
                  }
                }, Actions.empty()));
          }
        });
    RxView.touches(input_password, new Func1<MotionEvent, Boolean>() {
      @Override
      public Boolean call(MotionEvent motionEvent) {
        return motionEvent.getAction() == MotionEvent.ACTION_UP
            && motionEvent.getRawX() >= verify_code_view.getRight() - verify_code_view.getTotalPaddingRight();
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
            input_password.setInputType(visible ?
                EditorInfo.TYPE_CLASS_TEXT|EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
                : EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
          }
        });
    RxView.clicks(btn_send)
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

  private static class SendVerifyCodeHandler extends Handler {

    private final static int SEND_VERIFY_CODE = 1;
    private final static int CONTINUE_CD = 2;
    private final static int CD = 60; // SEC

    private WeakReference<StatefulButton> mWeakRefView;
    private boolean mInCD = false;
    private final CompositeSubscription mSubscription;

    SendVerifyCodeHandler(StatefulButton textView) {
      super();
      mWeakRefView = new WeakReference<>(textView);
      mSubscription = new CompositeSubscription();
      textView.setState(StatefulButton.STATE_CAN_SEND_VERIFY_CODE);
    }

    @Override
    public void handleMessage(Message msg) {
      StatefulButton view = mWeakRefView.get();
      if (view == null) {
        return;
      }
      switch (msg.what) {
        case SEND_VERIFY_CODE:
          if (!mInCD) {
            sendEmail((String) msg.obj);
            view.setState(StatefulButton.STATE_CAN_NOT_SEND_VERIFY_CODE);
            ToastUtils.toast("send verify code");
            removeMessages(CONTINUE_CD);
            sendMessage(obtainMessage(CONTINUE_CD));
          }
          break;
        case CONTINUE_CD:
          mInCD = msg.arg1 < CD;
          if (mInCD) {
            view.setText(SparkleUtils.formatString(R.string.send_verify_code_delay, msg.arg1));
            sendMessageDelayed(obtainMessage(CONTINUE_CD, msg.arg1 + 1, 0), 1000);
          } else {
            reset();
          }
          break;
        default:
          super.handleMessage(msg);
      }
    }

    private void sendEmail(String email) {
      RequestFuture<EmailVerifyResponse> future = RequestFuture.newFuture();
      EmailVerifyRequest emailVerifyRequest = new EmailVerifyRequest.Builder()
          .email(email)
          .build();
      SparkleRequest<EmailVerifyResponse> request = SparkleApplication.getInstance().getRequestManager()
          .newSparkleRequest(ApiType.EMAIL_VERIFY,
              ByteString.of(EmailVerifyRequest.ADAPTER.encode(emailVerifyRequest)),
              EmailVerifyResponse.class, future, future);
      request.setTag(this);
      request.submit();
      mSubscription.add(Observable.from(future, Schedulers.newThread())
          .doOnUnsubscribe(new Action0() {
            @Override
            public void call() {
              SparkleApplication.getInstance().getRequestManager().cancel(SendVerifyCodeHandler.this);
            }
          })
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new ActionSubscriber<>(new Action1<EmailVerifyResponse>() {
            @Override
            public void call(EmailVerifyResponse emailVerifyResponse) {
              Timber.d(SparkleUtils.formatString("send email verify code success: %s",
                  Wire.get(emailVerifyResponse.success, false)));
            }
          }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
              Timber.e(throwable, "send email verify code error");
            }
          }, Actions.empty())));
    }

    void send(String email) {
      sendMessage(obtainMessage(SEND_VERIFY_CODE, email));
    }

    void reset() {
      mSubscription.unsubscribe();
      mInCD = false;
      removeMessages(CONTINUE_CD);
      StatefulButton view = mWeakRefView.get();
      if (view != null) {
        view.setState(StatefulButton.STATE_CAN_SEND_VERIFY_CODE);
      }
    }
  }
}
