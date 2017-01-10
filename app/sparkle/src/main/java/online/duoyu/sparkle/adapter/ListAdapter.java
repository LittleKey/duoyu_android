package online.duoyu.sparkle.adapter;

import android.os.Bundle;
import android.view.ViewGroup;

import com.squareup.wire.Wire;

import me.littlekey.mvp.adapter.MvpAdapter;
import me.littlekey.mvp.presenter.ViewGroupPresenter;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.presenter.SparklePresenterFactory;

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
      case ITEM_DIARY:
        return SparklePresenterFactory.createDiaryItemPresenter(parent, R.layout.item_diary);
      case ITEM_MONTH:
        return SparklePresenterFactory.createMonthItemPresenter(parent, R.layout.item_month);
      case ITEM_DIARY_WITH_MONTH:
        return SparklePresenterFactory.createDiaryWithMonthItemPresenter(parent, R.layout.item_diary_with_month);
      case ITEM_CORRECT_SENTENCE:
        return SparklePresenterFactory.createCorrectSentenceItemPresenter(parent, R.layout.item_correct_sentence);
      case ITEM_DIVIDER_HEADER:
        return SparklePresenterFactory.createDividerHeaderItemPresenter(parent, R.layout.item_divider_header);
      case ITEM_ORIGIN_SENTENCE:
        return SparklePresenterFactory.createOriginSentenceItemPresenter(parent, R.layout.item_origin_sentence);
      case ITEM_CORRECT:
        return SparklePresenterFactory.createCorrectItemPresenter(parent, R.layout.item_correct, this);
      case ITEM_COMMENT:
        return SparklePresenterFactory.createCommentItemPresenter(parent, R.layout.item_comment, this);
      case ITEM_DIARY_TITLE:
        return SparklePresenterFactory.createDiaryTitleItemPresenter(parent, R.layout.item_diary_title);
      case ITEM_NOTIFICATION:
        return SparklePresenterFactory.createNotificationItemPresenter(parent, R.layout.item_notification);
      case ITEM_FOLLOWER:
        return SparklePresenterFactory.createFollowerItemPresenter(parent, R.layout.item_follower, this);
      case ITEM_HAPPENING:
        return SparklePresenterFactory.createHappeningItemPresenter(parent, R.layout.item_happening);
      case ITEM_CORRECT_WITH_DIARY:
        return SparklePresenterFactory.createCorrectWithDiaryItemPresenter(parent, R.layout.item_correct_with_diary, this);
      default:
        throw new IllegalStateException("Nonsupport template : " + template.name());
    }
  }

  @Override
  public int getDataItemViewType(int position) {
    return getItem(position).template.getValue();
  }
}
