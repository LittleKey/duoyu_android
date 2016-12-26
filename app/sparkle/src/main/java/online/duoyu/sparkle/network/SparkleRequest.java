package online.duoyu.sparkle.network;

import android.app.PendingIntent;
import android.content.Intent;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.Wire;

import java.util.Arrays;

import me.littlekey.network.ApiContext;
import me.littlekey.network.ApiRequest;
import me.littlekey.network.RequestManager;
import okio.ByteString;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.activity.LoginActivity;
import online.duoyu.sparkle.model.proto.RPCResponse;
import timber.log.Timber;

/**
 * Created by littlekey on 12/19/16.
 */

public class SparkleRequest<T extends Message> extends ApiRequest<T> {

  private final Class<T> mClass;
  private final RequestManager.CacheConfig mCacheConfig;

  public SparkleRequest(ApiContext apiContext, int method, String url, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener, RequestManager.CacheConfig config) {
    super(apiContext, method, url, listener, errorListener);
    this.mCacheConfig = config;
    this.mClass = clazz;
  }

  @Override
  protected T parseResponse(NetworkResponse response) throws Exception {
    RPCResponse pbRPCResponse = RPCResponse.ADAPTER.decode(response.data);
    if (pbRPCResponse.success == null || !pbRPCResponse.success) {
      if (!TextUtils.isEmpty(pbRPCResponse.content.toString())) {
        Timber.d(pbRPCResponse.content.toString());
      }
      switch (pbRPCResponse.reason) {
        case UNAUTHORIZED:
          SparkleApplication.getInstance().getAccountManager().logout();
          Intent intent = new Intent(SparkleApplication.getInstance(), LoginActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
          PendingIntent pendingIntent = PendingIntent.getActivity(
              SparkleApplication.getInstance(), 0, intent, 0);
          pendingIntent.send();
          break;
      }
      throw new VolleyError();
    }
    return ProtoAdapter.get(mClass).decode(Wire.get(pbRPCResponse.content, ByteString.EMPTY));
  }

  @Override
  protected Cache.Entry parseCache(NetworkResponse response) {
    Cache.Entry entry = super.parseCache(response);
    if (entry != null && mCacheConfig.isPreferLocalConfig()) {
      long now = System.currentTimeMillis();
      entry.ttl = now + mCacheConfig.getTtl();
      entry.softTtl = now + mCacheConfig.getSoftTtl();
    }
    return entry;
  }

  @Override
  public byte[] getBody() {
    return new byte[0];
  }

  @Override
  public String getCacheKey() {
    return super.getCacheKey() + Arrays.toString(getBody()).hashCode();
  }
}
