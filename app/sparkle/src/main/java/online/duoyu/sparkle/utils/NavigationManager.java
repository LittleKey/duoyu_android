package online.duoyu.sparkle.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import me.littlekey.base.utils.CollectionUtils;
import me.littlekey.network.NameValuePair;

/**
 * Created by littlekey on 12/21/16.
 */

public class NavigationManager {
  public static final String SCHEME = "sparkle";

  private NavigationManager() {}

  public static void navigationTo(Activity activity, int requestCode, Class clazz) {
    navigationTo(activity, requestCode, clazz, null);
  }

  public static void navigationTo(Activity activity, int requestCode, Class clazz, Bundle bundle) {
    Intent intent = new Intent(activity, clazz);
    if (bundle != null) {
      intent.putExtras(bundle);
    }
    activity.startActivityForResult(intent, requestCode);
  }

  public static void navigationTo(Context context, Class clazz) {
    navigationTo(context, clazz, null);
  }

  public static void navigationTo(Context context, Class clazz, Bundle bundle) {
    Intent intent = new Intent(context, clazz);
    if (bundle != null) {
      intent.putExtras(bundle);
    }
    context.startActivity(intent);
  }

  public static void navigationTo(Context context, Class clazz, int flag) {
    Intent intent = new Intent(context, clazz);
    intent.setFlags(flag);
    context.startActivity(intent);
  }

  public static void navigationTo(Context context, Uri uri) {
    navigationTo(context, uri, null);
  }

  public static void navigationTo(Context context, String url, Bundle bundle) {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    if (bundle != null) {
      intent.putExtras(bundle);
    }
    context.startActivity(intent);
  }

  public static void navigationTo(Context context, Uri uri, Bundle bundle) {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri.toString()));
    if (bundle != null) {
      intent.putExtras(bundle);
    }
    context.startActivity(intent);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public static void navigationTo(Context context, Uri uri, Bundle bundle, ActivityOptions options) {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri.toString()));
    if (bundle != null) {
      intent.putExtras(bundle);
    }
    context.startActivity(intent, options.toBundle());
  }

  public static Uri buildUri(String path, NameValuePair... pairs) {
    Map<String, String> map = new HashMap<>();
    if (pairs != null) {
      for (NameValuePair pair : pairs) {
        map.put(pair.getName(), pair.getValue());
      }
    }
    return buildUri(path, map);
  }

  public static Uri buildUri(String path, Map<String, String> pairs) {
    Uri.Builder builder = new Uri.Builder()
        .scheme(SCHEME)
        .appendEncodedPath(path);
    if (pairs != null && pairs.size() > 0) {
      for (Map.Entry<String, String> entry : pairs.entrySet()) {
        builder.appendQueryParameter(entry.getKey(), entry.getValue());
      }
    }
    return builder.build();
  }

  public static Bundle parseIntent(Intent intent) {
    Uri uri = intent.getData();
    Set<String> names = null;
    ArrayList<String> paths = null;
    if (uri != null) {
      names = uri.getQueryParameterNames();
      paths = new ArrayList<>(uri.getPathSegments());
    }
    Bundle bundle = intent.getExtras();
    if (bundle == null) {
      bundle = new Bundle();
    }
    if (!CollectionUtils.isEmpty(names)) {
      ArrayList<NameValuePair> pairs = new ArrayList<>();
      for (String name : names) {
        if (name.startsWith(Const.KEY_EXTRA)) {
          bundle.putString(name, uri.getQueryParameter(name));
          continue;
        }
        pairs.add(new NameValuePair(name, uri.getQueryParameter(name)));
      }
      bundle.putSerializable(Const.KEY_API_QUERY, pairs);
    }
    if (!CollectionUtils.isEmpty(paths)) {
      bundle.putStringArrayList(Const.KEY_API_PATH, paths);
    }
    return bundle;
  }
}
