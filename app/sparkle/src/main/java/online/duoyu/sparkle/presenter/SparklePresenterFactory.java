package online.duoyu.sparkle.presenter;

import android.view.ViewGroup;

import me.littlekey.mvp.presenter.ViewGroupPresenter;
import me.littlekey.mvp.widget.MvpRecyclerView;
import online.duoyu.sparkle.R;

/**
 * Created by littlekey on 12/24/16.
 */

public class SparklePresenterFactory {

  private SparklePresenterFactory() {}

  public static ViewGroupPresenter createEmptyPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(new ActionPresenter());
  }

  public static ViewGroupPresenter createDiaryItemPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(R.id.theme_title, new TextPresenter())
        .add(R.id.theme_title, new ThemePresenter())
        .add(R.id.theme_day, new TextPresenter())
        .add(R.id.theme_day, new ThemePresenter())
        .add(R.id.theme_week, new TextPresenter())
        .add(R.id.theme_week, new ThemePresenter())
        .add(R.id.nickname, new TextPresenter())
        .add(R.id.content, new TextPresenter())
        .add(R.id.avatar, new ImagePresenter())
        .add(0, new ListenActionPresenter())
        .add(0, new ActionPresenter());
  }

  public static ViewGroupPresenter createDiaryWithMonthItemPresenter(ViewGroup parent, int layout) {
    return createDiaryItemPresenter(parent, layout).remove(0)
        .add(R.id.month, new TextPresenter())
        .add(new ActionPresenter());
  }

  public static ViewGroupPresenter createMonthItemPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(R.id.month, new TextPresenter())
        .add(new ListenActionPresenter());
  }

  public static ViewGroupPresenter createDiaryTitleItemPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(R.id.title, new TextPresenter())
        .add(R.id.nickname, new TextPresenter());
  }

  public static ViewGroupPresenter createCorrectSentenceItemPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(R.id.avatar, new ImagePresenter())
        .add(R.id.nickname, new TextPresenter())
        .add(R.id.date, new TextPresenter())
        .add(R.id.correct_sentence, new TextPresenter());
  }

  public static ViewGroupPresenter createDividerHeaderItemPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(R.id.divider_text, new TextPresenter());
  }

  public static ViewGroupPresenter createOriginSentenceItemPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(R.id.origin_sentence, new TextPresenter());
  }

  public static ViewGroupPresenter createCorrectItemPresenter(ViewGroup parent, int layout,
        MvpRecyclerView.Adapter adapter) {
    return new ViewGroupPresenter(parent, layout, adapter)
        .add(R.id.avatar, new ImagePresenter())
        .add(R.id.nickname, new TextPresenter())
        .add(R.id.date, new TextPresenter())
        .add(R.id.correct_sentence_list, new ListPresenter())
        .add(R.id.likes, new TextPresenter())
        .add(R.id.likes, new ActionPresenter());
  }

  public static ViewGroupPresenter createCorrectWithDiaryItemPresenter(ViewGroup parent, int layout,
        MvpRecyclerView.Adapter adapter) {
    return new ViewGroupPresenter(parent, layout, adapter)
        .add(R.id.title, new TextPresenter())
        .add(R.id.date, new TextPresenter())
        .add(R.id.correct_sentence_list, new ListPresenter())
        .add(new ActionPresenter());
  }

  public static ViewGroupPresenter createCorrectWithOriginContentSentenceItemPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(R.id.origin_sentence, new TextPresenter())
        .add(R.id.correct_sentence, new TextPresenter());
  }

  public static ViewGroupPresenter createLanguageItemPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(R.id.language_text, new TextPresenter())
        .add(R.id.selected, new FlagPresenter())
        .add(new ActionPresenter());
  }

  public static ViewGroupPresenter createCommentItemPresenter(ViewGroup parent, int layout,
        MvpRecyclerView.Adapter adapter) {
    return new ViewGroupPresenter(parent, layout, adapter)
        .add(R.id.nickname, new TextPresenter())
        .add(R.id.avatar, new ImagePresenter())
        .add(R.id.date, new TextPresenter())
        .add(R.id.content, new TextPresenter())
        .add(R.id.quote_content, new TextPresenter())
        .add(R.id.likes, new TextPresenter())
        .add(R.id.likes, new ActionPresenter())
        .add(new ActionPresenter());
  }

  public static ViewGroupPresenter createNotificationItemPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(R.id.avatar, new ImagePresenter())
        .add(R.id.description, new TextPresenter())
        .add(R.id.title, new TextPresenter())
        .add(new ActionPresenter());
  }

  public static ViewGroupPresenter createFollowerItemPresenter(ViewGroup parent, int layout,
        MvpRecyclerView.Adapter adapter) {
    return new ViewGroupPresenter(parent, layout, adapter)
        .add(R.id.avatar, new ImagePresenter())
        .add(R.id.nickname, new TextPresenter())
        .add(R.id.theme_btn_follow, new FlagPresenter())
        .add(R.id.theme_btn_follow, new ActionPresenter())
        .add(new ActionPresenter());
  }

  public static ViewGroupPresenter createHappeningItemPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(R.id.description, new TextPresenter())
        .add(R.id.title, new TextPresenter())
        .add(R.id.date, new TextPresenter())
        .add(new ActionPresenter());
  }

  public static ViewGroupPresenter createDiaryPresenter(ViewGroup view) {
    return new ViewGroupPresenter(view)
        .add(R.id.title, new TextPresenter())
        .add(R.id.month, new TextPresenter())
        .add(R.id.day, new TextPresenter())
        .add(R.id.week, new TextPresenter())
        .add(R.id.nickname, new TextPresenter())
        .add(R.id.avatar, new ImagePresenter())
        .add(R.id.introduce, new TextPresenter())
        .add(R.id.content, new TextPresenter())
        .add(R.id.attentions, new ActionPresenter())
        .add(R.id.attentions, new TextPresenter())
        .add(R.id.likes, new ActionPresenter())
        .add(R.id.likes, new TextPresenter())
        .add(R.id.comments, new ActionPresenter())
        .add(R.id.comments, new TextPresenter())
        .add(R.id.corrects, new ActionPresenter())
        .add(R.id.corrects, new TextPresenter())
        .add(R.id.edit_correct, new ActionPresenter())
        .add(R.id.user, new ActionPresenter());
  }

  public static ViewGroupPresenter createUserInfoPresenter(ViewGroup view) {
    return new ViewGroupPresenter(view)
        .add(R.id.nickname, new TextPresenter())
        .add(R.id.avatar, new ImagePresenter())
        .add(R.id.email, new TextPresenter())
        .add(R.id.theme_btn_follow, new FlagPresenter())
        .add(R.id.theme_btn_follow, new ActionPresenter())
        .add(R.id.theme_btn_edit_profile, new ActionPresenter())
        .add(R.id.published_diaries, new ActionPresenter())
        .add(R.id.attending_diaries, new ActionPresenter())
        .add(R.id.published_corrects, new ActionPresenter())
        .add(R.id.btn_logout, new ActionPresenter())
        .add(R.id.theme_followers, new TextPresenter())
        .add(R.id.theme_followers, new ThemePresenter())
        .add(R.id.theme_following, new TextPresenter())
        .add(R.id.theme_following, new ThemePresenter());
  }
}
