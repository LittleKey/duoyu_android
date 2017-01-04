package online.duoyu.sparkle.model.data;

import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.littlekey.base.ReadOnlyList;
import me.littlekey.network.ApiRequest;
import okio.ByteString;
import online.duoyu.sparkle.R;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.model.ModelFactory;
import online.duoyu.sparkle.model.business.CommentsResponse;
import online.duoyu.sparkle.model.business.GetCorrectsByDiaryIdRequest;
import online.duoyu.sparkle.model.proto.Comment;
import online.duoyu.sparkle.model.proto.Count;
import online.duoyu.sparkle.model.proto.Cursor;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.utils.CollectionUtils;
import online.duoyu.sparkle.utils.Const;
import online.duoyu.sparkle.utils.SparkleUtils;

/**
 * Created by littlekey on 1/4/17.
 */

public class GetCommentsByDiaryDataGenerator extends SparkleDataGenerator<CommentsResponse> {

  private Model mModel;

  public GetCommentsByDiaryDataGenerator(ApiType apiType, Bundle bundle) {
    super(apiType);
    mModel = bundle.getParcelable(Const.KEY_MODEL);
  }

  @Override
  protected ApiRequest<CommentsResponse> onCreateRequest(ApiType apiType, Map<String, String> pairs) {
    Map<String, String> body = new HashMap<>();
    body.put(Const.KEY_DIARY_IDENTITY, mModel.identity);
    return SparkleApplication.getInstance().getRequestManager().newSparkleRequest(apiType, body,
        CommentsResponse.class, mListener, mErrorListener);
  }

  @Override
  public ApiRequest<CommentsResponse> getNextRequestFromResponse(CommentsResponse response) {
    Cursor cursor = new Cursor.Builder()
        .timestamp(response.comments.get(response.comments.size() - 1).date)
        .limit(20)
        .build();
    GetCorrectsByDiaryIdRequest request = new GetCorrectsByDiaryIdRequest.Builder()
        .diary_id(mModel.identity)
        .cursor(cursor)
        .build();
    return SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(mApiType,
            ByteString.of(GetCorrectsByDiaryIdRequest.ADAPTER.encode(request)),
            CommentsResponse.class, mListener, mErrorListener);
  }

  @Override
  public boolean getHasMoreFromResponse(CommentsResponse response) {
    return response != null && response.cursor != null
        && response.cursor.has_more != null && response.cursor.has_more;
  }

  @Override
  public List<Model> getItemsFromResponse(@NonNull CommentsResponse response, ReadOnlyList<Model> roProcessedItems) {
    List<Model> models = new ArrayList<>();
    if (roProcessedItems.size() == 0) {
      CollectionUtils.add(models, mModel.newBuilder()
          .template(Model.Template.ITEM_DIARY_TITLE)
          .build());
      CollectionUtils.add(models, new Model.Builder()
          .template(Model.Template.ITEM_DIVIDER_HEADER)
          .description(SparkleUtils.formatString(R.string.all_comments, mModel.count.comments))
          .count(new Count.Builder().comments(mModel.count.comments).build())
          .build());
    }
    for (Comment comment: response.comments) {
      CollectionUtils.add(models,
          ModelFactory.createModelFromComment(comment, Model.Template.ITEM_COMMENT));
    }
    return models;
  }
}
