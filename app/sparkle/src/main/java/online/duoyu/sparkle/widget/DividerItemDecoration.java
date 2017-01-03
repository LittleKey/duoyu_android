package online.duoyu.sparkle.widget;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by littlekey on 1/3/17.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

  private Drawable mDivider;
  private int mPadding;
  private boolean mDrawEndLine;
  private Rect mBounds = new Rect();

  public DividerItemDecoration(Drawable divider, int padding) {
    this(divider, padding, false);
  }

  public DividerItemDecoration(Drawable divider, int padding, boolean drawEndLine) {
    mDivider = divider;
    mPadding = padding;
    mDrawEndLine = drawEndLine;
  }

  /**
   * Sets the {@link Drawable} for this divider.
   *
   * @param drawable Drawable that should be used as a divider.
   */
  public void setDrawable(@NonNull Drawable drawable) {
    if (drawable == null) {
      throw new IllegalArgumentException("Drawable cannot be null.");
    }
    mDivider = drawable;
  }

  @Override
  public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    if (parent.getLayoutManager() == null) {
      return;
    }
    if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
      drawVertical(c, parent);
    } else {
      drawHorizontal(c, parent);
    }
  }

  @SuppressLint("NewApi")
  private void drawVertical(Canvas canvas, RecyclerView parent) {
    canvas.save();
    final int left;
    final int right;
    if (parent.getClipToPadding()) {
      left = parent.getPaddingLeft() + mPadding;
      right = parent.getWidth() - parent.getPaddingRight() - mPadding;
      canvas.clipRect(left, parent.getPaddingTop(), right,
          parent.getHeight() - parent.getPaddingBottom());
    } else {
      left = mPadding;
      right = parent.getWidth() - mPadding;
    }

    final int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      if (!mDrawEndLine && i + 1 == childCount) {
        break;
      }
      final View child = parent.getChildAt(i);
      parent.getDecoratedBoundsWithMargins(child, mBounds);
      final int bottom = mBounds.bottom + Math.round(ViewCompat.getTranslationY(child));
      final int top = bottom - mDivider.getIntrinsicHeight();
      mDivider.setBounds(left, top, right, bottom);
      mDivider.draw(canvas);
    }
    canvas.restore();
  }

  @SuppressLint("NewApi")
  private void drawHorizontal(Canvas canvas, RecyclerView parent) {
    canvas.save();
    final int top;
    final int bottom;
    if (parent.getClipToPadding()) {
      top = parent.getPaddingTop() + mPadding;
      bottom = parent.getHeight() - parent.getPaddingBottom() - mPadding;
      canvas.clipRect(parent.getPaddingLeft(), top,
          parent.getWidth() - parent.getPaddingRight(), bottom);
    } else {
      top = mPadding;
      bottom = parent.getHeight() - mPadding;
    }

    final int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      if (!mDrawEndLine && i + 1 == childCount) {
        break;
      }
      final View child = parent.getChildAt(i);
      parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
      final int right = mBounds.right + Math.round(ViewCompat.getTranslationX(child));
      final int left = right - mDivider.getIntrinsicWidth();
      mDivider.setBounds(left, top, right, bottom);
      mDivider.draw(canvas);
    }
    canvas.restore();
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                             RecyclerView.State state) {
    if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
      outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
    } else {
      outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
    }
  }

  private int getOrientation(RecyclerView parent) {
    if (parent.getLayoutManager() instanceof LinearLayoutManager) {
      return ((LinearLayoutManager) parent.getLayoutManager()).getOrientation();
    } else {
      throw new IllegalStateException("DividerItemDecoration can only be used with a LinearLayoutManager.");
    }
  }
}