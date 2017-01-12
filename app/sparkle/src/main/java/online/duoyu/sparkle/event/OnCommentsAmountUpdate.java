package online.duoyu.sparkle.event;

/**
 * Created by littlekey on 1/12/17.
 */

public class OnCommentsAmountUpdate {

  public final String diary_id;
  public final int amount;

  public OnCommentsAmountUpdate(String diary_id, int amount) {
    this.diary_id = diary_id;
    this.amount = amount;
  }
}
