package online.duoyu.sparkle.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.event.OnEditTitleEvent;
import online.duoyu.sparkle.utils.Colorful;
import online.duoyu.sparkle.widget.StatefulButton;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by littlekey on 1/8/17.
 */

public class EditTitleDialog extends Dialog {

  private CharSequence mTitle;

  public EditTitleDialog(Context context, CharSequence title) {
    super(context, Colorful.getThemeDelegate().getDialogStyle());
    setContentView(R.layout.dialog_edit_title);
    EditText input_title_view = (EditText) findViewById(R.id.input_title);
    StatefulButton btn_sure = (StatefulButton) findViewById(R.id.theme_btn_sure);
    btn_sure.setState(StatefulButton.STATE_DONE);
    RxTextView.textChanges(input_title_view)
        .compose(RxLifecycleAndroid.<CharSequence>bindView(input_title_view))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<CharSequence>() {
          @Override
          public void call(CharSequence charSequence) {
            mTitle = charSequence;
          }
        });
    input_title_view.setText(title);
    RxView.clicks(btn_sure)
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .compose(RxLifecycleAndroid.<Void>bindView(btn_sure))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Void>() {
          @Override
          public void call(Void aVoid) {
            EventBus.getDefault().post(new OnEditTitleEvent(mTitle));
            dismiss();
          }
        });
  }
}
