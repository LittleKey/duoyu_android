package online.duoyu.sparkle.model;

import java.util.List;

import me.littlekey.network.NameValuePair;
import online.duoyu.sparkle.model.data.SparkleDataGenerator;
import online.duoyu.sparkle.network.ApiType;

/**
 * Created by littlekey on 12/19/16.
 */

public class DataGeneratorFactory {
  private DataGeneratorFactory() {}

  public static SparkleDataGenerator<?>
  createDataGenerator(ApiType apiType, List<String> paths, NameValuePair... pairs) {
    switch (apiType) {
      default:
        throw new IllegalStateException("Unknown api type:" + apiType.name());
    }
  }
}
