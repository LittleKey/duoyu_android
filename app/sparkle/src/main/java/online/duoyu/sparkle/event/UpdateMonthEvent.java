package online.duoyu.sparkle.event;

import online.duoyu.sparkle.model.Model;

/**
 * Created by littlekey on 12/25/16.
 */

public class UpdateMonthEvent {

  public String month;

  public UpdateMonthEvent(Model model) {
    this(model.month);
  }

  public UpdateMonthEvent(String month) {
    this.month = month;
  }
}
