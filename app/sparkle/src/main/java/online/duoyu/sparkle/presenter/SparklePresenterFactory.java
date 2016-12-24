package online.duoyu.sparkle.presenter;

import android.view.ViewGroup;

import me.littlekey.mvp.presenter.ViewGroupPresenter;
import online.duoyu.sparkle.R;

/**
 * Created by littlekey on 12/24/16.
 */

public class SparklePresenterFactory {

  private SparklePresenterFactory() {}

  public static ViewGroupPresenter createEmptyPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(new ActionPresenter());
  }

  public static ViewGroupPresenter createDiaryItemPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(R.id.title, new TextPresenter())
        .add(R.id.day, new TextPresenter())
        .add(R.id.week, new TextPresenter())
        .add(R.id.time, new TextPresenter())
        .add(R.id.nickname, new TextPresenter())
        .add(R.id.content, new TextPresenter())
        .add(R.id.avatar, new ImagePresenter())
        .add(0, new ListenActionPresenter())
        .add(0, new ActionPresenter());
  }

  public static ViewGroupPresenter createMonthItemPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(R.id.month, new TextPresenter())
        .add(new ListenActionPresenter());
  }
}
