package online.duoyu.sparkle.utils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by littlekey on 12/24/16.
 */

public class CollectionUtils {

  private CollectionUtils() {}

  public static <T> boolean isEmpty(Collection<T> collection) {
    return collection == null || collection.isEmpty();
  }

  public static <T> void add(Collection<T> collection, T item) {
    if (item != null) {
      collection.add(item);
    }
  }

  public static <T> void addAll(Collection<T> collection, Collection<T> items) {
    if (items != null) {
      collection.addAll(items);
    }
  }

  public static <T> Collection<T> filter(Collection<T> collection, me.littlekey.base.utils.CollectionUtils.FilterCallback<T> callback){
    Collection<T> newCol = new ArrayList<>();
    if (callback != null) {
      for (T item : collection) {
        if (callback.onFilter(item)) {
          add(newCol, item);
        }
      }
    }
    return newCol;
  }

  public interface FilterCallback<T> {
    boolean onFilter(T item);
  }
}
