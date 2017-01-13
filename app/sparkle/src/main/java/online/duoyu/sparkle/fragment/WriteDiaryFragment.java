package online.duoyu.sparkle.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.squareup.wire.Wire;
import com.trello.rxlifecycle.android.FragmentEvent;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import okio.ByteString;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.activity.EditActivity;
import online.duoyu.sparkle.dialog.EditTitleDialog;
import online.duoyu.sparkle.event.OnEditContentEvent;
import online.duoyu.sparkle.event.OnEditTitleEvent;
import online.duoyu.sparkle.model.business.DiaryRequest;
import online.duoyu.sparkle.model.business.DiaryResponse;
import online.duoyu.sparkle.model.proto.Diary;
import online.duoyu.sparkle.model.proto.Language;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.network.SparkleRequest;
import online.duoyu.sparkle.utils.Colorful;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.NavigationManager;
import online.duoyu.sparkle.utils.SparkleUtils;
import online.duoyu.sparkle.widget.StatefulButton;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.MainThreadSubscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.functions.Func1;
import rx.internal.util.ActionSubscriber;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static rx.android.MainThreadSubscription.verifyMainThread;

/**
 * Created by littlekey on 12/20/16.
 */

public class WriteDiaryFragment extends LazyLoadFragment {

  private CharSequence mTitle;
  private CharSequence mContent;
  private TextView mTitleView;
  private TextView mContentView;
  private StatefulButton mBtnPublish;
  private DateTime mDateTime;
  private TextView mMonthView;
  private TextView mDayView;
  private TextView mWeekView;

  public static WriteDiaryFragment newInstance() {
    Bundle args = new Bundle();
    WriteDiaryFragment fragment = new WriteDiaryFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  @SuppressWarnings("RtlHardCoded")
  protected View lazyLoad(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_write_diary, container, false);
    mTitleView = (TextView) view.findViewById(R.id.input_title);
    mContentView = (TextView) view.findViewById(R.id.input_content);
    mBtnPublish = (StatefulButton) view.findViewById(R.id.theme_btn_publish);
    mBtnPublish.setState(StatefulButton.STATE_CANCELED);
    mMonthView = (TextView) view.findViewById(R.id.month);
    mDayView = (TextView) view.findViewById(R.id.day);
    mWeekView = (TextView) view.findViewById(R.id.week);

    mDateTime = DateTime.now();
    setDateTime(mDateTime);
    RxView.clicks(view.findViewById(R.id.theme_date_background))
        .compose(this.<Void>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .observeOn(AndroidSchedulers.mainThread())
        .flatMap(new Func1<Void, Observable<DateTime>>() {
          @Override
          public Observable<DateTime> call(Void aVoid) {
            return Observable.create(new Observable.OnSubscribe<DateTime>() {
              @Override
              public void call(final Subscriber<? super DateTime> subscriber) {
                // NOTE : do something with magic number
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                  @Override
                  public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    verifyMainThread();
                    if (!subscriber.isUnsubscribed()) {
                      subscriber.onNext(new DateTime(year, month + 1, dayOfMonth,
                          mDateTime.getHourOfDay(), mDateTime.getMinuteOfHour()));
                    }
                  }
                };
                DatePickerDialog datePickerDialog =
                    new DatePickerDialog(getActivity(), Colorful.getThemeDelegate().getDatePickerDialogStyle(),
                        listener, mDateTime.getYear(), mDateTime.getMonthOfYear() - 1, mDateTime.getDayOfMonth());
                datePickerDialog.show();
                subscriber.onNext(mDateTime);
              }
            });
          }
        })
        .subscribe(new Action1<DateTime>() {
          @Override
          public void call(DateTime dateTime) {
            setDateTime(dateTime);
          }
        });
    Observable.just(mTitleView, mContentView)
        .flatMap(new Func1<TextView, Observable<Boolean>>() {
          @Override
          public Observable<Boolean> call(final TextView textView) {
            return RxTextView.textChanges(textView)
                .compose(WriteDiaryFragment.this.<CharSequence>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<CharSequence, Boolean>() {
                  @Override
                  public Boolean call(CharSequence charSequence) {
                    if (TextUtils.isEmpty(charSequence)) {
                      textView.setGravity(Gravity.CENTER);
                    }
                    switch (textView.getId()) {
                      case R.id.input_title:
                        mTitle = charSequence;
                        if (!TextUtils.isEmpty(charSequence)) {
                          textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                        }
                        break;
                      case R.id.input_content:
                        mContent = charSequence;
                        if (!TextUtils.isEmpty(charSequence)) {
                          textView.setGravity(Gravity.LEFT | Gravity.TOP);
                        }
                        break;
                    }
                    return !TextUtils.isEmpty(mTitle) && !TextUtils.isEmpty(mContent);
                  }
                });
          }
        })
        .compose(this.<Boolean>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .subscribe(new Action1<Boolean>() {
          @Override
          public void call(Boolean aBoolean) {
            mBtnPublish.setEnabled(aBoolean);
            mBtnPublish.setState(aBoolean ? StatefulButton.STATE_DONE : StatefulButton.STATE_CANCELED);
          }
        });
    RxView.clicks(mTitleView)
        .compose(this.<Void>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Void>() {
          @Override
          public void call(Void aVoid) {
            new EditTitleDialog(getActivity(), mTitle).show();
          }
        });
    RxView.clicks(mContentView)
        .compose(this.<Void>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Void>() {
          @Override
          public void call(Void aVoid) {
            Bundle bundle = new Bundle();
            bundle.putCharSequence(Const.KEY_CONTENT, mContent);
            NavigationManager.navigationTo(
                getActivity(), Const.REQUEST_CODE_EDIT, EditActivity.class, bundle);
          }
        });
    RxView.clicks(mBtnPublish)
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .compose(this.<Void>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Void>() {
          @Override
          public void call(Void aVoid) {
            RequestFuture<DiaryResponse> future = RequestFuture.newFuture();
            final DiaryRequest diaryRequest = new DiaryRequest.Builder()
                .diary(new Diary.Builder()
                    .diary_date(mDateTime.getMillis())
                    .title(mTitle.toString())
                    .content(mContent.toString())
                    .language(Language.ENGLISH)
                    .build())
                .build();
            SparkleRequest<DiaryResponse> request = SparkleApplication.getInstance().getRequestManager()
                .newSparkleRequest(ApiType.DIARY, Request.Method.PUT,
                    ByteString.of(DiaryRequest.ADAPTER.encode(diaryRequest)),
                    DiaryResponse.class, future, future);
            request.setTag(WriteDiaryFragment.this);
            request.submit();
            Observable.from(future, Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ActionSubscriber<>(new Action1<DiaryResponse>() {
                  @Override
                  public void call(DiaryResponse diaryResponse) {
                    if (!Wire.get(diaryResponse.success, false)) {
                      Timber.e("error on publish diary: %s", diaryResponse.errno.name());
                      return;
                    }
                    mContentView.setText(Const.EMPTY_CHAR_SEQUENCE);
                    mTitleView.setText(Const.EMPTY_CHAR_SEQUENCE);
                  }
                }, new Action1<Throwable>() {
                  @Override
                  public void call(Throwable throwable) {
                    Timber.e(throwable, "error on publish diary.");
                  }
                }, Actions.empty()));
          }
        });
    EventBus.getDefault().register(this);
    return view;
  }

  public void onEventMainThread(OnEditContentEvent event) {
    mContentView.setText(event.content);
  }

  public void onEventMainThread(OnEditTitleEvent event) {
    mTitleView.setText(event.title);
  }

  @Override
  public void onDestroyView() {
    EventBus.getDefault().unregister(this);
    super.onDestroyView();
  }

  private void setDateTime(DateTime dateTime) {
    String month = dateTime.monthOfYear().getAsShortText();
    String week = dateTime.dayOfWeek().getAsShortText();
    String day = SparkleUtils.formatString("%02d", dateTime.getDayOfMonth());
    mMonthView.setText(month);
    mDayView.setText(day);
    mWeekView.setText(week);
    mDateTime = dateTime;
  }
}
