package online.duoyu.sparkle.adapter;

import android.os.Bundle;
import android.view.ViewGroup;

import com.squareup.wire.Wire;

import me.littlekey.mvp.adapter.MvpAdapter;
import me.littlekey.mvp.presenter.ViewGroupPresenter;
import online.duoyu.sparkle.model.Model;

/**
 * Created by littlekey on 12/20/16.
 */

public class ListAdapter extends MvpAdapter<Model> {

  private Bundle mBundle;

  public ListAdapter() {
    super();
  }

  public ListAdapter(Bundle bundle) {
    this();
    this.mBundle = bundle;
  }

  @Override
  protected ViewGroupPresenter onCreateDataViewPresenter(ViewGroup parent, int viewType) {
    Model.Template template = Wire.get(Model.Template.fromValue(viewType), Model.Template.UNSUPPORTED);
    switch (template) {
      default:
        throw new IllegalStateException("Nonsupport template : " + template.name());
    }
  }

  @Override
  public int getDataItemViewType(int position) {
    return getItem(position).template.getValue();
  }
}
