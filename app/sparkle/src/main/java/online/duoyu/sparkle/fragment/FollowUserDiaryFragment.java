package online.duoyu.sparkle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import online.duoyu.sparkle.R;

/**
 * Created by littlekey on 12/20/16.
 */

public class FollowUserDiaryFragment extends LazyLoadFragment {

  public static FollowUserDiaryFragment newInstance(String month) {
    Bundle args = new Bundle();
    args.putString("TEST", month);
    FollowUserDiaryFragment fragment = new FollowUserDiaryFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  protected View lazyLoad(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_folow_user_diary, container, false);
    TextView textView = (TextView) view.findViewById(R.id.month);
    textView.setText(getArguments().getString("TEST"));
    return view;
  }
}
