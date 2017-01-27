package online.duoyu.sparkle.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import online.duoyu.sparkle.R;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.utils.Colorful;
import online.duoyu.sparkle.widget.StatefulButton;

/**
 * Created by littlekey on 1/24/17.
 */

public class LogoutDialog extends Dialog implements View.OnClickListener {

  public LogoutDialog(Context context) {
    super(context, Colorful.getThemeDelegate().getDialogStyle());
    setContentView(R.layout.dialog_logout);
    StatefulButton btn_sure = (StatefulButton) findViewById(R.id.theme_btn_sure);
    btn_sure.setState(StatefulButton.STATE_DONE);
    btn_sure.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.theme_btn_sure:
        SparkleApplication.getInstance().getAccountManager().logout();
        break;
    }
  }
}
