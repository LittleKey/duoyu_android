package online.duoyu.sparkle.fragment;

import android.os.Bundle;

/**
 * Created by littlekey on 12/27/16.
 */

public class CorrectsFragment extends BaseFragment {

  public static CorrectsFragment newInstance() {
    Bundle args = new Bundle();
    CorrectsFragment fragment = new CorrectsFragment();
    fragment.setArguments(args);
    return fragment;
  }
}
