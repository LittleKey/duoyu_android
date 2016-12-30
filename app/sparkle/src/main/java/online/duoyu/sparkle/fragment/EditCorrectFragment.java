package online.duoyu.sparkle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.jakewharton.rxbinding.support.v4.view.RxViewPager;
import com.jakewharton.rxbinding.view.RxView;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.Wire;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import okio.ByteString;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.event.OnEditCorrectTextChangeEvent;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.model.ModelFactory;
import online.duoyu.sparkle.model.business.CorrectRequest;
import online.duoyu.sparkle.model.business.CorrectResponse;
import online.duoyu.sparkle.model.business.GetCorrectByDiaryIdResponse;
import online.duoyu.sparkle.model.proto.Correct;
import online.duoyu.sparkle.model.proto.RPCResponse;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.network.SparkleRequest;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.SparkleUtils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.functions.Func1;
import rx.internal.util.ActionSubscriber;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by littlekey on 12/27/16.
 */

public class EditCorrectFragment extends BaseFragment {

  private ViewPager mViewPager;
  private List<String> mCorrectContent;
  private Model mModel;
  private FragmentStatePagerAdapter mPagerAdapter;
  private TextView mSentenceNumberView;
  private TextView mSend;
  private String mCorrectId;

  public static EditCorrectFragment newInstance(Model model) {
    Bundle args = new Bundle();
    args.putParcelable(Const.KEY_MODEL, model);
    EditCorrectFragment fragment = new EditCorrectFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_edit_correct, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mCorrectId = null;
    mSentenceNumberView = (TextView) view.findViewById(R.id.sentence_number);
    mSend = (TextView) view.findViewById(R.id.send);
    mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
    mPagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
      @Override
      public Fragment getItem(int position) {
        return EditCorrectChildFragment.newInstance(mModel, mCorrectContent.get(position), position);
      }

      @Override
      public int getCount() {
        return mCorrectContent.size();
      }
    };
    mModel = getArguments().getParcelable(Const.KEY_MODEL);
    if (mModel == null) {
      return;
    }
    mSentenceNumberView.setText(String.valueOf(1));
    RxViewPager.pageScrollStateChanges(mViewPager)
        .compose(this.<Integer>bindToLifecycle())
        .filter(new Func1<Integer, Boolean>() {
          @Override
          public Boolean call(Integer integer) {
            return integer == ViewPager.SCROLL_STATE_IDLE;
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Integer>() {
          @Override
          public void call(Integer integer) {
            mSentenceNumberView.setText(String.valueOf(mViewPager.getCurrentItem() + 1));
          }
        });
    RxView.clicks(mSend)
        .compose(this.<Void>bindToLifecycle())
        .throttleFirst(1, TimeUnit.SECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Void>() {
          @Override
          public void call(Void aVoid) {
            mSend.setEnabled(false);
            List<String> correct_content = new ArrayList<>();
            for (String content: mCorrectContent) {
              correct_content.add(Wire.get(content, Const.EMPTY_STRING));
            }
            RequestFuture<CorrectResponse> future = RequestFuture.newFuture();
            Correct correct = new Correct.Builder()
                .diary(mModel.diary)
                .correct_id(mCorrectId)
                .content(correct_content)
                .build();
            SparkleRequest<CorrectResponse> request = SparkleApplication.getInstance().getRequestManager()
                .newSparkleRequest(ApiType.CORRECT, mCorrectId == null ? Request.Method.PUT : Request.Method.PATCH,
                    ByteString.of(new CorrectRequest.Builder().correct(correct).build().encode()),
                    CorrectResponse.class, future, future);
            request.setTag(EditCorrectFragment.this);
            request.submit();
            Observable.from(future, Schedulers.newThread())
                .compose(EditCorrectFragment.this.<CorrectResponse>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ActionSubscriber<>(new Action1<CorrectResponse>() {
                  @Override
                  public void call(CorrectResponse correctResponse) {
                    getActivity().finish();
                  }
                }, new Action1<Throwable>() {
                  @Override
                  public void call(Throwable throwable) {
                    try {
                      if (throwable instanceof VolleyError) {
                        RPCResponse pbRPCResponse = RPCResponse.ADAPTER.decode(((VolleyError) throwable).networkResponse.data);
                        CorrectResponse correctResponse = CorrectResponse.ADAPTER.decode(Wire.get(pbRPCResponse.content, ByteString.EMPTY));
                        Timber.e(throwable, "error in send edit correct: " + correctResponse);
                      }
                    } catch (IOException ignore) {}
                    mSend.setEnabled(true);
                  }
                }, Actions.empty()));
          }
        });
    mCorrectContent = new ArrayList<>(Arrays.asList(new String[mModel.content.size()]));
    RequestFuture<GetCorrectByDiaryIdResponse> future = RequestFuture.newFuture();
    Map<String, String> body = new HashMap<>();
    body.put(Const.KEY_DIARY_IDENTITY, mModel.identity);
    SparkleRequest<GetCorrectByDiaryIdResponse> request = SparkleApplication.getInstance()
        .getRequestManager().newSparkleRequest(ApiType.GET_CORRECT_BY_DIARY_AND_USER, body,
            GetCorrectByDiaryIdResponse.class, future, future);
    request.setShouldCache(false);
    request.setTag(this);
    request.submit();
    Observable.from(future, Schedulers.newThread())
        .compose(this.<GetCorrectByDiaryIdResponse>bindToLifecycle())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ActionSubscriber<>(new Action1<GetCorrectByDiaryIdResponse>() {
          @Override
          public void call(GetCorrectByDiaryIdResponse getCorrectByDiaryIdResponse) {
            if (Wire.get(getCorrectByDiaryIdResponse.success, false)) {
              Model my_correct_model = ModelFactory.createModelFromCorrect(
                  getCorrectByDiaryIdResponse.correct, Model.Template.DATA);
              if (my_correct_model != null) {
                mCorrectId = my_correct_model.identity;
                mCorrectContent.clear();
                mCorrectContent.addAll(my_correct_model.content);
              }
            }
            mViewPager.setAdapter(mPagerAdapter);
            mViewPager.setOffscreenPageLimit(mCorrectContent.size() - 1);
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, SparkleUtils.formatString("get correct from diary '%s' error", mModel.identity));
            if (throwable instanceof VolleyError) {
              try {
                RPCResponse pbRPCResponse = RPCResponse.ADAPTER.decode(
                    ((VolleyError) throwable).networkResponse.data);
                GetCorrectByDiaryIdResponse response = GetCorrectByDiaryIdResponse.ADAPTER.decode(
                    Wire.get(pbRPCResponse.content, ByteString.EMPTY));
                Timber.e(response.errno.name());
              } catch (IOException ignore) {}
            }
            mViewPager.setAdapter(mPagerAdapter);
          }
        }, Actions.empty()));
    EventBus.getDefault().register(this);
  }

  @Override
  public void onDestroyView() {
    EventBus.getDefault().unregister(this);
    super.onDestroyView();
  }

  public void onEventMainThread(OnEditCorrectTextChangeEvent event) {
    if (mModel != null && TextUtils.equals(event.diary_id, mModel.identity)
        && event.position > -1 && event.position < mCorrectContent.size()) {
      mCorrectContent.set(event.position, event.content);
    }
  }
}
