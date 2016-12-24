package online.duoyu.sparkle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import online.duoyu.sparkle.R;

/**
 * Created by littlekey on 12/24/16.
 */

public class RegisterFragment extends BaseFragment {

  public static RegisterFragment newInstance() {
    Bundle args = new Bundle();
    RegisterFragment fragment = new RegisterFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_register, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }
}
