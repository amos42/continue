package com.github.continuedev.continueeclipseextension.protocol;

import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class GetFileStatsParams {
	@NotNull
	private final List files;
	
	public GetFileStatsParams(@NotNull List files) {
	   super();
	   Intrinsics.checkNotNullParameter(files, "files");
	   this.files = files;
	}
	
	@NotNull
	public final List getFiles() {
	   return this.files;
	}
	
	@NotNull
	public final List component1() {
	   return this.files;
	}
	
	@NotNull
	public final GetFileStatsParams copy(@NotNull List files) {
	   Intrinsics.checkNotNullParameter(files, "files");
	   return new GetFileStatsParams(files);
	}
	
	// $FF: synthetic method
	public static GetFileStatsParams copy$default(GetFileStatsParams var0, List var1, int var2, Object var3) {
	   if ((var2 & 1) != 0) {
	      var1 = var0.files;
	   }
	
	   return var0.copy(var1);
	}
	
	@NotNull
	public String toString() {
	   return "GetFileStatsParams(files=" + this.files + ')';
	}
	
	public int hashCode() {
	   return this.files.hashCode();
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof GetFileStatsParams)) {
	      return false;
	   } else {
	      GetFileStatsParams var2 = (GetFileStatsParams)other;
	      return Intrinsics.areEqual(this.files, var2.files);
	   }
	}
}
