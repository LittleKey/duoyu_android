package online.duoyu.sparkle.presenter;

import me.littlekey.mvp.presenter.Presenter;
import online.duoyu.sparkle.model.Model;

/**
 * Created by littlekey on 12/24/16.
 */

public abstract class SparklePresenter extends Presenter {
  @Override
  public final void bind(Object model) {
    bind((Model) model);
  }

  public abstract void bind(Model model);
}
