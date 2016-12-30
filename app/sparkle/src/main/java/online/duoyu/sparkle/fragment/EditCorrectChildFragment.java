package online.duoyu.sparkle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.littlekey.mvp.DataLoadObserver;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.utils.Const;

/**
 * Created by littlekey on 12/28/16.
 */

public class EditCorrectChildFragment extends BaseFragment {

  public static EditCorrectChildFragment newInstance(Model model, int position) {
    Bundle args = new Bundle();
    args.putInt(Const.KEY_POSITION, position);
    args.putParcelable(Const.KEY_MODEL, model);
    EditCorrectChildFragment fragment = new EditCorrectChildFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_edit_correct_child, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }
}
