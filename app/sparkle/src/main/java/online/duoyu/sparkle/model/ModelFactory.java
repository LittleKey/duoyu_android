package online.duoyu.sparkle.model;

import online.duoyu.sparkle.model.proto.User;

/**
 * Created by littlekey on 12/19/16.
 */

public class ModelFactory {
  private ModelFactory() {
  }

  public static Model createModelFromUser(User user, Model.Template template) {
    user = DataVerifier.verify(user);
    if (user == null) {
      return null;
    }
    return new Model.Builder().type(Model.Type.USER).template(template).identity(user.user_id).build();
  }
}
