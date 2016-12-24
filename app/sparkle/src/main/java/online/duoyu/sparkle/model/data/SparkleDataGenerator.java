package online.duoyu.sparkle.model.data;

import android.support.annotation.NonNull;

import com.android.volley.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.littlekey.mvp.DataGenerator;
import me.littlekey.network.ApiRequest;
import me.littlekey.network.NameValuePair;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.network.ApiType;

/**
 * Created by littlekey on 12/19/16.
 */

public abstract class SparkleDataGenerator<R> implements DataGenerator<R, Model> {

  private boolean mEnableCache;
  protected ApiType mApiType;
  protected Map<String, String> mBasePairs;
  protected List<String> mPaths;
  protected ApiRequest<R> mInitRequest;
  protected Response.Listener<R> mListener;
  protected Response.ErrorListener mErrorListener;

  public SparkleDataGenerator(@NonNull ApiType apiType, NameValuePair... pairs) {
    init(apiType, pairs);
  }

  private void init(@NonNull ApiType apiType, NameValuePair... pairs) {
    this.mApiType = apiType;
    this.mBasePairs = new HashMap<>();
    if (pairs != null) {
      for (NameValuePair pair : pairs) {
        mBasePairs.put(pair.getName(), pair.getValue());
      }
    }
  }

  public void resetPairs(NameValuePair... pairs) {
    init(mApiType, pairs);
  }

  public void setEnableCache(boolean enableCache) {
    this.mEnableCache = enableCache;
  }

  protected abstract ApiRequest<R> onCreateRequest(ApiType apiType, Map<String, String> pairs);

  @Override
  public ApiRequest<R> onCreateRequest() {
    if (mInitRequest == null) {
      mInitRequest = onCreateRequest(mApiType, mBasePairs);
    }
    return mInitRequest;
  }

  public void onRequestCreated(ApiRequest<R> request, boolean clearData) {
    if (request == null) {
      return;
    }
    if ((request.getUrl() == null || request.equals(mInitRequest)) && mEnableCache) {
      if (clearData) {
        request.getCache().invalidate(request.getCacheKey(), false);
      }
      request.setShouldCache(true);
      request.setEnableMultiResponse(true);
    } else {
      request.setShouldCache(false);
    }
  }

  @Override
  public void setListener(Response.Listener<R> listener) {
    mListener = listener;
  }

  @Override
  public void setErrorListener(Response.ErrorListener errorListener) {
    mErrorListener = errorListener;
  }
}
