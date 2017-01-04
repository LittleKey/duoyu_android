package online.duoyu.sparkle.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.squareup.wire.Wire;

import de.greenrobot.event.EventBus;
import okio.ByteString;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.activity.BaseActivity;
import online.duoyu.sparkle.event.OnReplyCommentEvent;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.model.ModelFactory;
import online.duoyu.sparkle.model.business.CommentRequest;
import online.duoyu.sparkle.model.business.CommentResponse;
import online.duoyu.sparkle.model.proto.Comment;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.network.SparkleRequest;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.SparkleUtils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.internal.util.ActionSubscriber;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by littlekey on 1/4/17.
 */

public class CommentsFragment extends ListFragment {

  private Model mModel;
  private Model mQuote;
  private boolean mHasQuote;
  private EditText mEditCommentView;

  public static CommentsFragment newInstance(Bundle bundle) {
    CommentsFragment fragment = new CommentsFragment();
    fragment.setLazyLoad(false);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    EventBus.getDefault().register(this);
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  protected View lazyLoad(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
    ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_comments, container, false);
    mEditCommentView = (EditText) rootView.findViewById(R.id.edit_comment);
    final View btn_send = rootView.findViewById(R.id.btn_send);
    Bundle bundle = getArguments().getBundle(Const.KEY_EXTRA);
    if (bundle != null) {
      mModel = bundle.getParcelable(Const.KEY_MODEL);
    }
    if (mModel == null) {
      return rootView;
    }
    RxTextView.textChanges(mEditCommentView)
        .compose(this.<CharSequence>bindToLifecycle())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<CharSequence>() {
          @Override
          public void call(CharSequence charSequence) {
            mHasQuote = mQuote != null &&
                charSequence.toString().startsWith("@" + mQuote.user.nickname + " ");
          }
        });
    RxView.clicks(btn_send)
        .compose(this.<Void>bindToLifecycle())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Void>() {
          @Override
          public void call(Void aVoid) {
            String content = mEditCommentView.getText().toString();
            if (TextUtils.isEmpty(content)) {
              return;
            }
            btn_send.setEnabled(false);
            // TODO : popup progress dialog
            Comment.Builder commentBuilder = new Comment.Builder()
                .entire_id(mModel.identity)
                .content(content);
            if (mHasQuote && mQuote != null) {
              commentBuilder.quote_author(mQuote.user)
                  .quote(mQuote.description)
                  .quote_id(mQuote.identity)
                  .content(content.substring(("@" + mQuote.user.nickname + " ").length()));
            }
            RequestFuture<CommentResponse> future = RequestFuture.newFuture();
            final CommentRequest commentRequest = new CommentRequest.Builder()
                .comment(commentBuilder.build())
                .build();
            SparkleRequest<CommentResponse> request = SparkleApplication.getInstance().getRequestManager()
                .newSparkleRequest(ApiType.COMMENT, Request.Method.PUT,
                    ByteString.of(CommentRequest.ADAPTER.encode(commentRequest)),
                    CommentResponse.class, future, future);
            request.setTag(CommentsFragment.this);
            request.submit();
            Observable.from(future, Schedulers.newThread())
                .compose(CommentsFragment.this.<CommentResponse>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ActionSubscriber<>(new Action1<CommentResponse>() {
                  @Override
                  public void call(CommentResponse commentResponse) {
                    if (Wire.get(commentResponse.success, false)) {
                      Model model = ModelFactory.createModelFromComment(
                          commentResponse.comment, Model.Template.ITEM_COMMENT);
                      if (model != null) {
                        insertItem(2, model);
                        Model divider_model = getDataSet().get(1);
                        changeItem(1, divider_model.newBuilder()
                            .description(SparkleUtils
                                .formatString(R.string.all_comments, divider_model.count.comments + 1))
                            .count(divider_model.count.newBuilder()
                                .comments(divider_model.count.comments + 1)
                                .build())
                            .build());
                        mEditCommentView.setText("");
                        ((BaseActivity) getActivity()).closeKeyboard();
                      }
                    } else {
                      Timber.d("send comment error");
                    }
                    btn_send.setEnabled(true);
                  }
                }, new Action1<Throwable>() {
                  @Override
                  public void call(Throwable throwable) {
                    Timber.e(throwable, "send comment error");
                    btn_send.setEnabled(true);
                  }
                }, Actions.empty()));
          }
        });
    View comments_view = super.lazyLoad(inflater, container, savedInstanceState);
    ViewGroup comments_container = (ViewGroup) rootView.findViewById(R.id.comments_container);
    int index = rootView.indexOfChild(comments_container);
    rootView.removeView(comments_container);
    rootView.addView(comments_view, index, comments_container.getLayoutParams());
    return rootView;
  }

  @Override
  public void onDestroyView() {
    EventBus.getDefault().unregister(this);
    SparkleApplication.getInstance().getRequestManager().cancel(this);
    super.onDestroyView();
  }

  public void onEventMainThread(OnReplyCommentEvent event) {
    if (TextUtils.equals(mModel.identity, event.model.comment.entire_id)) {
      mQuote = event.model;
      String content = "@" + mQuote.user.nickname + " " + mEditCommentView.getText().toString();
      mEditCommentView.setText("");
      mEditCommentView.append(content);
      mEditCommentView.requestFocus();
      InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.showSoftInput(mEditCommentView, InputMethodManager.SHOW_IMPLICIT);
    }
  }
}
