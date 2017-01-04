package online.duoyu.sparkle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;

import de.greenrobot.event.EventBus;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.event.OnEditCorrectTextChangeEvent;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.utils.Const;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by littlekey on 12/28/16.
 */

public class EditCorrectChildFragment extends ListFragment {

  public static EditCorrectChildFragment newInstance(Model model, String correct_content, int position) {
    Bundle args = new Bundle();
    args.putString(Const.KEY_CORRECT_CONTENT, correct_content);
    args.putInt(Const.KEY_API_TYPE, ApiType.GET_CORRECTS_SENTENCE_BY_DIARY.ordinal());
    Bundle extra_bundle = new Bundle();
    extra_bundle.putParcelable(Const.KEY_MODEL, model);
    extra_bundle.putInt(Const.KEY_POSITION, position);
    args.putBundle(Const.KEY_EXTRA, extra_bundle);
    EditCorrectChildFragment fragment = new EditCorrectChildFragment();
    fragment.setArguments(args);
    fragment.setLazyLoad(false);
    return fragment;
  }

  @Override
  protected View lazyLoad(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
    ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_edit_correct_child, container, false);
    EditText edit_correct = (EditText) rootView.findViewById(R.id.edit_correct);
    if (getArguments() == null) {
      return rootView;
    }
    edit_correct.setText(getArguments().getString(Const.KEY_CORRECT_CONTENT, Const.EMPTY_STRING));
    RxTextView.textChanges(edit_correct)
        .compose(this.<CharSequence>bindToLifecycle())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<CharSequence>() {
          @Override
          public void call(CharSequence charSequence) {
            Bundle bundle = getArguments().getBundle(Const.KEY_EXTRA);
            if (bundle == null) {
              return;
            }
            Model model = bundle.getParcelable(Const.KEY_MODEL);
            if (model == null) {
              return;
            }
            int position = bundle.getInt(Const.KEY_POSITION, -1);
            EventBus.getDefault().post(new OnEditCorrectTextChangeEvent(
                model.identity, charSequence.toString(), position));
          }
        });
    View corrects_view = super.lazyLoad(inflater, rootView, savedInstanceState);
    ViewGroup corrects_container = (ViewGroup) rootView.findViewById(R.id.corrects_container);
    corrects_view.setLayoutParams(corrects_container.getLayoutParams());
    int index = rootView.indexOfChild(corrects_container);
    rootView.removeView(corrects_container);
    rootView.addView(corrects_view, index, corrects_container.getLayoutParams());
    return rootView;
  }
}
