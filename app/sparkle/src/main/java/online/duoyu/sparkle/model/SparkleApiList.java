package online.duoyu.sparkle.model;

import android.support.annotation.Nullable;

import me.littlekey.mvp.PageList;
import me.littlekey.network.ApiRequest;
import me.littlekey.network.NameValuePair;
import online.duoyu.sparkle.model.data.SparkleDataGenerator;
import online.duoyu.sparkle.model.proto.Model;

/**
 * Created by littlekey on 12/20/16.
 */

public class SparkleApiList<T> extends PageList<T, Model> {
  private final SparkleDataGenerator<T> mDataGenerator;
  private String mCurrentUrl;

  public SparkleApiList(SparkleDataGenerator<T> dataGenerator) {
    super(dataGenerator);
    mDataGenerator = dataGenerator;
  }

  @Override
  protected void onRequestCreated(ApiRequest<T> request, boolean clearData) {
    super.onRequestCreated(request, clearData);
    mDataGenerator.onRequestCreated(request, clearData);
    mCurrentUrl = request.getUrl();
  }

  public void refresh(NameValuePair... pairs) {
    mDataGenerator.resetPairs(pairs);
  }

  public @Nullable
  String getCurrentUrl() {
    return mCurrentUrl;
  }
}
