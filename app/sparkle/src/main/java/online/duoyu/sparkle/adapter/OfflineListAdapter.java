package online.duoyu.sparkle.adapter;

import android.view.ViewGroup;

import com.squareup.wire.Wire;

import me.littlekey.mvp.adapter.MvpAdapter;
import me.littlekey.mvp.presenter.ViewGroupPresenter;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.presenter.SparklePresenterFactory;

/**
 * Created by littlekey on 1/3/17.
 */

public class OfflineListAdapter extends MvpAdapter<Model> {

  public OfflineListAdapter() {
    super();
  }

  @Override
  protected ViewGroupPresenter onCreateDataViewPresenter(ViewGroup parent, int viewType) {
    Model.Template template = Wire.get(Model.Template.fromValue(viewType), Model.Template.UNSUPPORTED);
    switch (template) {
      case ITEM_CORRECT_WITH_ORIGIN_CONTENT_SENTENCE:
        return SparklePresenterFactory.createCorrectWithOriginContentSentenceItemPresenter(
            parent, R.layout.item_correct_with_origin_content_sentence);
      default:
        throw new IllegalStateException("Nonsupport template : " + template.name());
    }
  }

  @Override
  public int getDataItemViewType(int position) {
    return getItem(position).template.getValue();
  }
}
