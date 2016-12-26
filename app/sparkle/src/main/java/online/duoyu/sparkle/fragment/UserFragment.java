package online.duoyu.sparkle.fragment;

import android.os.Bundle;

/**
 * Created by littlekey on 12/27/16.
 */

public class UserFragment extends BaseFragment {

  public static UserFragment newInstance() {
    Bundle args = new Bundle();
    UserFragment fragment = new UserFragment();
    fragment.setArguments(args);
    return fragment;
  }
}
