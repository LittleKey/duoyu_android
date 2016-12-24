package online.duoyu.sparkle.utils;

import java.util.Random;

/**
 * Created by littlekey on 12/24/16.
 */

public class RandomUtils {
  private static Random mRandom;
  private RandomUtils() {}

  public static Random getRandom() {
    if (mRandom == null) {
      mRandom = new Random(System.currentTimeMillis());
    }
    return mRandom;
  }
}
