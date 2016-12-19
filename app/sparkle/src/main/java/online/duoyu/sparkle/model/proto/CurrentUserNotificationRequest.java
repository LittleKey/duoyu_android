// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: models/proto/business/notify.proto at 9:1
package online.duoyu.sparkle.model.proto;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;

import java.io.IOException;
import java.util.List;

import okio.ByteString;

public final class CurrentUserNotificationRequest extends Message<CurrentUserNotificationRequest, CurrentUserNotificationRequest.Builder> {
  public static final ProtoAdapter<CurrentUserNotificationRequest> ADAPTER = new ProtoAdapter_CurrentUserNotificationRequest();

  private static final long serialVersionUID = 0L;

  @WireField(
      tag = 1,
      adapter = "online.duoyu.sparkle.model.proto.Notification$Event#ADAPTER",
      label = WireField.Label.REPEATED)
  public final List<Notification.Event> events;

  @WireField(
      tag = 2,
      adapter = "online.duoyu.sparkle.model.proto.Cursor#ADAPTER")
  public final Cursor cursor;

  public CurrentUserNotificationRequest(List<Notification.Event> events, Cursor cursor) {
    this(events, cursor, ByteString.EMPTY);
  }

  public CurrentUserNotificationRequest(List<Notification.Event> events, Cursor cursor, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.events = Internal.immutableCopyOf("events", events);
    this.cursor = cursor;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.events = Internal.copyOf("events", events);
    builder.cursor = cursor;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof CurrentUserNotificationRequest)) return false;
    CurrentUserNotificationRequest o = (CurrentUserNotificationRequest) other;
    return unknownFields().equals(o.unknownFields()) && events.equals(o.events) && Internal.equals(cursor, o.cursor);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + events.hashCode();
      result = result * 37 + (cursor != null ? cursor.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (!events.isEmpty()) builder.append(", events=").append(events);
    if (cursor != null) builder.append(", cursor=").append(cursor);
    return builder.replace(0, 2, "CurrentUserNotificationRequest{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<CurrentUserNotificationRequest, Builder> {
    public List<Notification.Event> events;

    public Cursor cursor;

    public Builder() {
      events = Internal.newMutableList();
    }

    public Builder events(List<Notification.Event> events) {
      Internal.checkElementsNotNull(events);
      this.events = events;
      return this;
    }

    public Builder cursor(Cursor cursor) {
      this.cursor = cursor;
      return this;
    }

    @Override
    public CurrentUserNotificationRequest build() {
      return new CurrentUserNotificationRequest(events, cursor, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_CurrentUserNotificationRequest extends ProtoAdapter<CurrentUserNotificationRequest> {
    ProtoAdapter_CurrentUserNotificationRequest() {
      super(FieldEncoding.LENGTH_DELIMITED, CurrentUserNotificationRequest.class);
    }

    @Override
    public int encodedSize(CurrentUserNotificationRequest value) {
      return Notification.Event.ADAPTER.asRepeated().encodedSizeWithTag(1, value.events) + (value.cursor != null ? Cursor.ADAPTER.encodedSizeWithTag(2, value.cursor) : 0) + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, CurrentUserNotificationRequest value) throws IOException {
      Notification.Event.ADAPTER.asRepeated().encodeWithTag(writer, 1, value.events);
      if (value.cursor != null) Cursor.ADAPTER.encodeWithTag(writer, 2, value.cursor);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public CurrentUserNotificationRequest decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1; ) {
        switch (tag) {
          case 1: {
            try {
              builder.events.add(Notification.Event.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 2:
            builder.cursor(Cursor.ADAPTER.decode(reader));
            break;
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
    public CurrentUserNotificationRequest redact(CurrentUserNotificationRequest value) {
      Builder builder = value.newBuilder();
      if (builder.cursor != null) builder.cursor = Cursor.ADAPTER.redact(builder.cursor);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}