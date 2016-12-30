// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: models/proto/business/correct.proto at 10:1
package online.duoyu.sparkle.model.business;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class GetCorrectByDiaryIdRequest extends Message<GetCorrectByDiaryIdRequest, GetCorrectByDiaryIdRequest.Builder> {
  public static final ProtoAdapter<GetCorrectByDiaryIdRequest> ADAPTER = new ProtoAdapter_GetCorrectByDiaryIdRequest();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_DIARY_ID = "";

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String diary_id;

  public GetCorrectByDiaryIdRequest(String diary_id) {
    this(diary_id, ByteString.EMPTY);
  }

  public GetCorrectByDiaryIdRequest(String diary_id, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.diary_id = diary_id;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.diary_id = diary_id;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GetCorrectByDiaryIdRequest)) return false;
    GetCorrectByDiaryIdRequest o = (GetCorrectByDiaryIdRequest) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(diary_id, o.diary_id);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (diary_id != null ? diary_id.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (diary_id != null) builder.append(", diary_id=").append(diary_id);
    return builder.replace(0, 2, "GetCorrectByDiaryIdRequest{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<GetCorrectByDiaryIdRequest, Builder> {
    public String diary_id;

    public Builder() {
    }

    public Builder diary_id(String diary_id) {
      this.diary_id = diary_id;
      return this;
    }

    @Override
    public GetCorrectByDiaryIdRequest build() {
      return new GetCorrectByDiaryIdRequest(diary_id, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_GetCorrectByDiaryIdRequest extends ProtoAdapter<GetCorrectByDiaryIdRequest> {
    ProtoAdapter_GetCorrectByDiaryIdRequest() {
      super(FieldEncoding.LENGTH_DELIMITED, GetCorrectByDiaryIdRequest.class);
    }

    @Override
    public int encodedSize(GetCorrectByDiaryIdRequest value) {
      return (value.diary_id != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.diary_id) : 0)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, GetCorrectByDiaryIdRequest value) throws IOException {
      if (value.diary_id != null) ProtoAdapter.STRING.encodeWithTag(writer, 1, value.diary_id);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public GetCorrectByDiaryIdRequest decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.diary_id(ProtoAdapter.STRING.decode(reader)); break;
          default: {
            FieldEncoding fieldEncoding = reader.peekFieldEncoding();
            Object value = fieldEncoding.rawProtoAdapter().decode(reader);
            builder.addUnknownField(tag, fieldEncoding, value);
          }
        }
      }
      reader.endMessage(token);
      return builder.build();
    }

    @Override
    public GetCorrectByDiaryIdRequest redact(GetCorrectByDiaryIdRequest value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
