package com.github.continuedev.continueeclipseextension.protocol;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ShowVirtualFileParams {
	@NotNull
	private final String name;
	@NotNull
	private final String content;
	
	public ShowVirtualFileParams(@NotNull String name, @NotNull String content) {
	   super();
	   Intrinsics.checkNotNullParameter(name, "name");
	   Intrinsics.checkNotNullParameter(content, "content");
	   this.name = name;
	   this.content = content;
	}
	
	@NotNull
	public final String getName() {
	   return this.name;
	}
	
	@NotNull
	public final String getContent() {
	   return this.content;
	}
	
	@NotNull
	public final String component1() {
	   return this.name;
	}
	
	@NotNull
	public final String component2() {
	   return this.content;
	}
	
	@NotNull
	public final ShowVirtualFileParams copy(@NotNull String name, @NotNull String content) {
	   Intrinsics.checkNotNullParameter(name, "name");
	   Intrinsics.checkNotNullParameter(content, "content");
	   return new ShowVirtualFileParams(name, content);
	}
	
	// $FF: synthetic method
	public static ShowVirtualFileParams copy$default(ShowVirtualFileParams var0, String var1, String var2, int var3, Object var4) {
	   if ((var3 & 1) != 0) {
	      var1 = var0.name;
	   }
	
	   if ((var3 & 2) != 0) {
	      var2 = var0.content;
	   }
	
	   return var0.copy(var1, var2);
	}
	
	@NotNull
	public String toString() {
	   return "ShowVirtualFileParams(name=" + this.name + ", content=" + this.content + ')';
	}
	
	public int hashCode() {
	   int result = this.name.hashCode();
	   result = result * 31 + this.content.hashCode();
	   return result;
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof ShowVirtualFileParams)) {
	      return false;
	   } else {
	      ShowVirtualFileParams var2 = (ShowVirtualFileParams)other;
	      if (!Intrinsics.areEqual(this.name, var2.name)) {
	         return false;
	      } else {
	         return Intrinsics.areEqual(this.content, var2.content);
	      }
	   }
	}
}