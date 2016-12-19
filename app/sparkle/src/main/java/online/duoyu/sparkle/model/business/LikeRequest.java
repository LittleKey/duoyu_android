// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: models/proto/business/public.proto at 6:1
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

public final class LikeRequest extends Message<LikeRequest, LikeRequest.Builder> {
  public static final ProtoAdapter<LikeRequest> ADAPTER = new ProtoAdapter_LikeRequest();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_IDENTITY = "";

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String identity;

  public LikeRequest(String identity) {
    this(identity, ByteString.EMPTY);
  }

  public LikeRequest(String identity, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.identity = identity;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.identity = identity;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof LikeRequest)) return false;
    LikeRequest o = (LikeRequest) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(identity, o.identity);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (identity != null ? identity.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (identity != null) builder.append(", identity=").append(identity);
    return builder.replace(0, 2, "LikeRequest{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<LikeRequest, Builder> {
    public String identity;

    public Builder() {
    }

    public Builder identity(String identity) {
      this.identity = identity;
      return this;
    }

    @Override
    public LikeRequest build() {
      return new LikeRequest(identity, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_LikeRequest extends ProtoAdapter<LikeRequest> {
    ProtoAdapter_LikeRequest() {
      super(FieldEncoding.LENGTH_DELIMITED, LikeRequest.class);
    }

    @Override
    public int encodedSize(LikeRequest value) {
      return (value.identity != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.identity) : 0)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, LikeRequest value) throws IOException {
      if (value.identity != null) ProtoAdapter.STRING.encodeWithTag(writer, 1, value.identity);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public LikeRequest decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.identity(ProtoAdapter.STRING.decode(reader)); break;
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
    public LikeRequest redact(LikeRequest value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}