package online.duoyu.sparkle.fragment;

import android.os.Bundle;

/**
 * Created by littlekey on 1/11/17.
 */

public class CorrectFragment extends BaseFragment {

  public static CorrectFragment newInstance() {
    Bundle args = new Bundle();
    CorrectFragment fragment = new CorrectFragment();
    fragment.setArguments(args);
    return fragment;
  }
}
