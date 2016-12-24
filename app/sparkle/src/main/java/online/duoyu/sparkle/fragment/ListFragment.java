package online.duoyu.sparkle.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class ListFragment extends BaseFragment {

  private SparkleSwipeRefreshLayout mSwipeRefreshLayout;
  private MvpRecyclerView mRecyclerView;
  private SparkleApiList<?> mList;

  public static ListFragment newInstance(Bundle bundle) {
    ListFragment fragment = new ListFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(getLayout(), container, false);
    mSwipeRefreshLayout = (SparkleSwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
    mRecyclerView = (MvpRecyclerView) rootView.findViewById(R.id.recycler);
    return rootView;
  }

  @Override
  public void onViewCreated(final View view, final Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
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
    resetApi(apiType, getArguments().getStringArrayList(Const.KEY_API_PATH), pairs);
    mSwipeRefreshLayout.setEnabled(getArguments().getBoolean(Const.KEY_ENABLE_SWIPE_REFRESH, true));
  }

  @Override
  public void onDestroyView() {
    mList.unregisterDataLoadObservers();
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

  public void resetApi(@NonNull ApiType apiType, List<String> paths, NameValuePair... pairs) {
    mList = new SparkleApiList<>(DataGeneratorFactory.createDataGenerator(apiType, paths, pairs));
    final ListAdapter adapter = new ListAdapter(getArguments());
    mList.registerDataLoadObserver(adapter);
    mList.registerDataLoadObserver(mSwipeRefreshLayout);
    mSwipeRefreshLayout.setAdapter(adapter);
    adapter.setList(mList);
    mList.refresh();
    final RecyclerView.LayoutManager layoutManager;
    switch (apiType) {
      default:
        layoutManager = new LinearLayoutManager(getActivity());
    }
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setItemAnimator(null);
  }
}
