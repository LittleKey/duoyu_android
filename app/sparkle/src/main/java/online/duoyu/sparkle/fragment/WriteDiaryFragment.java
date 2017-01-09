package online.duoyu.sparkle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.trello.rxlifecycle.android.FragmentEvent;

import de.greenrobot.event.EventBus;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.activity.EditActivity;
import online.duoyu.sparkle.event.OnEditContentEvent;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.NavigationManager;
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
    mBtnPublish = (StatefulButton) view.findViewById(R.id.theme_btn_publish);
    mBtnPublish.setState(StatefulButton.STATE_CANCELED);
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
                    switch (textView.getId()) {
                      case R.id.input_title:
                        mTitle = charSequence;
                        break;
                      case R.id.input_content:
                        mContent = charSequence;
                        break;
                    }
                    if (TextUtils.isEmpty(charSequence)) {
                      textView.setGravity(Gravity.CENTER);
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
            // TODO : start edit diary title activity
          }
        });
    RxView.clicks(mContentView)
        .compose(this.<Void>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Void>() {
          @Override
          public void call(Void aVoid) {
            Bundle bundle = new Bundle();
            bundle.putCharSequence(Const.KEY_CONTENT, mContentView.getText());
            NavigationManager.navigationTo(getActivity(),
                EditActivity.class, Const.REQUEST_CODE_EDIT, bundle);
          }
        });
    RxView.clicks(mBtnPublish)
        .compose(this.<Void>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Void>() {
          @Override
          public void call(Void aVoid) {
            // TODO : publish diary
          }
        });
    EventBus.getDefault().register(this);
    return view;
  }

  public void onEventMainThread(OnEditContentEvent event) {
    mContentView.setText(event.content);
  }

  @Override
  public void onDestroyView() {
    EventBus.getDefault().unregister(this);
    super.onDestroyView();
  }
}
