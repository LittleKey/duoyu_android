package online.duoyu.sparkle.utils;

import android.os.Handler;
import android.os.Message;

import com.android.volley.toolbox.RequestFuture;
import com.squareup.wire.Wire;

import java.lang.ref.WeakReference;

import okio.ByteString;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.model.business.EmailVerifyRequest;
import online.duoyu.sparkle.model.business.EmailVerifyResponse;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.network.SparkleRequest;
import online.duoyu.sparkle.widget.StatefulButton;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.internal.util.ActionSubscriber;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by littlekey on 1/27/17.
 */

public class SendVerifyCodeHandler extends Handler {

  private final static int SEND_VERIFY_CODE = 1;
  private final static int CONTINUE_CD = 2;
  private final static int CD = 60; // SEC

  private WeakReference<StatefulButton> mWeakRefView;
  private boolean mInCD = false;
  private final CompositeSubscription mSubscription;

  public SendVerifyCodeHandler(StatefulButton textView) {
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

  public void send(String email) {
    sendMessage(obtainMessage(SEND_VERIFY_CODE, email));
  }

  public void reset() {
    mSubscription.unsubscribe();
    mInCD = false;
    removeMessages(CONTINUE_CD);
    StatefulButton view = mWeakRefView.get();
    if (view != null) {
      view.setState(StatefulButton.STATE_CAN_SEND_VERIFY_CODE);
    }
  }
}
