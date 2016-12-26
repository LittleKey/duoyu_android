package online.duoyu.sparkle.model;

import android.text.format.DateFormat;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import online.duoyu.sparkle.activity.AttentionsActivity;
import online.duoyu.sparkle.activity.CommentsActivity;
import online.duoyu.sparkle.activity.CorrectsActivity;
import online.duoyu.sparkle.activity.DiaryActivity;
import online.duoyu.sparkle.activity.EditCorrectActivity;
import online.duoyu.sparkle.activity.UserActivity;
import online.duoyu.sparkle.model.proto.Action;
import online.duoyu.sparkle.model.proto.Diary;
import online.duoyu.sparkle.model.proto.User;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.SparkleUtils;

/**
 * Created by littlekey on 12/19/16.
 */

public class ModelFactory {
  private ModelFactory() {
  }

  public static Model createModelFromUser(User user, Model.Template template) {
    user = DataVerifier.verify(user);
    if (user == null) {
      return null;
    }
    return new Model.Builder()
        .user(user)
        .type(Model.Type.USER)
        .template(template)
        .identity(user.user_id)
        .title(user.nickname)
        .cover(user.avatar)
        .email(user.email)
        .description(user.introduce)
        .build();
  }

  public static Model createModelFromDiary(Diary diary, Model.Template template) {
    diary = DataVerifier.verify(diary);
    if (diary == null) {
      return null;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(diary.diary_date * 1000);
    DateTime date_time = new DateTime(cal);
    String month = date_time.monthOfYear().getAsShortText();
    String week = date_time.dayOfWeek().getAsShortText();
    Map<Integer, Action> actions = new HashMap<>();
    actions.put(Const.ACTION_MAIN, new Action.Builder()
        .type(Action.Type.JUMP)
        .clazz(DiaryActivity.class.getName())
        .build());
    actions.put(Const.ACTION_LIKED, new Action.Builder()
        .type(Action.Type.LIKED)
        .build());
    actions.put(Const.ACTION_ATTENTIONS, new Action.Builder()
        .type(Action.Type.JUMP)
        .clazz(AttentionsActivity.class.getName())
        .build());
    actions.put(Const.ACTION_COMMENTS, new Action.Builder()
        .type(Action.Type.JUMP)
        .clazz(CommentsActivity.class.getName())
        .build());
    actions.put(Const.ACTION_CORRECTS, new Action.Builder()
        .type(Action.Type.JUMP)
        .clazz(CorrectsActivity.class.getName())
        .build());
    actions.put(Const.ACTION_EDIT_CORRECT, new Action.Builder()
        .type(Action.Type.JUMP)
        .clazz(EditCorrectActivity.class.getName())
        .build());
    actions.put(Const.ACTION_USER, new Action.Builder()
        .type(Action.Type.JUMP)
        .clazz(UserActivity.class.getName())
        .build());
    return new Model.Builder()
        .diary(diary)
        .user(diary.author)
        .template(template)
        .type(Model.Type.DIARY)
        .identity(diary.diary_id)
        .language(diary.language.name())
        .date(cal.getTimeInMillis())
        .month(month)
        .week(week)
        .day(SparkleUtils.formatString("%02d", date_time.getDayOfMonth()))
        .title(diary.title)
        .description(diary.content)
        .cover(diary.author.avatar)
        .actions(actions)
        .build();
  }
}
