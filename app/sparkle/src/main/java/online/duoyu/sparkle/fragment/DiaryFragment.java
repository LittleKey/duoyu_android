package online.duoyu.sparkle.fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import me.littlekey.base.utils.FormatUtils;
import me.littlekey.mvp.presenter.ViewGroupPresenter;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.presenter.SparklePresenterFactory;
import online.duoyu.sparkle.utils.Const;
import rx.functions.Action1;

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
  }
}
