// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: models/proto/business/diary.proto at 50:1
package online.duoyu.sparkle.model.business;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireEnum;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class AttentionResponse extends Message<AttentionResponse, AttentionResponse.Builder> {
  public static final ProtoAdapter<AttentionResponse> ADAPTER = new ProtoAdapter_AttentionResponse();

  private static final long serialVersionUID = 0L;

  public static final Boolean DEFAULT_SUCCESS = false;

  public static final Errno DEFAULT_ERRNO = Errno.NOT_FOUND_DIARY;

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean success;

  @WireField(
      tag = 2,
      adapter = "online.duoyu.sparkle.model.business.AttentionResponse$Errno#ADAPTER"
  )
  public final Errno errno;

  public AttentionResponse(Boolean success, Errno errno) {
    this(success, errno, ByteString.EMPTY);
  }

  public AttentionResponse(Boolean success, Errno errno, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.success = success;
    this.errno = errno;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.success = success;
    builder.errno = errno;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof AttentionResponse)) return false;
    AttentionResponse o = (AttentionResponse) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(success, o.success)
        && Internal.equals(errno, o.errno);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (success != null ? success.hashCode() : 0);
      result = result * 37 + (errno != null ? errno.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (success != null) builder.append(", success=").append(success);
    if (errno != null) builder.append(", errno=").append(errno);
    return builder.replace(0, 2, "AttentionResponse{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<AttentionResponse, Builder> {
    public Boolean success;

    public Errno errno;

    public Builder() {
    }

    public Builder success(Boolean success) {
      this.success = success;
      return this;
    }

    public Builder errno(Errno errno) {
      this.errno = errno;
      return this;
    }

    @Override
    public AttentionResponse build() {
      return new AttentionResponse(success, errno, super.buildUnknownFields());
    }
  }

  public enum Errno implements WireEnum {
    NOT_FOUND_DIARY(0),

    ALREADY_ATTENTION(1);

    public static final ProtoAdapter<Errno> ADAPTER = ProtoAdapter.newEnumAdapter(Errno.class);

    private final int value;

    Errno(int value) {
      this.value = value;
    }

    /**
     * Return the constant for {@code value} or null.
     */
    public static Errno fromValue(int value) {
      switch (value) {
        case 0: return NOT_FOUND_DIARY;
        case 1: return ALREADY_ATTENTION;
        default: return null;
      }
    }

    @Override
    public int getValue() {
      return value;
    }
  }

  private static final class ProtoAdapter_AttentionResponse extends ProtoAdapter<AttentionResponse> {
    ProtoAdapter_AttentionResponse() {
      super(FieldEncoding.LENGTH_DELIMITED, AttentionResponse.class);
    }

    @Override
    public int encodedSize(AttentionResponse value) {
      return (value.success != null ? ProtoAdapter.BOOL.encodedSizeWithTag(1, value.success) : 0)
          + (value.errno != null ? Errno.ADAPTER.encodedSizeWithTag(2, value.errno) : 0)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, AttentionResponse value) throws IOException {
      if (value.success != null) ProtoAdapter.BOOL.encodeWithTag(writer, 1, value.success);
      if (value.errno != null) Errno.ADAPTER.encodeWithTag(writer, 2, value.errno);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public AttentionResponse decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.success(ProtoAdapter.BOOL.decode(reader)); break;
          case 2: {
            try {
              builder.errno(Errno.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
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
    public AttentionResponse redact(AttentionResponse value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
