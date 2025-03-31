package com.github.continuedev.continueeclipseextension.protocol;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class WriteFileParams {
	@NotNull
	private final String path;
	@NotNull
	private final String contents;
	
	public WriteFileParams(@NotNull String path, @NotNull String contents) {
	   super();
	   Intrinsics.checkNotNullParameter(path, "path");
	   Intrinsics.checkNotNullParameter(contents, "contents");
	   this.path = path;
	   this.contents = contents;
	}
	
	@NotNull
	public final String getPath() {
	   return this.path;
	}
	
	@NotNull
	public final String getContents() {
	   return this.contents;
	}
	
	@NotNull
	public final String component1() {
	   return this.path;
	}
	
	@NotNull
	public final String component2() {
	   return this.contents;
	}
	
	@NotNull
	public final WriteFileParams copy(@NotNull String path, @NotNull String contents) {
	   Intrinsics.checkNotNullParameter(path, "path");
	   Intrinsics.checkNotNullParameter(contents, "contents");
	   return new WriteFileParams(path, contents);
	}
	
	// $FF: synthetic method
	public static WriteFileParams copy$default(WriteFileParams var0, String var1, String var2, int var3, Object var4) {
	   if ((var3 & 1) != 0) {
	      var1 = var0.path;
	   }
	
	   if ((var3 & 2) != 0) {
	      var2 = var0.contents;
	   }
	
	   return var0.copy(var1, var2);
	}
	
	@NotNull
	public String toString() {
	   return "WriteFileParams(path=" + this.path + ", contents=" + this.contents + ')';
	}
	
	public int hashCode() {
	   int result = this.path.hashCode();
	   result = result * 31 + this.contents.hashCode();
	   return result;
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof WriteFileParams)) {
	      return false;
	   } else {
	      WriteFileParams var2 = (WriteFileParams)other;
	      if (!Intrinsics.areEqual(this.path, var2.path)) {
	         return false;
	      } else {
	         return Intrinsics.areEqual(this.contents, var2.contents);
	      }
	   }
	}
}
