package online.duoyu.sparkle.event;

/**
 * Created by littlekey on 1/10/17.
 */

public class OnEditTitleEvent {

  public final CharSequence title;

  public OnEditTitleEvent(CharSequence title) {
    this.title = title;
  }
}
