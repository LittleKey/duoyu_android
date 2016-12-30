package online.duoyu.sparkle.presenter;

import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.utils.Colorful;

/**
 * Created by littlekey on 12/30/16.
 */

public class ThemePresenter extends SparklePresenter {

  @Override
  public void bind(Model model) {
    Colorful.setTheme(id(), view());
  }

  @Override
  public void unbind() {
    super.unbind();
  }
}
