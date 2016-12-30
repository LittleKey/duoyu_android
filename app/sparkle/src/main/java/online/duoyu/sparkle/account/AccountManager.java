package online.duoyu.sparkle.account;

import android.text.TextUtils;

import de.greenrobot.event.EventBus;
import online.duoyu.sparkle.event.AccountChangeEvent;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.model.ModelFactory;
import online.duoyu.sparkle.model.proto.User;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.PreferenceUtils;

/**
 * Created by littlekey on 12/19/16.
 */

public class AccountManager {

  private boolean isSignIn;
  private Model mUser;
  private String mToken;

  public void login(User user, String token) {
    setIsSignIn(true);
    mUser = ModelFactory.createModelFromUser(user, Model.Template.DATA);
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
