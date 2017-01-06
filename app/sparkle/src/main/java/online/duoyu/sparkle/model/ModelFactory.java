package online.duoyu.sparkle.model;

import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.activity.CommentsActivity;
import online.duoyu.sparkle.activity.CorrectsActivity;
import online.duoyu.sparkle.activity.DiaryActivity;
import online.duoyu.sparkle.activity.EditCorrectActivity;
import online.duoyu.sparkle.activity.UserActivity;
import online.duoyu.sparkle.model.proto.Action;
import online.duoyu.sparkle.model.proto.Comment;
import online.duoyu.sparkle.model.proto.Correct;
import online.duoyu.sparkle.model.proto.Count;
import online.duoyu.sparkle.model.proto.Diary;
import online.duoyu.sparkle.model.proto.Flag;
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
    Count count = new Count.Builder()
        .likes(diary.likes)
        .corrects(diary.corrects)
        .attentions(diary.attentions)
        .comments(diary.comments)
        .build();
    Flag flag = new Flag.Builder()
        .is_liked(diary.liked)
        .is_attending(diary.attending)
        .is_corrected(diary.corrected)
        .build();
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
        .type(Action.Type.ATTENTION)
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
        .template(template)
        .type(Model.Type.DIARY)
        .diary(diary)
        .user(diary.author)
        .identity(diary.diary_id)
        .language(diary.language.name())
        .date(cal.getTimeInMillis())
        .month(month)
        .week(week)
        .day(SparkleUtils.formatString("%02d", date_time.getDayOfMonth()))
        .title(diary.title)
        .description(diary.content)
        .content(Arrays.asList(SparkleUtils.split_content_v1(diary.content)))
        .cover(diary.author.avatar)
        .count(count)
        .flag(flag)
        .actions(actions)
        .build();
  }

  public static Model createModelFromCorrect(Correct correct, Model.Template template) {
    correct = DataVerifier.verify(correct);
    if (correct == null) {
      return null;
    }
    Count count = new Count.Builder()
        .likes(correct.likes)
        .comments(correct.comments)
        .build();
    Flag flag = new Flag.Builder()
        .is_liked(correct.liked)
        .build();
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(correct.date * 1000);
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
    actions.put(Const.ACTION_COMMENTS, new Action.Builder()
        .type(Action.Type.JUMP)
        .clazz(CommentsActivity.class.getName())
        .build());
    if (SparkleApplication.getInstance().getAccountManager().isSelf(correct.author.user_id)) {
      actions.put(Const.ACTION_EDIT_CORRECT, new Action.Builder()
          .type(Action.Type.JUMP)
          .clazz(EditCorrectActivity.class.getName())
          .build());
    }
    actions.put(Const.ACTION_USER, new Action.Builder()
        .type(Action.Type.JUMP)
        .clazz(UserActivity.class.getName())
        .build());
    return new Model.Builder()
        .template(template)
        .type(Model.Type.CORRECT)
        .diary(correct.diary)
        .correct(correct)
        .user(correct.author)
        .identity(correct.correct_id)
        .date(cal.getTimeInMillis())
        .month(month)
        .week(week)
        .day(SparkleUtils.formatString("%02d", date_time.getDayOfMonth()))
        .title(correct.diary.title)
        .content(correct.content)
        .cover(correct.author.avatar)
        .actions(actions)
        .count(count)
        .flag(flag)
        .addition(createModelFromDiary(correct.diary, Model.Template.DATA))
        .build();
  }

  public static Model createModelFromComment(Comment comment, Model.Template template) {
    comment = DataVerifier.verify(comment);
    if (comment == null) {
      return null;
    }
    Count count = new Count.Builder()
        .likes(comment.likes)
        .floor(comment.floor)
        .build();
    Flag flag = new Flag.Builder()
        .is_liked(comment.liked)
        .build();
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(comment.date * 1000);
    DateTime date_time = new DateTime(cal);
    String month = date_time.monthOfYear().getAsShortText();
    String week = date_time.dayOfWeek().getAsShortText();
    Map<Integer, Action> actions = new HashMap<>();
    actions.put(Const.ACTION_MAIN, new Action.Builder()
        .type(Action.Type.COMMENT)
        .build());
    actions.put(Const.ACTION_LIKED, new Action.Builder()
        .type(Action.Type.LIKED)
        .build());
    Model addition = null;
    if (comment.quote_id != null) {
      // TODO : determine comment deleted
      addition = new Model.Builder()
          .identity(comment.quote_id)
          .user(comment.quote_author)
          .description(comment.quote)
          .build();
    }
    return new Model.Builder()
        .type(Model.Type.COMMENT)
        .template(template)
        .identity(comment.comment_id)
        .user(comment.author)
        .cover(comment.author.avatar)
        .comment(comment)
        .description(comment.content)
        .date(cal.getTimeInMillis())
        .month(month)
        .week(week)
        .day(SparkleUtils.formatString("%02d", date_time.getDayOfMonth()))
        .addition(addition)
        .actions(actions)
        .count(count)
        .flag(flag)
        .build();
  }
}
