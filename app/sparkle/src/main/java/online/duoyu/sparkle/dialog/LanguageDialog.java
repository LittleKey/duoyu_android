package online.duoyu.sparkle.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import me.littlekey.base.utils.CollectionUtils;
import me.littlekey.mvp.widget.MvpRecyclerView;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.adapter.OfflineListAdapter;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.model.proto.Flag;
import online.duoyu.sparkle.model.proto.Language;
import online.duoyu.sparkle.utils.Colorful;

/**
 * Created by littlekey on 1/26/17.
 */

public class LanguageDialog extends Dialog {

  public LanguageDialog(Context context, Language[] languages) {
    super(context, Colorful.getThemeDelegate().getDialogStyle());
    setContentView(R.layout.dialog_languages);
    List<Model> models = new ArrayList<>();
    for (Language language: languages) {
      CollectionUtils.add(models, new Model.Builder()
          .type(Model.Type.LANGUAGE)
          .template(Model.Template.ITEM_LANGUAGE)
          .description(language.name()) // TODO: locally
          .flag(new Flag.Builder().is_selected(false).build())
          .build());
    }
    MvpRecyclerView recyclerView = (MvpRecyclerView) findViewById(R.id.languages);
    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
    recyclerView.setLayoutManager(layoutManager);
    OfflineListAdapter adapter = new OfflineListAdapter();
    adapter.setData(models);
    recyclerView.setAdapter(adapter);
  }
}
