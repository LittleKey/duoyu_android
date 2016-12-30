package online.duoyu.sparkle.event;

/**
 * Created by littlekey on 1/1/17.
 */

public class OnEditCorrectTextChangeEvent {

  final public String diary_id;
  final public String content;
  final public int position;

  public OnEditCorrectTextChangeEvent(String diary_id, String content, int position) {
    this.diary_id = diary_id;
    this.content = content;
    this.position = position;
  }
}
