package online.duoyu.sparkle.fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.RequestFuture;
import com.jakewharton.rxbinding.view.RxView;
import com.squareup.wire.Wire;

import java.util.concurrent.TimeUnit;

import me.littlekey.base.utils.FormatUtils;
import me.littlekey.mvp.presenter.ViewGroupPresenter;
import okio.ByteString;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.model.ModelFactory;
import online.duoyu.sparkle.model.business.DiaryRequest;
import online.duoyu.sparkle.model.business.DiaryResponse;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.network.SparkleRequest;
import online.duoyu.sparkle.presenter.SparklePresenterFactory;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.ToastUtils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.internal.util.ActionSubscriber;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by littlekey on 12/26/16.
 */

public class DiaryFragment extends BaseFragment {

  private ViewGroupPresenter mPresenterGroup;

  public static DiaryFragment newInstance(Model model) {
    Bundle args = new Bundle();
    args.putParcelable(Const.KEY_MODEL, model);
    DiaryFragment fragment = new DiaryFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//    lp.dimAmount = 0.0f;
//    getActivity().getWindow().setAttributes(lp);
//    getActivity().getWindow().setBackgroundDrawableResource(R.color.transparent);
    mPresenterGroup = SparklePresenterFactory.createDiaryPresenter(
        (ViewGroup) inflater.inflate(R.layout.fragment_diary, container, false));
    return mPresenterGroup.view;
  }

  @Override
  public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    view.post(new Runnable() {
      @Override
      public void run() {
        int left_margin = FormatUtils.dipsToPix(15);
        int right_margin = FormatUtils.dipsToPix(15);
        int top_margin = FormatUtils.dipsToPix(60);
        int bottom_margin = FormatUtils.dipsToPix(20);
        Point point = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(point);
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = point.x - left_margin - right_margin;
        lp.height = point.y - top_margin - bottom_margin;
        view.setLayoutParams(lp);
        view.requestLayout();
      }
    });
    final Model model = getArguments().getParcelable(Const.KEY_MODEL);
    if (model == null) {
      return;
    }
    mPresenterGroup.bind(model);
    RxView.clicks(view.findViewById(R.id.close)).cache()
        .throttleFirst(1, TimeUnit.SECONDS)
        .subscribe(new Action1<Void>() {
          @Override
          public void call(Void aVoid) {
            getActivity().finish();
          }
        });
    RequestFuture<DiaryResponse> future = RequestFuture.newFuture();
    DiaryRequest diaryRequest = new DiaryRequest.Builder()
        .diary(model.diary)
        .build();
    SparkleRequest<DiaryResponse> request = SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(ApiType.GET_DIARY_BY_ID, ByteString.of(DiaryRequest.ADAPTER.encode(diaryRequest)),
            DiaryResponse.class, future, future);
    request.setTag(this);
    request.submit();
    Observable.from(future, Schedulers.newThread())
        .compose(this.<DiaryResponse>bindToLifecycle())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ActionSubscriber<>(new Action1<DiaryResponse>() {
          @Override
          public void call(DiaryResponse diaryResponse) {
            if (Wire.get(diaryResponse.success, false)) {
              Model newModel = ModelFactory.createModelFromDiary(diaryResponse.diary, Model.Template.DATA);
              if (newModel != null) {
                mPresenterGroup.bind(newModel);
              }
            } else {
              ToastUtils.toast(R.string.get_diary_error);
            }
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "get diary by id error: " + model);
            ToastUtils.toast(R.string.get_diary_error);
          }
        }, Actions.empty()));
  }

  @Override
  public void onDestroyView() {
    SparkleApplication.getInstance().getRequestManager().cancel(this);
    mPresenterGroup.unbind();
    super.onDestroyView();
  }
}
