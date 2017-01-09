package online.duoyu.sparkle.event;

/**
 * Created by littlekey on 1/8/17.
 */

public class OnEditContentEvent {

  public final CharSequence content;

  public OnEditContentEvent(CharSequence content) {
    this.content = content;
  }
}
