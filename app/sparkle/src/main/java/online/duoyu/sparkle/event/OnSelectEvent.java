package online.duoyu.sparkle.event;

import online.duoyu.sparkle.model.Model;

/**
 * Created by littlekey on 12/25/16.
 */

public class OnSelectEvent {
  private boolean select;
  private Model model;

  public OnSelectEvent(boolean select, Model model) {
    this.select = select;
    this.model = model;
  }

  public boolean isSelected() {
    return this.select;
  }

  public Model getModel() {
    return model;
  }
}
