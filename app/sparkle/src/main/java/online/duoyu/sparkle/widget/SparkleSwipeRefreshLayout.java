package online.duoyu.sparkle.widget;

import android.content.Context;
import android.util.AttributeSet;

import me.littlekey.mvp.widget.MvpSwipeRefreshLayout;
import online.duoyu.sparkle.model.Model;

/**
 * Created by littlekey on 12/20/16.
 */

public class SparkleSwipeRefreshLayout extends MvpSwipeRefreshLayout<Model> {

  public static final int DEFAULT_CIRCLE_TARGET = 64;

  public SparkleSwipeRefreshLayout(Context context) {
    this(context, null);
  }

  public SparkleSwipeRefreshLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
}
