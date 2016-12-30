// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: models/proto/common.proto at 61:1
package online.duoyu.sparkle.model.proto;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class Flag extends Message<Flag, Flag.Builder> {
  public static final ProtoAdapter<Flag> ADAPTER = new ProtoAdapter_Flag();

  private static final long serialVersionUID = 0L;

  public static final Boolean DEFAULT_IS_ADMIN = false;

  public static final Boolean DEFAULT_IS_ME = false;

  public static final Boolean DEFAULT_IS_WINNER = false;

  public static final Boolean DEFAULT_IS_SELECTED = false;

  public static final Boolean DEFAULT_CAN_MOVE = false;

  public static final Boolean DEFAULT_LOADING = false;

  public static final Boolean DEFAULT_IS_READY = false;

  public static final Boolean DEFAULT_IN_ROOM = false;

  public static final Boolean DEFAULT_IS_MATCHING = false;

  public static final Boolean DEFAULT_IS_LIKED = false;

  public static final Boolean DEFAULT_IS_THUMBNAIL = false;

  public static final Boolean DEFAULT_IS_COMPLETED = false;

  public static final Boolean DEFAULT_IS_ATTENDING = false;

  public static final Boolean DEFAULT_IS_CORRECTED = false;

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean is_admin;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean is_me;

  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean is_winner;

  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean is_selected;

  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean can_move;

  @WireField(
      tag = 6,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean loading;

  @WireField(
      tag = 7,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean is_ready;

  @WireField(
      tag = 8,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean in_room;

  @WireField(
      tag = 9,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean is_matching;

  @WireField(
      tag = 10,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean is_liked;

  @WireField(
      tag = 11,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean is_thumbnail;

  @WireField(
      tag = 12,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean is_completed;

  @WireField(
      tag = 13,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean is_attending;

  @WireField(
      tag = 14,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean is_corrected;

  public Flag(Boolean is_admin, Boolean is_me, Boolean is_winner, Boolean is_selected, Boolean can_move, Boolean loading, Boolean is_ready, Boolean in_room, Boolean is_matching, Boolean is_liked, Boolean is_thumbnail, Boolean is_completed, Boolean is_attending, Boolean is_corrected) {
    this(is_admin, is_me, is_winner, is_selected, can_move, loading, is_ready, in_room, is_matching, is_liked, is_thumbnail, is_completed, is_attending, is_corrected, ByteString.EMPTY);
  }

  public Flag(Boolean is_admin, Boolean is_me, Boolean is_winner, Boolean is_selected, Boolean can_move, Boolean loading, Boolean is_ready, Boolean in_room, Boolean is_matching, Boolean is_liked, Boolean is_thumbnail, Boolean is_completed, Boolean is_attending, Boolean is_corrected, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.is_admin = is_admin;
    this.is_me = is_me;
    this.is_winner = is_winner;
    this.is_selected = is_selected;
    this.can_move = can_move;
    this.loading = loading;
    this.is_ready = is_ready;
    this.in_room = in_room;
    this.is_matching = is_matching;
    this.is_liked = is_liked;
    this.is_thumbnail = is_thumbnail;
    this.is_completed = is_completed;
    this.is_attending = is_attending;
    this.is_corrected = is_corrected;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.is_admin = is_admin;
    builder.is_me = is_me;
    builder.is_winner = is_winner;
    builder.is_selected = is_selected;
    builder.can_move = can_move;
    builder.loading = loading;
    builder.is_ready = is_ready;
    builder.in_room = in_room;
    builder.is_matching = is_matching;
    builder.is_liked = is_liked;
    builder.is_thumbnail = is_thumbnail;
    builder.is_completed = is_completed;
    builder.is_attending = is_attending;
    builder.is_corrected = is_corrected;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Flag)) return false;
    Flag o = (Flag) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(is_admin, o.is_admin)
        && Internal.equals(is_me, o.is_me)
        && Internal.equals(is_winner, o.is_winner)
        && Internal.equals(is_selected, o.is_selected)
        && Internal.equals(can_move, o.can_move)
        && Internal.equals(loading, o.loading)
        && Internal.equals(is_ready, o.is_ready)
        && Internal.equals(in_room, o.in_room)
        && Internal.equals(is_matching, o.is_matching)
        && Internal.equals(is_liked, o.is_liked)
        && Internal.equals(is_thumbnail, o.is_thumbnail)
        && Internal.equals(is_completed, o.is_completed)
        && Internal.equals(is_attending, o.is_attending)
        && Internal.equals(is_corrected, o.is_corrected);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (is_admin != null ? is_admin.hashCode() : 0);
      result = result * 37 + (is_me != null ? is_me.hashCode() : 0);
      result = result * 37 + (is_winner != null ? is_winner.hashCode() : 0);
      result = result * 37 + (is_selected != null ? is_selected.hashCode() : 0);
      result = result * 37 + (can_move != null ? can_move.hashCode() : 0);
      result = result * 37 + (loading != null ? loading.hashCode() : 0);
      result = result * 37 + (is_ready != null ? is_ready.hashCode() : 0);
      result = result * 37 + (in_room != null ? in_room.hashCode() : 0);
      result = result * 37 + (is_matching != null ? is_matching.hashCode() : 0);
      result = result * 37 + (is_liked != null ? is_liked.hashCode() : 0);
      result = result * 37 + (is_thumbnail != null ? is_thumbnail.hashCode() : 0);
      result = result * 37 + (is_completed != null ? is_completed.hashCode() : 0);
      result = result * 37 + (is_attending != null ? is_attending.hashCode() : 0);
      result = result * 37 + (is_corrected != null ? is_corrected.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (is_admin != null) builder.append(", is_admin=").append(is_admin);
    if (is_me != null) builder.append(", is_me=").append(is_me);
    if (is_winner != null) builder.append(", is_winner=").append(is_winner);
    if (is_selected != null) builder.append(", is_selected=").append(is_selected);
    if (can_move != null) builder.append(", can_move=").append(can_move);
    if (loading != null) builder.append(", loading=").append(loading);
    if (is_ready != null) builder.append(", is_ready=").append(is_ready);
    if (in_room != null) builder.append(", in_room=").append(in_room);
    if (is_matching != null) builder.append(", is_matching=").append(is_matching);
    if (is_liked != null) builder.append(", is_liked=").append(is_liked);
    if (is_thumbnail != null) builder.append(", is_thumbnail=").append(is_thumbnail);
    if (is_completed != null) builder.append(", is_completed=").append(is_completed);
    if (is_attending != null) builder.append(", is_attending=").append(is_attending);
    if (is_corrected != null) builder.append(", is_corrected=").append(is_corrected);
    return builder.replace(0, 2, "Flag{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<Flag, Builder> {
    public Boolean is_admin;

    public Boolean is_me;

    public Boolean is_winner;

    public Boolean is_selected;

    public Boolean can_move;

    public Boolean loading;

    public Boolean is_ready;

    public Boolean in_room;

    public Boolean is_matching;

    public Boolean is_liked;

    public Boolean is_thumbnail;

    public Boolean is_completed;

    public Boolean is_attending;

    public Boolean is_corrected;

    public Builder() {
    }

    public Builder is_admin(Boolean is_admin) {
      this.is_admin = is_admin;
      return this;
    }

    public Builder is_me(Boolean is_me) {
      this.is_me = is_me;
      return this;
    }

    public Builder is_winner(Boolean is_winner) {
      this.is_winner = is_winner;
      return this;
    }

    public Builder is_selected(Boolean is_selected) {
      this.is_selected = is_selected;
      return this;
    }

    public Builder can_move(Boolean can_move) {
      this.can_move = can_move;
      return this;
    }

    public Builder loading(Boolean loading) {
      this.loading = loading;
      return this;
    }

    public Builder is_ready(Boolean is_ready) {
      this.is_ready = is_ready;
      return this;
    }

    public Builder in_room(Boolean in_room) {
      this.in_room = in_room;
      return this;
    }

    public Builder is_matching(Boolean is_matching) {
      this.is_matching = is_matching;
      return this;
    }

    public Builder is_liked(Boolean is_liked) {
      this.is_liked = is_liked;
      return this;
    }

    public Builder is_thumbnail(Boolean is_thumbnail) {
      this.is_thumbnail = is_thumbnail;
      return this;
    }

    public Builder is_completed(Boolean is_completed) {
      this.is_completed = is_completed;
      return this;
    }

    public Builder is_attending(Boolean is_attending) {
      this.is_attending = is_attending;
      return this;
    }

    public Builder is_corrected(Boolean is_corrected) {
      this.is_corrected = is_corrected;
      return this;
    }

    @Override
    public Flag build() {
      return new Flag(is_admin, is_me, is_winner, is_selected, can_move, loading, is_ready, in_room, is_matching, is_liked, is_thumbnail, is_completed, is_attending, is_corrected, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_Flag extends ProtoAdapter<Flag> {
    ProtoAdapter_Flag() {
      super(FieldEncoding.LENGTH_DELIMITED, Flag.class);
    }

    @Override
    public int encodedSize(Flag value) {
      return (value.is_admin != null ? ProtoAdapter.BOOL.encodedSizeWithTag(1, value.is_admin) : 0)
          + (value.is_me != null ? ProtoAdapter.BOOL.encodedSizeWithTag(2, value.is_me) : 0)
          + (value.is_winner != null ? ProtoAdapter.BOOL.encodedSizeWithTag(3, value.is_winner) : 0)
          + (value.is_selected != null ? ProtoAdapter.BOOL.encodedSizeWithTag(4, value.is_selected) : 0)
          + (value.can_move != null ? ProtoAdapter.BOOL.encodedSizeWithTag(5, value.can_move) : 0)
          + (value.loading != null ? ProtoAdapter.BOOL.encodedSizeWithTag(6, value.loading) : 0)
          + (value.is_ready != null ? ProtoAdapter.BOOL.encodedSizeWithTag(7, value.is_ready) : 0)
          + (value.in_room != null ? ProtoAdapter.BOOL.encodedSizeWithTag(8, value.in_room) : 0)
          + (value.is_matching != null ? ProtoAdapter.BOOL.encodedSizeWithTag(9, value.is_matching) : 0)
          + (value.is_liked != null ? ProtoAdapter.BOOL.encodedSizeWithTag(10, value.is_liked) : 0)
          + (value.is_thumbnail != null ? ProtoAdapter.BOOL.encodedSizeWithTag(11, value.is_thumbnail) : 0)
          + (value.is_completed != null ? ProtoAdapter.BOOL.encodedSizeWithTag(12, value.is_completed) : 0)
          + (value.is_attending != null ? ProtoAdapter.BOOL.encodedSizeWithTag(13, value.is_attending) : 0)
          + (value.is_corrected != null ? ProtoAdapter.BOOL.encodedSizeWithTag(14, value.is_corrected) : 0)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, Flag value) throws IOException {
      if (value.is_admin != null) ProtoAdapter.BOOL.encodeWithTag(writer, 1, value.is_admin);
      if (value.is_me != null) ProtoAdapter.BOOL.encodeWithTag(writer, 2, value.is_me);
      if (value.is_winner != null) ProtoAdapter.BOOL.encodeWithTag(writer, 3, value.is_winner);
      if (value.is_selected != null) ProtoAdapter.BOOL.encodeWithTag(writer, 4, value.is_selected);
      if (value.can_move != null) ProtoAdapter.BOOL.encodeWithTag(writer, 5, value.can_move);
      if (value.loading != null) ProtoAdapter.BOOL.encodeWithTag(writer, 6, value.loading);
      if (value.is_ready != null) ProtoAdapter.BOOL.encodeWithTag(writer, 7, value.is_ready);
      if (value.in_room != null) ProtoAdapter.BOOL.encodeWithTag(writer, 8, value.in_room);
      if (value.is_matching != null) ProtoAdapter.BOOL.encodeWithTag(writer, 9, value.is_matching);
      if (value.is_liked != null) ProtoAdapter.BOOL.encodeWithTag(writer, 10, value.is_liked);
      if (value.is_thumbnail != null) ProtoAdapter.BOOL.encodeWithTag(writer, 11, value.is_thumbnail);
      if (value.is_completed != null) ProtoAdapter.BOOL.encodeWithTag(writer, 12, value.is_completed);
      if (value.is_attending != null) ProtoAdapter.BOOL.encodeWithTag(writer, 13, value.is_attending);
      if (value.is_corrected != null) ProtoAdapter.BOOL.encodeWithTag(writer, 14, value.is_corrected);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public Flag decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.is_admin(ProtoAdapter.BOOL.decode(reader)); break;
          case 2: builder.is_me(ProtoAdapter.BOOL.decode(reader)); break;
          case 3: builder.is_winner(ProtoAdapter.BOOL.decode(reader)); break;
          case 4: builder.is_selected(ProtoAdapter.BOOL.decode(reader)); break;
          case 5: builder.can_move(ProtoAdapter.BOOL.decode(reader)); break;
          case 6: builder.loading(ProtoAdapter.BOOL.decode(reader)); break;
          case 7: builder.is_ready(ProtoAdapter.BOOL.decode(reader)); break;
          case 8: builder.in_room(ProtoAdapter.BOOL.decode(reader)); break;
          case 9: builder.is_matching(ProtoAdapter.BOOL.decode(reader)); break;
          case 10: builder.is_liked(ProtoAdapter.BOOL.decode(reader)); break;
          case 11: builder.is_thumbnail(ProtoAdapter.BOOL.decode(reader)); break;
          case 12: builder.is_completed(ProtoAdapter.BOOL.decode(reader)); break;
          case 13: builder.is_attending(ProtoAdapter.BOOL.decode(reader)); break;
          case 14: builder.is_corrected(ProtoAdapter.BOOL.decode(reader)); break;
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
    public Flag redact(Flag value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
