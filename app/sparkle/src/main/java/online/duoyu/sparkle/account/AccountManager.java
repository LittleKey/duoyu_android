package online.duoyu.sparkle.account;

import android.app.PendingIntent;
import android.content.Intent;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.activity.AttendingDiariesActivity;
import online.duoyu.sparkle.activity.LoginActivity;
import online.duoyu.sparkle.activity.PublishedCorrectsActivity;
import online.duoyu.sparkle.activity.PublishedDiariesActivity;
import online.duoyu.sparkle.event.AccountChangeEvent;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.model.ModelFactory;
import online.duoyu.sparkle.model.proto.Action;
import online.duoyu.sparkle.model.proto.User;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.PreferenceUtils;
import timber.log.Timber;

/**
 * Created by littlekey on 12/19/16.
 */

public class AccountManager {

  private boolean isSignIn;
  private Model mUser;
  private String mToken;

  public void login(User user, String token) {
    setIsSignIn(true);
    Model model = ModelFactory.createModelFromUser(user, Model.Template.DATA);
    if (model == null) {
      logout();
      return;
    }
    Map<Integer, Action> actions = new HashMap<>(model.actions);
    actions.put(Const.ACTION_PUBLISHED_DIARIES, new Action.Builder()
        .type(Action.Type.JUMP)
        .clazz(PublishedDiariesActivity.class.getName())
        .build());
    actions.put(Const.ACTION_ATTENDING_DIARIES, new Action.Builder()
        .type(Action.Type.JUMP)
        .clazz(AttendingDiariesActivity.class.getName())
        .build());
    actions.put(Const.ACTION_PUBLISHED_CORRECTS, new Action.Builder()
        .type(Action.Type.JUMP)
        .clazz(PublishedCorrectsActivity.class.getName())
        .build());
    actions.put(Const.ACTION_LOGOUT, new Action.Builder()
        .type(Action.Type.LOGOUT)
        .build());
    mUser = model.newBuilder()
        .actions(actions)
        .build();
    mToken = token;
    PreferenceUtils.setString(Const.LAST_ACTION, Const.LAST_TOKEN, mToken);
    EventBus.getDefault().post(new AccountChangeEvent(true));
  }

  public void logout() {
    setIsSignIn(false);
    mUser = null;
    mToken = null;
    EventBus.getDefault().post(new AccountChangeEvent(false));
    PreferenceUtils.removeString(Const.LAST_ACTION, Const.LAST_TOKEN);
    Intent intent = new Intent(SparkleApplication.getInstance(), LoginActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
    PendingIntent pendingIntent = PendingIntent.getActivity(
        SparkleApplication.getInstance(), 0, intent, 0);
    try {
      pendingIntent.send();
    } catch (PendingIntent.CanceledException e) {
      Timber.e("logout jump activity error");
    }
  }

  public boolean isSignIn() {
    return isSignIn;
  }

  private void setIsSignIn(boolean isSignIn) {
    this.isSignIn = isSignIn;
  }

  public String getToken() {
    return mToken;
  }

  public Model getUser() {
    return mUser;
  }

  public boolean isSelf(String id) {
    return isSignIn() && mUser != null && TextUtils.equals(mUser.identity, id);
  }
}
