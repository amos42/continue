package com.github.continuedev.continueeclipseextension.protocol;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class GetRepoNameParams {
	@NotNull
	private final String dir;
	
	public GetRepoNameParams(@NotNull String dir) {
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
	public final GetRepoNameParams copy(@NotNull String dir) {
	   Intrinsics.checkNotNullParameter(dir, "dir");
	   return new GetRepoNameParams(dir);
	}
	
	// $FF: synthetic method
	public static GetRepoNameParams copy$default(GetRepoNameParams var0, String var1, int var2, Object var3) {
	   if ((var2 & 1) != 0) {
	      var1 = var0.dir;
	   }
	
	   return var0.copy(var1);
	}
	
	@NotNull
	public String toString() {
	   return "GetRepoNameParams(dir=" + this.dir + ')';
	}
	
	public int hashCode() {
	   return this.dir.hashCode();
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof GetRepoNameParams)) {
	      return false;
	   } else {
	      GetRepoNameParams var2 = (GetRepoNameParams)other;
	      return Intrinsics.areEqual(this.dir, var2.dir);
	   }
	}
}
