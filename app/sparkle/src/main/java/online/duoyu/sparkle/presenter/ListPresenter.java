package online.duoyu.sparkle.presenter;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.littlekey.base.utils.FormatUtils;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.adapter.OfflineListAdapter;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.utils.CollectionUtils;
import online.duoyu.sparkle.utils.ResourcesUtils;
import online.duoyu.sparkle.widget.DividerItemDecoration;

/**
 * Created by littlekey on 1/3/17.
 */

public class ListPresenter extends SparklePresenter {

  private OfflineListAdapter mAdapter;

  @Override
  public void bind(Model model) {
    List<Model> models = new ArrayList<>();
    switch (model.template) {
      case ITEM_CORRECT_WITH_DIARY:
      case ITEM_CORRECT:
        for (int i = 0; i < model.content.size(); ++i) {
          String correct_content_sentence = model.content.get(i).trim();
          String origin_content_sentence = model.addition.content.get(i).trim();
          if (!TextUtils.isEmpty(correct_content_sentence) && !TextUtils.isEmpty(origin_content_sentence)) {
            CollectionUtils.add(models, new Model.Builder()
                .template(Model.Template.ITEM_CORRECT_WITH_ORIGIN_CONTENT_SENTENCE)
                .correct_sentence(correct_content_sentence)
                .origin_sentence(origin_content_sentence)
                .build());
          }
        }
        break;
    }
    if (view() instanceof RecyclerView) {
      if (mAdapter == null) {
        mAdapter = new OfflineListAdapter();
      }
      if (((RecyclerView) view()).getAdapter() == null) {
        ((RecyclerView) view()).setAdapter(mAdapter);
      }
      ((RecyclerView) view()).setItemAnimator(null);
      switch (model.template) {
        case ITEM_CORRECT_WITH_DIARY:
        case ITEM_CORRECT:
          if (((RecyclerView) view()).getLayoutManager() == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(view().getContext());
            ((RecyclerView) view()).setLayoutManager(layoutManager);
            ((RecyclerView) view()).addItemDecoration(new RecyclerView.ItemDecoration() {
              @Override
              public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = FormatUtils.dipsToPix(5);
                outRect.left = FormatUtils.dipsToPix(5);
                outRect.right = FormatUtils.dipsToPix(5);
                outRect.bottom = FormatUtils.dipsToPix(5);
              }
            });
            ((RecyclerView) view()).addItemDecoration(new DividerItemDecoration(
                ResourcesUtils.getDrawable(R.drawable.recycler_view_divider_line),
                FormatUtils.dipsToPix(3.5f)));
          }
          break;
      }
      mAdapter.setData(models);
    }
  }

  @Override
  public void unbind() {
    super.unbind();
  }
}
