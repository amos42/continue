package com.github.continuedev.continueeclipseextension.type;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonElement;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Message {
	@NotNull
	private final String messageType;
	@NotNull
	private final String messageId;
	@NotNull
	private final JsonElement data;
	
	public Message(@NotNull String messageType, @NotNull String messageId, @NotNull JsonElement data) {
	   Intrinsics.checkNotNullParameter(messageType, "messageType");
	   Intrinsics.checkNotNullParameter(messageId, "messageId");
	   Intrinsics.checkNotNullParameter(data, "data");
	   super();
	   this.messageType = messageType;
	   this.messageId = messageId;
	   this.data = data;
	}
	
	@NotNull
	public final String getMessageType() {
	   return this.messageType;
	}
	
	@NotNull
	public final String getMessageId() {
	   return this.messageId;
	}
	
	@NotNull
	public final JsonElement getData() {
	   return this.data;
	}
	
	@NotNull
	public final String component1() {
	   return this.messageType;
	}
	
	@NotNull
	public final String component2() {
	   return this.messageId;
	}
	
	@NotNull
	public final JsonElement component3() {
	   return this.data;
	}
	
	@NotNull
	public final Message copy(@NotNull String messageType, @NotNull String messageId, @NotNull JsonElement data) {
	   Intrinsics.checkNotNullParameter(messageType, "messageType");
	   Intrinsics.checkNotNullParameter(messageId, "messageId");
	   Intrinsics.checkNotNullParameter(data, "data");
	   return new Message(messageType, messageId, data);
	}
	
	// $FF: synthetic method
	public static Message copy$default(Message var0, String var1, String var2, JsonElement var3, int var4, Object var5) {
	   if ((var4 & 1) != 0) {
	      var1 = var0.messageType;
	   }
	
	   if ((var4 & 2) != 0) {
	      var2 = var0.messageId;
	   }
	
	   if ((var4 & 4) != 0) {
	      var3 = var0.data;
	   }
	
	   return var0.copy(var1, var2, var3);
	}
	
	@NotNull
	public String toString() {
	   return "Message(messageType=" + this.messageType + ", messageId=" + this.messageId + ", data=" + this.data + ')';
	}
	
	public int hashCode() {
	   int result = this.messageType.hashCode();
	   result = result * 31 + this.messageId.hashCode();
	   result = result * 31 + this.data.hashCode();
	   return result;
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof Message)) {
	      return false;
	   } else {
	      Message var2 = (Message)other;
	      if (!Intrinsics.areEqual(this.messageType, var2.messageType)) {
	         return false;
	      } else if (!Intrinsics.areEqual(this.messageId, var2.messageId)) {
	         return false;
	      } else {
	         return Intrinsics.areEqual(this.data, var2.data);
	      }
	   }
	}
}