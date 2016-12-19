package online.duoyu.sparkle.event;

/**
 * Created by littlekey on 12/19/16.
 */

public class AccountChangeEvent {
  private boolean mIsSignIn;

  public AccountChangeEvent(boolean mIsSignIn) {
    this.mIsSignIn = mIsSignIn;
  }

  public boolean isSignIn() {
    return mIsSignIn;
  }
}
