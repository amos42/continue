package com.github.continuedev.continueeclipseextension.protocol;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ReadFileParams {
	@NotNull
	private final String filepath;
	
	public ReadFileParams(@NotNull String filepath) {
	   super();
	   Intrinsics.checkNotNullParameter(filepath, "filepath");
	   this.filepath = filepath;
	}
	
	@NotNull
	public final String getFilepath() {
	   return this.filepath;
	}
	
	@NotNull
	public final String component1() {
	   return this.filepath;
	}
	
	@NotNull
	public final ReadFileParams copy(@NotNull String filepath) {
	   Intrinsics.checkNotNullParameter(filepath, "filepath");
	   return new ReadFileParams(filepath);
	}
	
	// $FF: synthetic method
	public static ReadFileParams copy$default(ReadFileParams var0, String var1, int var2, Object var3) {
	   if ((var2 & 1) != 0) {
	      var1 = var0.filepath;
	   }
	
	   return var0.copy(var1);
	}
	
	@NotNull
	public String toString() {
	   return "ReadFileParams(filepath=" + this.filepath + ')';
	}
	
	public int hashCode() {
	   return this.filepath.hashCode();
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof ReadFileParams)) {
	      return false;
	   } else {
	      ReadFileParams var2 = (ReadFileParams)other;
	      return Intrinsics.areEqual(this.filepath, var2.filepath);
	   }
	}
}
