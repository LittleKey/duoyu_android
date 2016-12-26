package online.duoyu.sparkle.fragment;

import android.os.Bundle;

/**
 * Created by littlekey on 12/27/16.
 */

public class EditCorrectFragment extends BaseFragment {

  public static EditCorrectFragment newInstance() {
    Bundle args = new Bundle();
    EditCorrectFragment fragment = new EditCorrectFragment();
    fragment.setArguments(args);
    return fragment;
  }
}
