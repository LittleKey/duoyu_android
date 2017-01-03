package online.duoyu.sparkle.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

import me.littlekey.base.utils.FormatUtils;
import me.littlekey.mvp.adapter.HeaderFooterAdapter;
import me.littlekey.mvp.widget.MvpRecyclerView;
import me.littlekey.network.NameValuePair;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.adapter.ListAdapter;
import online.duoyu.sparkle.model.DataGeneratorFactory;
import online.duoyu.sparkle.model.SparkleApiList;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.widget.SparkleSwipeRefreshLayout;

/**
 * Created by littlekey on 12/20/16.
 */

public class ListFragment extends LazyLoadFragment {

  private SparkleSwipeRefreshLayout mSwipeRefreshLayout;
  private MvpRecyclerView mRecyclerView;
  private SparkleApiList<?> mList;

  public static ListFragment newInstance(Bundle bundle) {
    ListFragment fragment = new ListFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  public ListFragment setLazyLoad(boolean enable) {
    setMenuVisibility(!enable);
    setUserVisibleHint(!enable);
    return this;
  }

  @Override
  protected View lazyLoad(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(getLayout(), container, false);
    mSwipeRefreshLayout = (SparkleSwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
    mRecyclerView = (MvpRecyclerView) rootView.findViewById(R.id.recycler);
    // TODO: may should not pause load image when scroll settling for a little performance
    mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
          if (!Fresco.getImagePipeline().isPaused()) {
            Fresco.getImagePipeline().pause();
          }
        } else {
          if (Fresco.getImagePipeline().isPaused()) {
            Fresco.getImagePipeline().resume();
          }
        }
      }
    });
    ApiType apiType;
    int apiTypeNum = getArguments().getInt(Const.KEY_API_TYPE, -1);
    if (apiTypeNum >= 0 && apiTypeNum < ApiType.values().length) {
      apiType = ApiType.values()[apiTypeNum];
    } else {
      throw new IllegalStateException("Api type can not be null.");
    }
    @SuppressWarnings("unchecked") ArrayList<NameValuePair> pairList =
        (ArrayList<NameValuePair>) getArguments().getSerializable(Const.KEY_API_QUERY);
    NameValuePair[] pairs;
    if (pairList == null) {
      pairs = new NameValuePair[0];
    } else {
      pairs = pairList.toArray(new NameValuePair[pairList.size()]);
    }
    resetApi(apiType, getArguments().getStringArrayList(Const.KEY_API_PATH),
        getArguments().getBundle(Const.KEY_EXTRA), pairs);
    mSwipeRefreshLayout.setEnabled(getArguments().getBoolean(Const.KEY_ENABLE_SWIPE_REFRESH, true));
    return rootView;
  }

  @Override
  public void onDestroyView() {
    if (mList != null) {
      mList.unregisterDataLoadObservers();
    }
    super.onDestroyView();
  }

  public void insertItemAndScroll(int position, Model model) {
    if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
      ((ListAdapter) mRecyclerView.getAdapter()).insertData(position, model);
      mRecyclerView.scrollToPosition(position);
    }
  }

  public void deleteItem(Model model) {
    if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
      ((ListAdapter) mRecyclerView.getAdapter()).removeData(model);
    }
  }

  public void deleteRangeItem(Model model, int count) {
    if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
      ((ListAdapter) mRecyclerView.getAdapter()).removeRangeData(model, count);
    }
  }

  public void changeItem(Model model, int position) {
    if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
      ((ListAdapter) mRecyclerView.getAdapter()).changeData(position, model);
    }
  }

  protected List<Model> getDataSet() {
    return ((ListAdapter) mRecyclerView.getAdapter()).getData();
  }

  protected int getLayout() {
    return R.layout.fragment_list;
  }

  private void setEmptyViewCheckListener(final ListAdapter adapter) {
    adapter.setOnCheckEmptyListener(new HeaderFooterAdapter.CheckEmptyListener() {
      @Override
      public boolean onCheckEmpty(HeaderFooterAdapter.ViewData emptyView, int headerCount,
                                  int dataCount, int footerCount) {
        boolean isEmpty = super.onCheckEmpty(emptyView, headerCount, dataCount, footerCount);
        if (isEmpty) {
          for (Model model : mList.getItems()) {
            adapter.removeData(model);
          }
        }
        return isEmpty;
      }
    });
  }

  public void smoothScrollToPosition(int position) {
    if (mRecyclerView != null) {
      mRecyclerView.smoothScrollToPosition(position);
    }
  }

  public void refresh(NameValuePair... pairs) {
    mList.refresh(pairs);
  }

  public @Nullable
  String getUrl() {
    return mList == null ? null : mList.getCurrentUrl();
  }

  public void resetApi(@NonNull ApiType apiType, List<String> paths, Bundle bundle, NameValuePair... pairs) {
    mList = new SparkleApiList<>(DataGeneratorFactory.createDataGenerator(apiType, paths, bundle, pairs));
    final ListAdapter adapter = new ListAdapter(getArguments());
    mList.registerDataLoadObserver(adapter);
    mList.registerDataLoadObserver(mSwipeRefreshLayout);
    mSwipeRefreshLayout.setAdapter(adapter);
    adapter.setList(mList);
    mList.refresh();
    switch (apiType) {
      case GET_CORRECTS_BY_DIARY:
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
          @Override
          public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (parent.getChildAdapterPosition(view) == 0) {
              outRect.top = FormatUtils.dipsToPix(20);
            }
            outRect.left = FormatUtils.dipsToPix(10);
            outRect.right = FormatUtils.dipsToPix(10);
            outRect.bottom = FormatUtils.dipsToPix(20);
          }
        });
        break;
      case GET_CORRECTS_SENTENCE_BY_DIARY:
      case RECENT_DIARY:
      case FOLLOW_USER_DIARY:
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
          @Override
          public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (parent.getChildAdapterPosition(view) == 0) {
              outRect.top = FormatUtils.dipsToPix(10);
            }
            outRect.left = FormatUtils.dipsToPix(4);
            outRect.right = FormatUtils.dipsToPix(4);
            outRect.bottom = FormatUtils.dipsToPix(10);
          }
        });
        break;
    }
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setItemAnimator(null);
  }
}
