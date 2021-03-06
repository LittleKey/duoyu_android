// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: models/proto/model/model.proto at 91:1
package online.duoyu.sparkle.model.proto;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireEnum;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class Happening extends Message<Happening, Happening.Builder> {
  public static final ProtoAdapter<Happening> ADAPTER = new ProtoAdapter_Happening();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_ARTICLE_ID = "";

  public static final Long DEFAULT_DATE = 0L;

  public static final String DEFAULT_TITLE = "";

  public static final Event DEFAULT_EVENT = Event.UNKNOWN_EVENT;

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String article_id;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#INT64"
  )
  public final Long date;

  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String title;

  @WireField(
      tag = 4,
      adapter = "online.duoyu.sparkle.model.proto.Happening$Event#ADAPTER"
  )
  public final Event event;

  public Happening(String article_id, Long date, String title, Event event) {
    this(article_id, date, title, event, ByteString.EMPTY);
  }

  public Happening(String article_id, Long date, String title, Event event, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.article_id = article_id;
    this.date = date;
    this.title = title;
    this.event = event;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.article_id = article_id;
    builder.date = date;
    builder.title = title;
    builder.event = event;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Happening)) return false;
    Happening o = (Happening) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(article_id, o.article_id)
        && Internal.equals(date, o.date)
        && Internal.equals(title, o.title)
        && Internal.equals(event, o.event);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (article_id != null ? article_id.hashCode() : 0);
      result = result * 37 + (date != null ? date.hashCode() : 0);
      result = result * 37 + (title != null ? title.hashCode() : 0);
      result = result * 37 + (event != null ? event.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (article_id != null) builder.append(", article_id=").append(article_id);
    if (date != null) builder.append(", date=").append(date);
    if (title != null) builder.append(", title=").append(title);
    if (event != null) builder.append(", event=").append(event);
    return builder.replace(0, 2, "Happening{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<Happening, Builder> {
    public String article_id;

    public Long date;

    public String title;

    public Event event;

    public Builder() {
    }

    public Builder article_id(String article_id) {
      this.article_id = article_id;
      return this;
    }

    public Builder date(Long date) {
      this.date = date;
      return this;
    }

    public Builder title(String title) {
      this.title = title;
      return this;
    }

    public Builder event(Event event) {
      this.event = event;
      return this;
    }

    @Override
    public Happening build() {
      return new Happening(article_id, date, title, event, super.buildUnknownFields());
    }
  }

  public enum Event implements WireEnum {
    UNKNOWN_EVENT(0),

    LIKE_DIARY(1),

    LIKE_CORRECT(2),

    PUBLISH_DIARY(3),

    PUBLISH_CORRECT(4),

    ATTENTION_DIARY(5);

    public static final ProtoAdapter<Event> ADAPTER = ProtoAdapter.newEnumAdapter(Event.class);

    private final int value;

    Event(int value) {
      this.value = value;
    }

    /**
     * Return the constant for {@code value} or null.
     */
    public static Event fromValue(int value) {
      switch (value) {
        case 0: return UNKNOWN_EVENT;
        case 1: return LIKE_DIARY;
        case 2: return LIKE_CORRECT;
        case 3: return PUBLISH_DIARY;
        case 4: return PUBLISH_CORRECT;
        case 5: return ATTENTION_DIARY;
        default: return null;
      }
    }

    @Override
    public int getValue() {
      return value;
    }
  }

  private static final class ProtoAdapter_Happening extends ProtoAdapter<Happening> {
    ProtoAdapter_Happening() {
      super(FieldEncoding.LENGTH_DELIMITED, Happening.class);
    }

    @Override
    public int encodedSize(Happening value) {
      return (value.article_id != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.article_id) : 0)
          + (value.date != null ? ProtoAdapter.INT64.encodedSizeWithTag(2, value.date) : 0)
          + (value.title != null ? ProtoAdapter.STRING.encodedSizeWithTag(3, value.title) : 0)
          + (value.event != null ? Event.ADAPTER.encodedSizeWithTag(4, value.event) : 0)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, Happening value) throws IOException {
      if (value.article_id != null) ProtoAdapter.STRING.encodeWithTag(writer, 1, value.article_id);
      if (value.date != null) ProtoAdapter.INT64.encodeWithTag(writer, 2, value.date);
      if (value.title != null) ProtoAdapter.STRING.encodeWithTag(writer, 3, value.title);
      if (value.event != null) Event.ADAPTER.encodeWithTag(writer, 4, value.event);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public Happening decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.article_id(ProtoAdapter.STRING.decode(reader)); break;
          case 2: builder.date(ProtoAdapter.INT64.decode(reader)); break;
          case 3: builder.title(ProtoAdapter.STRING.decode(reader)); break;
          case 4: {
            try {
              builder.event(Event.ADAPTER.decode(reader));
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
    public Happening redact(Happening value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
