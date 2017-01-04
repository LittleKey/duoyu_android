package online.duoyu.sparkle.event;

import online.duoyu.sparkle.model.Model;

/**
 * Created by littlekey on 1/4/17.
 */

public class OnReplyCommentEvent {

  public final Model model;

  public OnReplyCommentEvent(Model model) {
    this.model =model;
  }
}
