package online.duoyu.sparkle.presenter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.reflect.InvocationTargetException;

import de.greenrobot.event.EventBus;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.model.proto.Action;
import online.duoyu.sparkle.utils.Const;
import timber.log.Timber;

/**
 * Created by littlekey on 12/25/16.
 */

public class ListenActionPresenter extends SparklePresenter {

  private View.OnAttachStateChangeListener mOnAttachStateChangeListener;

  @Override
  public void bind(final Model model) {
    final Action action = getValueByViewId(id(), model);
    if (action == null) {
      return;
    }
    switch (action.type) {
      case UPDATE_MONTH:
        if (mOnAttachStateChangeListener != null) {
          view().removeOnAttachStateChangeListener(mOnAttachStateChangeListener);
        }
        mOnAttachStateChangeListener = new View.OnAttachStateChangeListener() {
          @Override
          public void onViewAttachedToWindow(View v) {
            RecyclerView.LayoutManager layoutManager = group().pageContext.layoutManager;
            if (layoutManager instanceof LinearLayoutManager) {
              if (((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition()
                  == group().holder.getAdapterPosition()) {
                postEvent(action.clazz, model);
              }
            }
          }

          @Override
          public void onViewDetachedFromWindow(View v) {
            RecyclerView.LayoutManager layoutManager = group().pageContext.layoutManager;
            if (layoutManager instanceof LinearLayoutManager) {
              if (((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition()
                  > group().holder.getAdapterPosition()) {
                postEvent(action.clazz, model);
              }
            }
          }
        };
        view().addOnAttachStateChangeListener(mOnAttachStateChangeListener);
        break;
    }
  }

  private void postEvent(String class_name, Model model) {
    try {
      postEvent(Class.forName(class_name), model);
    } catch (ClassNotFoundException e) {
      Timber.e(e, "ClassNotFoundException in ActionPresenter");
    }
  }

  private void postEvent(Class<?> clazz, Model model) {
    try {
      EventBus.getDefault().post(clazz.getDeclaredConstructor(Model.class).newInstance(model));
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

  private Action getValueByViewId(int id, final Model model) {
    switch (id) {
      case 0:
        return model.actions.get(Const.ACTION_UPDATE_MONTH);
    }
    return null;
  }

  @Override
  public void unbind() {
    if (mOnAttachStateChangeListener != null) {
      view().removeOnAttachStateChangeListener(mOnAttachStateChangeListener);
    }
    super.unbind();
  }
}
