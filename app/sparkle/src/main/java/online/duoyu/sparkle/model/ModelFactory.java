package online.duoyu.sparkle.model;

import android.text.format.DateFormat;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import online.duoyu.sparkle.model.proto.Diary;
import online.duoyu.sparkle.model.proto.User;
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
        .build();
  }
}
