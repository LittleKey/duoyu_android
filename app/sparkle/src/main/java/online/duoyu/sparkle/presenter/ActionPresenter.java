package online.duoyu.sparkle.presenter;

import android.os.Bundle;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.squareup.wire.Wire;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import me.littlekey.base.utils.CollectionUtils;
import me.littlekey.mvp.widget.MvpRecyclerView;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.activity.LoginActivity;
import online.duoyu.sparkle.event.OnSelectEvent;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.model.proto.Action;
import online.duoyu.sparkle.model.proto.Count;
import online.duoyu.sparkle.model.proto.Flag;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.NavigationManager;
import rx.functions.Action1;
import timber.log.Timber;

/**
 * Created by littlekey on 12/24/16.
 */

public class ActionPresenter extends SparklePresenter {

  @Override
  public void bind(final Model model) {
    final Action action = getValueByViewId(id(), model);
    if (action == null) {
      return;
    }
    RxView.clicks(view())
        .throttleFirst(1, TimeUnit.SECONDS)
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
            }
          }
        });
  }

  private void logout() {
    SparkleApplication.getInstance().getAccountManager().logout();
  }

  private void liked(Model model) {
//    final boolean isUnlike = model.identity.equals(Const.FAV_DEL);
//    final ProgressDialog dialog = new ProgressDialog(group().context);
//    dialog.show();
//    Map<String, String> query = new HashMap<>();
//    query.put(Const.KEY_GID, gid);
//    query.put(Const.KEY_T, token);
//    query.put(Const.KEY_ACT, Const.ADD_FAV);
//    Map<String, String> params = new HashMap<>();
//    params.put(Const.KEY_FAV_NOTE, Const.EMPTY_STRING);
//    params.put(Const.KEY_UPDATE, Const.ONE);
//    params.put(Const.KEY_FAV_CAT, model.identity);
//    params.put(Const.KEY_APPLY, model.description);
//    SparkleRequest request = EarthApplication.getInstance().getRequestManager()
//        .newEarthRequest(ApiType.LIKED, Request.Method.POST, new Response.Listener<EarthResponse>() {
//          @Override
//          public void onResponse(EarthResponse response) {
//            EventBus.getDefault().post(new OnLikedEvent(gid, !isUnlike));
//            ToastUtils.toast(isUnlike ? R.string.unlike_succeed : R.string.like_succeed);
//            dialog.dismiss();
//          }
//        }, new Response.ErrorListener() {
//          @Override
//          public void onErrorResponse(VolleyError error) {
//            EventBus.getDefault().post(new OnLikedEvent(gid, isUnlike));
//            ToastUtils.toast(isUnlike ? R.string.unlike_error : R.string.like_succeed);
//            dialog.dismiss();
//          }
//        });
//    request.setTag(this);
//    request.setQuery(query);
//    request.setParams(params);
//    request.submit();
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
      case R.id.like:
        return model.actions.get(Const.ACTION_LIKED);
      case R.id.comments:
        return model.actions.get(Const.ACTION_COMMENTS);
      case R.id.corrects:
        return model.actions.get(Const.ACTION_CORRECTS);
      case R.id.edit_correct:
        return model.actions.get(Const.ACTION_EDIT_CORRECT);
      case R.id.user:
        return model.actions.get(Const.ACTION_USER);
    }
    return null;
  }

  @Override
  public void unbind() {
    SparkleApplication.getInstance().getRequestManager().cancel(this);
    super.unbind();
  }
}
