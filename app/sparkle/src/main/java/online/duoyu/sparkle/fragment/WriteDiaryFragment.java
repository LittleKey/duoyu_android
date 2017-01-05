package online.duoyu.sparkle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import online.duoyu.sparkle.R;
import online.duoyu.sparkle.widget.StatefulButton;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by littlekey on 12/20/16.
 */

public class WriteDiaryFragment extends LazyLoadFragment {

  private CharSequence mTitle;
  private CharSequence mContent;
  private TextView mTitleView;
  private TextView mContentView;
  private StatefulButton mBtnPublish;

  public static WriteDiaryFragment newInstance() {
    Bundle args = new Bundle();
    WriteDiaryFragment fragment = new WriteDiaryFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  protected View lazyLoad(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_write_diary, container, false);
    mTitleView = (TextView) view.findViewById(R.id.input_title);
    mContentView = (TextView) view.findViewById(R.id.input_content);
    mBtnPublish = (StatefulButton) view.findViewById(R.id.btn_publish);
    mBtnPublish.setState(StatefulButton.STATE_CANCELED);
    Observable.just(mTitleView, mContentView)
        .flatMap(new Func1<TextView, Observable<Boolean>>() {
          @Override
          public Observable<Boolean> call(final TextView textView) {
            return RxTextView.textChanges(textView)
                .compose(WriteDiaryFragment.this.<CharSequence>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<CharSequence, Boolean>() {
                  @Override
                  public Boolean call(CharSequence charSequence) {
                    switch (textView.getId()) {
                      case R.id.input_title:
                        mTitle = charSequence;
                        break;
                      case R.id.input_content:
                        mContent = charSequence;
                        break;
                    }
                    return !TextUtils.isEmpty(mTitle) && !TextUtils.isEmpty(mContent);
                  }
                });
          }
        })
        .compose(this.<Boolean>bindToLifecycle())
        .subscribe(new Action1<Boolean>() {
          @Override
          public void call(Boolean aBoolean) {
            mBtnPublish.setEnabled(aBoolean);
            mBtnPublish.setState(aBoolean ? StatefulButton.STATE_DONE : StatefulButton.STATE_CANCELED);
          }
        });
    RxView.clicks(mTitleView)
        .compose(this.<Void>bindToLifecycle())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Void>() {
          @Override
          public void call(Void aVoid) {

          }
        });
    RxView.clicks(mContentView)
        .compose(this.<Void>bindToLifecycle())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Void>() {
          @Override
          public void call(Void aVoid) {

          }
        });
    return view;
  }
}
