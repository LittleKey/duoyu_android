package online.duoyu.sparkle.utils;

/**
 * Created by littlekey on 12/20/16.
 */

public class Optional<T> {

  private T value;

  private Optional(T value) {
    this.value = value;
  }

  public static <T> Optional<T> of(T value) {
    return new Optional<>(value);
  }

  public void IfPresent(Consumer<T> consumer) {
    if (value != null) {
      consumer.accept(value);
    }
  }

  interface Consumer<T> {
    void accept(T t);
  }
}
