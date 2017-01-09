package online.duoyu.sparkle.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;

import online.duoyu.sparkle.R;
import online.duoyu.sparkle.utils.Const;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by littlekey on 1/8/17.
 */

public class EditFragment extends BaseFragment {

  public static EditFragment newInstance(CharSequence content) {
    Bundle args = new Bundle();
    args.putCharSequence(Const.KEY_CONTENT, content);
    EditFragment fragment = new EditFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_edit, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    TextView content_view = (TextView) view.findViewById(R.id.content);
    RxTextView.textChanges(content_view)
        .compose(this.<CharSequence>bindToLifecycle())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<CharSequence>() {
          @Override
          public void call(CharSequence charSequence) {
            Intent intent = getActivity().getIntent();
            intent.putExtra(Const.KEY_CONTENT, charSequence);
          }
        });
    content_view.setText(getArguments().getCharSequence(Const.KEY_CONTENT, Const.EMPTY_CHAR_SEQUENCE));
  }
}
