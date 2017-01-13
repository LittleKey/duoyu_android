package online.duoyu.sparkle.event;

import online.duoyu.sparkle.network.ApiType;

/**
 * Created by littlekey on 1/12/17.
 */

public class OnCorrectsAmountUpdateEvent {

  public final ApiType apiType;
  public final String diary_id;
  public final int amount;

  public OnCorrectsAmountUpdateEvent(String diary_id, int amount) {
    this.diary_id = diary_id;
    this.amount = amount;
    this.apiType = null;
  }

  public OnCorrectsAmountUpdateEvent(ApiType apiType, int amount) {
    this.apiType = apiType;
    this.amount = amount;
    this.diary_id = null;
  }
}
