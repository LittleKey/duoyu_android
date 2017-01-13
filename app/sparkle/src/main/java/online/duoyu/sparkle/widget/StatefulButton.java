package online.duoyu.sparkle.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import online.duoyu.sparkle.R;
import online.duoyu.sparkle.utils.Colorful;
import online.duoyu.sparkle.utils.ResourcesUtils;

/**
 * Created by littlekey on 12/24/16.
 */

public class StatefulButton extends FrameLayout {

  public static final int STATE_GONE = -1;
  public static final int STATE_DONE = 1;
  public static final int STATE_CANCELED = 2;
  public static final int STATE_FOLLOW = 3;
  public static final int STATE_FOLLOWING = 4;
  public static final int STATE_EDIT_PROFILE = 5;
  private SparseIntArray mStateMap = new SparseIntArray();
  private int mLayout;
  private TextView mTextView;
  private int mCurrentState;

  public StatefulButton(Context context) {
    this(context, null);
  }

  public StatefulButton(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public StatefulButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    TypedArray a =
        context.obtainStyledAttributes(attrs, R.styleable.StatefulButton);
    mStateMap.put(STATE_DONE, a.getResourceId(R.styleable.StatefulButton_stateDone, 0));
    mStateMap.put(STATE_CANCELED, a.getResourceId(R.styleable.StatefulButton_stateCanceled, 0));
    mStateMap.put(STATE_FOLLOW, a.getResourceId(R.styleable.StatefulButton_stateFollow, 0));
    mStateMap.put(STATE_FOLLOWING, a.getResourceId(R.styleable.StatefulButton_stateFollowing, 0));
    mStateMap.put(STATE_EDIT_PROFILE, a.getResourceId(R.styleable.StatefulButton_stateEditProfile, 0));
    mLayout = a.getResourceId(R.styleable.StatefulButton_layoutRes, R.layout.stateful_button);
    init();
    styleAppearance(a);
    a.recycle();
  }

  protected void init() {
    LayoutInflater.from(getContext()).inflate(mLayout, this, true);
    mTextView = (TextView) findViewById(R.id.stateful_btn_label);
    mTextView.setIncludeFontPadding(false);
  }

  protected void styleAppearance(TypedArray appearance) {
    int n = appearance.getIndexCount();
    for (int i = 0; i < n; i++) {
      int attr = appearance.getIndex(i);
      switch (attr) {
        case R.styleable.StatefulButton_android_textColor:
          setTextColor(appearance.getColorStateList(attr));
          break;
        case R.styleable.StatefulButton_android_textSize:
          setTextSize(appearance.getDimensionPixelSize(attr, 0));
          break;
        case R.styleable.StatefulButton_android_text:
          setText(appearance.getString(attr));
          break;
        case R.styleable.StatefulButton_android_gravity:
          setGravity(appearance.getInt(attr, -1));
          break;
        case R.styleable.StatefulButton_android_singleLine:
          setSingleLine(appearance.getBoolean(attr, true));
          break;
        case R.styleable.StatefulButton_android_maxLines:
          setMaxLines(appearance.getInt(attr, 1));
          break;
        case R.styleable.StatefulButton_android_background:
          ResourcesUtils.setBackground(this, appearance.getDrawable(attr));
          break;
        case R.styleable.StatefulButton_android_drawable:
          ResourcesUtils.setBackground(mTextView, appearance.getDrawable(attr));
          break;
        case R.styleable.StatefulButton_android_alpha:
          setAlpha(appearance.getFloat(attr, 1));
          break;
      }
    }
  }

  public void updateState() {
    setState(mCurrentState);
  }

  public void setState(int state) {
    mCurrentState = state;
    if (state == STATE_GONE) {
      setVisibility(GONE);
      return;
    } else if (getVisibility() == GONE) {
      setVisibility(VISIBLE);
    }
    setState(getContext(), mStateMap.get(state));
  }

  private void setState(Context context, int resId) {
    TypedArray appearance =
        context.obtainStyledAttributes(Colorful.getThemeDelegate().themeStyle(resId),
            R.styleable.StatefulButton);
    styleAppearance(appearance);
    appearance.recycle();
  }

  protected void setSingleLine(boolean singleLine) {
    mTextView.setSingleLine(singleLine);
  }

  protected void setGravity(int gravity) {
    mTextView.setGravity(gravity);
  }

  protected void setTextSize(int dimensionPixelSize) {
    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimensionPixelSize);
  }


  protected void setTextColor(ColorStateList textColor) {
    mTextView.setTextColor(textColor);
  }

  protected void setText(String text) {
    mTextView.setText(text);
  }

  protected void setMaxLines(int maxLines) {
    mTextView.setMaxLines(maxLines);
  }
}
