package online.duoyu.sparkle.fragment;

import android.os.Bundle;

/**
 * Created by littlekey on 12/27/16.
 */

public class AttentionsFragment extends BaseFragment {

  public static AttentionsFragment newInstance() {
    Bundle args = new Bundle();
    AttentionsFragment fragment = new AttentionsFragment();
    fragment.setArguments(args);
    return fragment;
  }
}
