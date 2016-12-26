package online.duoyu.sparkle.fragment;

import android.os.Bundle;

/**
 * Created by littlekey on 12/27/16.
 */

public class CommentsFragment extends BaseFragment {

  public static CommentsFragment newInstance() {
    Bundle args = new Bundle();
    CommentsFragment fragment = new CommentsFragment();
    fragment.setArguments(args);
    return fragment;
  }
}
