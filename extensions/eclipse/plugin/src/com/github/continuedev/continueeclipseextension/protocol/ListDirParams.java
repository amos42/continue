package com.github.continuedev.continueeclipseextension.protocol;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ListDirParams {
	@NotNull
	private final String dir;
	
	public ListDirParams(@NotNull String dir) {
	   super();
	   Intrinsics.checkNotNullParameter(dir, "dir");
	   this.dir = dir;
	}
	
	@NotNull
	public final String getDir() {
	   return this.dir;
	}
	
	@NotNull
	public final String component1() {
	   return this.dir;
	}
	
	@NotNull
	public final ListDirParams copy(@NotNull String dir) {
	   Intrinsics.checkNotNullParameter(dir, "dir");
	   return new ListDirParams(dir);
	}
	
	// $FF: synthetic method
	public static ListDirParams copy$default(ListDirParams var0, String var1, int var2, Object var3) {
	   if ((var2 & 1) != 0) {
	      var1 = var0.dir;
	   }
	
	   return var0.copy(var1);
	}
	
	@NotNull
	public String toString() {
	   return "ListDirParams(dir=" + this.dir + ')';
	}
	
	public int hashCode() {
	   return this.dir.hashCode();
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof ListDirParams)) {
	      return false;
	   } else {
	      ListDirParams var2 = (ListDirParams)other;
	      return Intrinsics.areEqual(this.dir, var2.dir);
	   }
	}
}
