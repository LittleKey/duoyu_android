package online.duoyu.sparkle.model.data;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.littlekey.base.ReadOnlyList;
import me.littlekey.network.ApiRequest;
import okio.ByteString;
import online.duoyu.sparkle.SparkleApplication;
import online.duoyu.sparkle.model.Model;
import online.duoyu.sparkle.model.ModelFactory;
import online.duoyu.sparkle.model.business.CorrectsResponse;
import online.duoyu.sparkle.model.business.GetCorrectsByDiaryIdRequest;
import online.duoyu.sparkle.model.proto.Correct;
import online.duoyu.sparkle.model.proto.Count;
import online.duoyu.sparkle.model.proto.Cursor;
import online.duoyu.sparkle.network.ApiType;
import online.duoyu.sparkle.utils.CollectionUtils;
import online.duoyu.sparkle.utils.Const;

/**
 * Created by littlekey on 12/31/16.
 */

public class GetCorrectsSentenceByDiaryDataGenerator extends SparkleDataGenerator<CorrectsResponse> {

  private Model mModel;
  private int mPosition;

  public GetCorrectsSentenceByDiaryDataGenerator(ApiType apiType, Bundle bundle) {
    super(apiType);
    mModel = bundle.getParcelable(Const.KEY_MODEL);
    mPosition = bundle.getInt(Const.KEY_POSITION);
    setEnableCache(true);
  }

  @Override
  protected ApiRequest<CorrectsResponse> onCreateRequest(ApiType apiType, Map<String, String> pairs) {
    Map<String, String> body = new HashMap<>();
    body.put(Const.KEY_DIARY_IDENTITY, mModel.identity);
    return SparkleApplication.getInstance().getRequestManager().newSparkleRequest(apiType, body,
        CorrectsResponse.class, mListener, mErrorListener);
  }

  @Override
  public ApiRequest<CorrectsResponse> getNextRequestFromResponse(CorrectsResponse response) {
    Cursor cursor = new Cursor.Builder()
        .timestamp(response.corrects.get(response.corrects.size() - 1).date)
        .limit(20)
        .build();
    GetCorrectsByDiaryIdRequest request = new GetCorrectsByDiaryIdRequest.Builder()
        .diary_id(mModel.identity)
        .cursor(cursor)
        .build();
    return SparkleApplication.getInstance().getRequestManager()
        .newSparkleRequest(mApiType,
            ByteString.of(GetCorrectsByDiaryIdRequest.ADAPTER.encode(request)),
            CorrectsResponse.class, mListener, mErrorListener);
  }

  @Override
  public boolean getHasMoreFromResponse(CorrectsResponse response) {
    return response != null && response.cursor != null
        && response.cursor.has_more != null && response.cursor.has_more;
  }

  @Override
  public List<Model> getItemsFromResponse(@NonNull CorrectsResponse response, ReadOnlyList<Model> roProcessedItems) {
    List<Model> models = new ArrayList<>();
    if (roProcessedItems.size() == 0) {
      CollectionUtils.add(models, new Model.Builder()
          .template(Model.Template.ITEM_EDIT_CORRECT_HEADER)
          .count(new Count.Builder().corrects(mModel.count.corrects).build())
          .origin_sentence(mModel.content.get(mPosition))
          .build());
    }
    for (Correct correct: response.corrects) {
      Model model = ModelFactory.createModelFromCorrect(correct, Model.Template.ITEM_CORRECT_SENTENCE);
      if (model != null) {
        String correct_sentence = model.content.get(mPosition).trim();
        if (!TextUtils.isEmpty(correct_sentence)) {
          CollectionUtils.add(models, model.newBuilder()
              .correct_sentence(correct_sentence)
              .build());
        }
      }
    }
    return models;
  }
}
