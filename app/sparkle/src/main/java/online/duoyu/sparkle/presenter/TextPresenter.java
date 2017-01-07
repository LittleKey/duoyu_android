package online.duoyu.sparkle.presenter;

import android.text.Spannable;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.Calendar;

import online.duoyu.sparkle.R;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.utils.SparkleUtils;

/**
 * Created by littlekey on 12/25/16.
 */

public class TextPresenter extends SparklePresenter {


  @Override
  public void bind(Model model) {
    Object attrValue = getValueByViewId(id(), model);
    if (attrValue == null) {
      view().setVisibility(View.GONE);
      return;
    }
    view().setVisibility(View.VISIBLE);
    if (attrValue instanceof Spannable && view() instanceof TextView) {
      bindSpan((TextView) view(), (Spannable) attrValue);
    } else if (attrValue instanceof CharSequence && view() instanceof TextView) {
      bindText((TextView) view(), (CharSequence) attrValue);
    }
  }

  private void bindText(TextView view, CharSequence attrValue) {
    view.setText(attrValue);
  }

  private void bindSpan(TextView view, Spannable spannable) {
    view.setText(spannable);
    Linkify.addLinks(view, Linkify.ALL);
  }

  private Object getValueByViewId(int id, Model model) {
    switch (id) {
      /** Common **/
      case R.id.correct_sentence:
        return model.correct_sentence;
      case R.id.origin_sentence:
        return model.origin_sentence;
      case R.id.divider_text:
      case R.id.content:
//        return Html.fromHtml(model.description);
      case R.id.description:
        return model.description;
      case R.id.theme_title:
      case R.id.title:
        return model.title.trim();
      case R.id.month:
        return model.month;
      case R.id.theme_day:
      case R.id.day:
        return model.day;
      case R.id.date:
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(model.date);
        DateTime date_time = new DateTime(cal);
        return date_time.toString("yyyy-MM-dd");
      case R.id.theme_week:
      case R.id.week:
        return model.week;
      case R.id.nickname:
//      case R.id.user_name:
        return model.user.nickname;
      case R.id.attentions:
        return SparkleUtils.formatInteger(model.count.attentions);
      case R.id.likes:
        return SparkleUtils.formatInteger(model.count.likes);
      case R.id.comments:
        return SparkleUtils.formatInteger(model.count.comments);
      case R.id.corrects:
        return SparkleUtils.formatInteger(model.count.corrects);
      case R.id.quote_content:
        if (model.addition == null) {
          return null;
        }
        return SparkleUtils.formatString("@%s: %s", model.addition.user.nickname, model.addition.description);
//      case R.id.avatar:
//      case R.id.subtitle:
//        return model.subtitle;
//      case R.id.date:
//        return model.date;
//      case R.id.language:
//        if (TextUtils.isEmpty(model.language)) {
//          return SparkleApplication.getInstance().getString(R.string.unknown);
//        }
//        return model.language;
//      case R.id.page_number:
//        return SparkleUtils.formatString(R.string.page_count, model.count.pages);
//      case R.id.size:
//        if (TextUtils.isEmpty(model.fileSize)) {
//          return SparkleApplication.getInstance().getString(R.string.zero_file_size);
//        }
//        return model.fileSize;
//      case R.id.likes:
//        return String.valueOf(model.count.likes);
//      case R.id.number:
//        return String.valueOf(model.count.number);
//      case R.id.rating_count:
//        return SparkleUtils.formatString(R.string.rating_count,
//            model.count.rating, model.count.rating_count);
//      case R.id.rating:
//        return SparkleUtils.formatString(R.string.rating_num, model.count.rating);
    }
    return null;
  }
}
