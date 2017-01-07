package online.duoyu.sparkle.model;

import android.text.TextUtils;

import com.squareup.wire.Wire;

import java.util.ArrayList;
import java.util.List;

import online.duoyu.sparkle.model.proto.Comment;
import online.duoyu.sparkle.model.proto.Correct;
import online.duoyu.sparkle.model.proto.Diary;
import online.duoyu.sparkle.model.proto.Notification;
import online.duoyu.sparkle.model.proto.User;
import online.duoyu.sparkle.utils.CollectionUtils;
import online.duoyu.sparkle.utils.Const;

/**
 * Created by littlekey on 12/19/16.
 */

public class DataVerifier {
  private DataVerifier() {
  }

  public static User verify(User user) {
    if (user == null) {
      return null;
    }
    if (TextUtils.isEmpty(user.nickname)) {
      return null;
    }
    if (TextUtils.isEmpty(user.user_id)) {
      return null;
    }
    User.Builder builder = user.newBuilder();
    builder.nickname(Wire.get(user.nickname, Const.EMPTY_STRING));
    return builder.build();
  }

  public static Diary verify(Diary diary) {
    if (diary == null) {
      return null;
    }
    if (TextUtils.isEmpty(diary.diary_id)) {
      return null;
    }
    if (TextUtils.isEmpty(diary.title)) {
      return null;
    }
    if (TextUtils.isEmpty(diary.content)) {
      return null;
    }
    if (verify(diary.author) == null) {
      return null;
    }
    User author = verify(diary.author);
    if (author == null) {
      return null;
    }
    Diary.Builder builder = diary.newBuilder()
        .author(author)
        .liked(Wire.get(diary.liked, false))
        .likes(Wire.get(diary.likes, 0))
        .comments(Wire.get(diary.comments, 0))
        .corrects(Wire.get(diary.corrects, 0))
        .attentions(Wire.get(diary.attentions, 0));
    return builder.build();
  }

  public static Correct verify(Correct correct) {
    if (correct == null) {
      return null;
    }
    if (TextUtils.isEmpty(correct.correct_id)) {
      return null;
    }
    if (CollectionUtils.isEmpty(correct.content)) {
      return null;
    }
    User author = verify(correct.author);
    if (author == null) {
      return null;
    }
    Diary diary = verify(correct.diary);
    if (diary == null) {
      return null;
    }
    Correct.Builder builder = correct.newBuilder()
        .diary(diary)
        .author(author)
        .liked(Wire.get(correct.liked, false))
        .comments(Wire.get(diary.comments, 0))
        .likes(Wire.get(correct.likes, 0));
    return builder.build();
  }

  public static Comment verify(Comment comment) {
    if (comment == null) {
      return null;
    }
    if (TextUtils.isEmpty(comment.comment_id)) {
      return null;
    }
    if (TextUtils.isEmpty(comment.entire_id)) {
      return null;
    }
    User author = verify(comment.author);
    if (author == null) {
      return null;
    }
    Comment.Builder builder = comment.newBuilder();
    if (comment.quote_id != null) {
      if (TextUtils.isEmpty(comment.quote)) {
        return null;
      }
      User quote_author = verify(comment.quote_author);
      if (quote_author == null) {
        return null;
      }
      builder.quote_author(quote_author);
    }
    return builder
        .author(author)
        .likes(Wire.get(comment.likes, 0))
        .liked(Wire.get(comment.liked, false))
        .build();
  }

  public static Notification verify(Notification notification) {
    if (notification == null) {
      return null;
    }
    if (TextUtils.isEmpty(notification.which)) {
      return null;
    }
    List<User> users = new ArrayList<>();
    for (User user: notification.users) {
      CollectionUtils.add(users, verify(user));
    }
    if (users.size() < 1) {
      return null;
    }
    Notification.Builder builder = notification.newBuilder();
    return builder
        .users(users)
        .unread(Wire.get(notification.unread, false))
        .build();
  }
}
