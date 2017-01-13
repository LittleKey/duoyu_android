package online.duoyu.sparkle.event;

import online.duoyu.sparkle.network.ApiType;

/**
 * Created by littlekey on 1/14/17.
 */

public class OnDiariesAmountUpdateEvent {

  public final ApiType apiType;
  public final int amount;

  public OnDiariesAmountUpdateEvent(ApiType apiType, int amount) {
    this.apiType = apiType;
    this.amount = amount;
  }
}
