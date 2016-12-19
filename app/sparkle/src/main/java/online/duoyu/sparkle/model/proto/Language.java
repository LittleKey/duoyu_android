// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: models/proto/model/model.proto at 6:1
package online.duoyu.sparkle.model.proto;

import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;

public enum Language implements WireEnum {
  CHINESE(0),

  ENGLISH(1),

  JAPANESE(2);

  public static final ProtoAdapter<Language> ADAPTER = ProtoAdapter.newEnumAdapter(Language.class);

  private final int value;

  Language(int value) {
    this.value = value;
  }

  /**
   * Return the constant for {@code value} or null.
   */
  public static Language fromValue(int value) {
    switch (value) {
      case 0:
        return CHINESE;
      case 1:
        return ENGLISH;
      case 2:
        return JAPANESE;
      default:
        return null;
    }
  }

  @Override
  public int getValue() {
    return value;
  }
}