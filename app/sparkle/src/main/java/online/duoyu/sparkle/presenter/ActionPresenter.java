package online.duoyu.sparkle.presenter;

import android.os.Bundle;

import com.android.volley.toolbox.RequestFuture;
import com.jakewharton.rxbinding.view.RxView;
import com.squareup.wire.Wire;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import me.littlekey.base.utils.CollectionUtils;
import me.littlekey.mvp.widget.MvpRecyclerView;
import okio.ByteString;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.activity.LoginActivity;
import online.duoyu.sparkle.dialog.LogoutDialog;
import online.duoyu.sparkle.event.OnReplyCommentEvent;
import online.duoyu.sparkle.event.OnSelectEvent;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.model.business.AttentionRequest;
import online.duoyu.sparkle.model.business.AttentionResponse;
import online.duoyu.sparkle.model.business.FollowRequest;
import online.duoyu.sparkle.model.business.FollowResponse;
import online.duoyu.sparkle.model.business.LikeRequest;
import online.duoyu.sparkle.model.business.LikeResponse;
import online.duoyu.sparkle.model.business.UnattendedRequest;
import online.duoyu.sparkle.model.business.UnattendedResponse;
import online.duoyu.sparkle.model.business.UnfollowRequest;
import online.duoyu.sparkle.model.business.UnfollowResponse;
import online.duoyu.sparkle.model.business.UnlikeRequest;
import online.duoyu.sparkle.model.business.UnlikeResponse;
import online.duoyu.sparkle.model.proto.Action;
import online.duoyu.sparkle.model.proto.Count;
import online.duoyu.sparkle.model.proto.Flag;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.network.SparkleRequest;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.NavigationManager;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.internal.util.ActionSubscriber;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by littlekey on 12/24/16.
 */

public class ActionPresenter extends SparklePresenter {

  private Subscription mSubscription;

  @Override
  public void bind(final Model model) {
    final Action action = getValueByViewId(id(), model);
    if (action == null) {
      return;
    }
    if (mSubscription != null && !mSubscription.isUnsubscribed()) {
      mSubscription.unsubscribe();
    }
    mSubscription = RxView.clicks(view())
        .throttleFirst(500, TimeUnit.MICROSECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Void>() {
          @Override
          public void call(Void aVoid) {
            switch (action.type) {
              case JUMP_WITH_LOGIN:
                if (!SparkleApplication.getInstance().getAccountManager().isSignIn()) {
                  NavigationManager.navigationTo(view().getContext(), LoginActivity.class);
                  break;
                }
              case JUMP:
                if (null != action.clazz) {
                  try {
                    Class<?> clazz = Class.forName(action.clazz);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Const.KEY_MODEL, model);
                    NavigationManager.navigationTo(view().getContext(), clazz, bundle);
                  } catch (ClassNotFoundException e) {
                    Timber.e(e, "ClassNotFoundException in ActionPresenter");
                  }
                }
                break;
              case LOGOUT:
                logout();
                break;
              case EVENT:
                if (action.clazz != null) {
                  try {
                    Class<?> clazz = Class.forName(action.clazz);
                    EventBus.getDefault().post(
                        clazz.getDeclaredConstructor(Model.class).newInstance(model));
                  } catch (ClassNotFoundException e) {
                    Timber.e(e, "ClassNotFoundException in ActionPresenter");
                  } catch (IllegalAccessException e) {
                    Timber.e(e, "IllegalAccessException in ActionPresenter");
                  } catch (NoSuchMethodException e) {
                    Timber.e(e, "NoSuchMethodException in ActionPresenter");
                  } catch (InstantiationException e) {
                    Timber.e(e, "InstantiationException in ActionPresenter");
                  } catch (InvocationTargetException e) {
                    Timber.e(e, "InvocationTargetException in ActionPresenter");
                  }
                }
                break;
              case LIKED:
                liked(model);
                break;
              case ATTENTION:
                attention(model);
                break;
              case FOLLOW:
                follow(model);
                break;
              case COMMENT:
                comment(model);
                break;
            }
          }
        });
  }

  private void logout() {
    new LogoutDialog(view().getContext()).show();
  }

  private void attention(Model model) {
    view().setEnabled(false);
    // TODO : check diary author whether is me
    boolean should_attending = !Wire.get(model.flag.is_attending, false);
    group().bind(model.newBuilder()
        .flag(model.flag.newBuilder().is_attending(should_attending).build())
        .count(model.count.newBuilder()
            .attentions(Math.max(Wire.get(model.count.attentions, 0) + (should_attending ? 1 : -1), 0)).build())
        .build());
    if (should_attending) {
      _attention_imp(model);
    } else {
      _unattended_imp(model);
    }
  }

  private void _attention_imp(final Model model) {
    AttentionRequest attentionRequest = new AttentionRequest.Builder().diary_id(model.identity).build();
    RequestFuture<AttentionResponse> future = RequestFuture.newFuture();
    SparkleRequest<AttentionResponse> request = SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(ApiType.ATTENTION_DIARY,
            ByteString.of(AttentionRequest.ADAPTER.encode(attentionRequest)),
            AttentionResponse.class, future, future);
    request.setTag(this);
    request.submit();
    Observable.from(future, Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ActionSubscriber<>(new Action1<AttentionResponse>() {
          @Override
          public void call(AttentionResponse attentionResponse) {
            if (!Wire.get(attentionResponse.success, false)) {
              // NOTE: attention error restore model
              group().bind(model);
            }
            view().setEnabled(true);
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "attention diary error");
            // NOTE: attention error restore model
            group().bind(model);
            view().setEnabled(true);
          }
        }, Actions.empty()));
  }

  private void _unattended_imp(final Model model) {
    UnattendedRequest unattendedRequest = new UnattendedRequest.Builder().diary_id(model.identity).build();
    RequestFuture<UnattendedResponse> future = RequestFuture.newFuture();
    SparkleRequest<UnattendedResponse> request = SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(ApiType.UNATTENDED_DIARY,
            ByteString.of(UnattendedRequest.ADAPTER.encode(unattendedRequest)),
            UnattendedResponse.class, future, future);
    request.setTag(this);
    request.submit();
    Observable.from(future, Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ActionSubscriber<>(new Action1<UnattendedResponse>() {
          @Override
          public void call(UnattendedResponse unattendedRequest) {
            if (!Wire.get(unattendedRequest.success, false)) {
              // NOTE: unattended error restore model
              group().bind(model);
            }
            view().setEnabled(true);
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "unattended diary error");
            // NOTE: unattended error restore model
            group().bind(model);
            view().setEnabled(true);
          }
        }, Actions.empty()));
  }

  private void follow(Model model) {
    view().setEnabled(false);
    boolean should_follow = !Wire.get(model.flag.is_following, false);
    Model newModel = model.newBuilder()
        .flag(model.flag.newBuilder().is_following(should_follow).build())
        .count(model.count.newBuilder()
            .followers(model.count.followers + (should_follow ? 1 : -1)).build())
        .build();
    @SuppressWarnings("unchecked")
    MvpRecyclerView.Adapter<Model> adapter = group().pageContext.adapter;
    if (adapter != null) {
      adapter.changeData(adapter.indexOf(model), newModel);
    } else {
      group().bind(newModel);
    }
    if (should_follow) {
      _follow_imp(model, newModel);
    } else {
      _unfollow_imp(model, newModel);
    }
  }

  private void _follow_imp(final Model oldModel, final Model newModel) {
    @SuppressWarnings("unchecked")
    final MvpRecyclerView.Adapter<Model> adapter = group().pageContext.adapter;
    FollowRequest followRequest = new FollowRequest.Builder()
        .user_id(oldModel.identity)
        .build();
    RequestFuture<FollowResponse> future = RequestFuture.newFuture();
    SparkleRequest<FollowResponse> request = SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(ApiType.FOLLOW, ByteString.of(FollowRequest.ADAPTER.encode(followRequest)),
            FollowResponse.class, future, future);
    request.setTag(this);
    request.submit();
    Observable.from(future, Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ActionSubscriber<>(new Action1<FollowResponse>() {
          @Override
          public void call(FollowResponse followResponse) {
            if (!Wire.get(followResponse.success, false)) {
              if (adapter != null) {
                adapter.changeData(adapter.indexOf(newModel), oldModel);
              } else {
                group().bind(oldModel);
              }
            }
            view().setEnabled(true);
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            if (adapter != null) {
              adapter.changeData(adapter.indexOf(newModel), oldModel);
            } else {
              group().bind(oldModel);
            }
            view().setEnabled(true);
          }
        }, Actions.empty()));
  }

  private void _unfollow_imp(final Model oldModel, final Model newModel) {
    @SuppressWarnings("unchecked")
    final MvpRecyclerView.Adapter<Model> adapter = group().pageContext.adapter;
    UnfollowRequest unfollowRequest = new UnfollowRequest.Builder()
        .user_id(oldModel.identity)
        .build();
    RequestFuture<UnfollowResponse> future = RequestFuture.newFuture();
    SparkleRequest<UnfollowResponse> request = SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(ApiType.UNFOLLOW, ByteString.of(UnfollowRequest.ADAPTER.encode(unfollowRequest)),
            UnfollowResponse.class, future, future);
    request.setTag(this);
    request.submit();
    Observable.from(future, Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ActionSubscriber<>(new Action1<UnfollowResponse>() {
          @Override
          public void call(UnfollowResponse unfollowResponse) {
            if (!Wire.get(unfollowResponse.success, false)) {
              if (adapter != null) {
                adapter.changeData(adapter.indexOf(newModel), oldModel);
              }
            }
            view().setEnabled(true);
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            if (adapter != null) {
              adapter.changeData(adapter.indexOf(newModel), oldModel);
            }
            view().setEnabled(true);
          }
        }, Actions.empty()));
  }

  private void liked(Model model) {
    view().setEnabled(false);
    boolean should_liked = !Wire.get(model.flag.is_liked, false);
    Model newModel = model.newBuilder()
        .flag(model.flag.newBuilder().is_liked(should_liked).build())
        .count(model.count.newBuilder()
            .likes(Wire.get(model.count.likes, 0) + (should_liked ? 1 : -1)).build())
        .build();
    @SuppressWarnings("unchecked")
    MvpRecyclerView.Adapter<Model> adapter = group().pageContext.adapter;
    if (adapter != null) {
      adapter.changeData(adapter.indexOf(model), newModel);
    } else {
      group().bind(newModel);
    }
    if (should_liked) {
      _like_imp(model, newModel);
    } else {
      _unlike_imp(model, newModel);
    }
  }

  private void _like_imp(final Model oldModel, final Model newModel) {
    ApiType apiType;
    switch (newModel.type) {
      case DIARY:
        apiType = ApiType.LIKE_DIARY;
        break;
      case CORRECT:
        apiType = ApiType.LIKE_CORRECT;
        break;
      case COMMENT:
        apiType = ApiType.LIKE_COMMENT;
        break;
      default:
        Timber.e("error type: " + newModel.type);
        return;
    }
    @SuppressWarnings("unchecked")
    final MvpRecyclerView.Adapter<Model> adapter = group().pageContext.adapter;
    LikeRequest likeRequest = new LikeRequest.Builder()
        .identity(newModel.identity)
        .build();
    RequestFuture<LikeResponse> future = RequestFuture.newFuture();
    SparkleRequest<LikeResponse> request = SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(apiType, ByteString.of(LikeRequest.ADAPTER.encode(likeRequest)),
            LikeResponse.class, future, future);
    request.setTag(this);
    request.submit();
    Observable.from(future, Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ActionSubscriber<>(new Action1<LikeResponse>() {
          @Override
          public void call(LikeResponse likeResponse) {
            if (!Wire.get(likeResponse.success, false)) {
              // NOTE: like error restore model
              if (adapter != null) {
                adapter.changeData(adapter.indexOf(newModel), oldModel);
              } else {
                group().bind(oldModel);
              }
            }
            view().setEnabled(true);
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "like article error");
            // NOTE: like error restore model
            if (adapter != null) {
              adapter.changeData(adapter.indexOf(newModel), oldModel);
            } else {
              group().bind(oldModel);
            }
            view().setEnabled(true);
          }
        }, Actions.empty()));
  }

  private void _unlike_imp(final Model oldModel, final Model newModel) {
    ApiType apiType;
    switch (newModel.type) {
      case DIARY:
        apiType = ApiType.UNLIKE_DIARY;
        break;
      case CORRECT:
        apiType = ApiType.UNLIKE_CORRECT;
        break;
      case COMMENT:
        apiType = ApiType.UNLIKE_COMMENT;
        break;
      default:
        Timber.e("error type: " + newModel.type);
        return;
    }
    @SuppressWarnings("unchecked")
    final MvpRecyclerView.Adapter<Model> adapter = group().pageContext.adapter;
    UnlikeRequest unlikeRequest = new UnlikeRequest.Builder()
        .identity(newModel.identity)
        .build();
    RequestFuture<UnlikeResponse> future = RequestFuture.newFuture();
    SparkleRequest<UnlikeResponse> request = SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(apiType, ByteString.of(UnlikeRequest.ADAPTER.encode(unlikeRequest)),
            UnlikeResponse.class, future, future);
    request.setTag(this);
    request.submit();
    Observable.from(future, Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ActionSubscriber<>(new Action1<UnlikeResponse>() {
          @Override
          public void call(UnlikeResponse unlikeResponse) {
            if (!Wire.get(unlikeResponse.success, false)) {
              // NOTE: like error restore model
              if (adapter != null) {
                adapter.changeData(adapter.indexOf(newModel), oldModel);
              } else {
                group().bind(oldModel);
              }
            }
            view().setEnabled(true);
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "like article error");
            // NOTE: like error restore model
            if (adapter != null) {
              adapter.changeData(adapter.indexOf(newModel), oldModel);
            } else {
              group().bind(oldModel);
            }
            view().setEnabled(true);
          }
        }, Actions.empty()));
  }

  private void comment(Model model) {
    EventBus.getDefault().post(new OnReplyCommentEvent(model));
  }

  @SuppressWarnings("unchecked")
  private void select(Model model, boolean updateParent) {
    boolean willSelect = model.flag == null || !Wire.get(model.flag.is_selected, false);
    MvpRecyclerView.Adapter adapter = group().pageContext.adapter;
    Flag flag = Wire.get(model.flag, new Flag.Builder().build()).newBuilder()
        .is_selected(willSelect).build();
    Model newModel = model.newBuilder().flag(flag).build();
    if (updateParent) {
      updateParentSubModels(adapter, model, newModel);
    }
    adapter.changeData(group().holder.getAdapterPosition(), newModel);
    EventBus.getDefault().post(new OnSelectEvent(willSelect, model));
  }

  @SuppressWarnings("unchecked")
  private void check(Model model) {
    MvpRecyclerView.Adapter adapter = group().pageContext.adapter;
    Flag flag =
        Wire.get(model.flag, new Flag.Builder().build()).newBuilder().is_selected(true)
            .build();
    Model newModel = model.newBuilder().flag(flag).build();
    for (int i = 0; i < adapter.getData().size(); i++) {
      Object item = adapter.getItem(i);
      if (item instanceof Model && (flag = ((Model) item).flag) != null
          && Wire.get(flag.is_selected, false) && !item.equals(model)) {
        flag = flag.newBuilder().is_selected(false).build();
        adapter.changeData(i, ((Model) item).newBuilder().flag(flag).build());
        break;
      }
    }
    adapter.changeData(group().holder.getAdapterPosition(), newModel);
    EventBus.getDefault().post(new OnSelectEvent(true, model));
  }

  @SuppressWarnings("unchecked")
  private void updateParentSubModels(MvpRecyclerView.Adapter adapter, Model model, Model newModel) {
    int parentPosition = findParentModelPosition(model);
    if (parentPosition != -1) {
      Model parent = (Model) adapter.getItem(parentPosition);
      List<Model> subModels = new ArrayList<>(parent.subModels);
      subModels.set(subModels.indexOf(model), newModel);
      int selectedNum = 0;
      for (Model sub : subModels) {
        Flag flag = sub.flag;
        if (!CollectionUtils.isEmpty(sub.subModels)) {
          selectedNum += sub.count != null ? Wire.get(sub.count.selected_num, 0) : 0;
        } else if (flag != null && Wire.get(flag.is_selected, false)) {
          ++selectedNum;
        }
      }
      Count count = new Count.Builder().selected_num(selectedNum).build();
      Model newParent = parent.newBuilder().subModels(subModels).count(count).build();
      updateParentSubModels(adapter, parent, newParent);
      adapter.changeData(parentPosition, newParent);
    }
  }

  @SuppressWarnings({"unchecked", "unused"})
  private void expand(Model model, boolean shouldExpand) {
    MvpRecyclerView.Adapter adapter = group().pageContext.adapter;
    // int position = group().holder.getAdapterPosition();
    int position = adapter.indexOf(model);
    if (shouldExpand != (model.flag != null && Wire.get(model.flag.is_selected, false))) {
      Model newModel = model.newBuilder().flag(
          new Flag.Builder().is_selected(shouldExpand).build()).build();
      updateParentSubModels(adapter, model, newModel);
      adapter.changeData(position, newModel);
      if (shouldExpand) {
        adapter.insertData(position + 1, model.subModels);
      } else {
        for (int i = 0; i < model.subModels.size(); ++i) {
          Model item = (Model) adapter.getItem(position + 1);
          if (!CollectionUtils.isEmpty(item.subModels)) {
            expand(item, false);
            // NOTE : update data after adapter item change
            item = (Model) adapter.getItem(position + 1);
          }
          adapter.removeData(item);
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  private int findParentModelPosition(Model model) {
    MvpRecyclerView.Adapter adapter = group().pageContext.adapter;
    int position = adapter.indexOf(model);
    for (int i = position - 1; i >= 0; --i) {
      Model item = (Model) adapter.getItem(i);
      if (!CollectionUtils.isEmpty(item.subModels) && item.subModels.contains(model)) {
        return i;
      }
    }
    return -1;
  }

  private Action getValueByViewId(int id, final Model model) {
    switch (id) {
      case 0:
        //      case R.id.mask:
        return model.actions.get(Const.ACTION_MAIN);
      case R.id.attentions:
        return model.actions.get(Const.ACTION_ATTENTIONS);
      case R.id.likes:
        return model.actions.get(Const.ACTION_LIKED);
      case R.id.comments:
        return model.actions.get(Const.ACTION_COMMENTS);
      case R.id.corrects:
        return model.actions.get(Const.ACTION_CORRECTS);
      case R.id.edit_correct:
        return model.actions.get(Const.ACTION_EDIT_CORRECT);
      case R.id.user:
        return model.actions.get(Const.ACTION_USER);
      case R.id.theme_btn_follow:
        return model.actions.get(Const.ACTION_FOLLOW);
      case R.id.published_diaries:
        return model.actions.get(Const.ACTION_PUBLISHED_DIARIES);
      case R.id.attending_diaries:
        return model.actions.get(Const.ACTION_ATTENDING_DIARIES);
      case R.id.published_corrects:
        return model.actions.get(Const.ACTION_PUBLISHED_CORRECTS);
      case R.id.btn_logout:
        return model.actions.get(Const.ACTION_LOGOUT);
    }
    return null;
  }

  @Override
  public void unbind() {
    SparkleApplication.getInstance().getRequestManager().cancel(this);
    if (mSubscription != null && !mSubscription.isUnsubscribed()) {
      mSubscription.unsubscribe();
    }
    super.unbind();
  }
}
