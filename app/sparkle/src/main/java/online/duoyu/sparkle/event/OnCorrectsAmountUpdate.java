package online.duoyu.sparkle.event;

/**
 * Created by littlekey on 1/12/17.
 */

public class OnCorrectsAmountUpdate {

  public final String diary_id;
  public final int amount;

  public OnCorrectsAmountUpdate(String diary_id, int amount) {
    this.diary_id = diary_id;
    this.amount = amount;
  }
}
