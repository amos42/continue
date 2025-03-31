package com.github.continuedev.continueeclipseextension.protocol;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FileExistsParams {
	@NotNull
	private final String filepath;
	
	public FileExistsParams(@NotNull String filepath) {
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
	public final FileExistsParams copy(@NotNull String filepath) {
	   Intrinsics.checkNotNullParameter(filepath, "filepath");
	   return new FileExistsParams(filepath);
	}
	
	// $FF: synthetic method
	public static FileExistsParams copy$default(FileExistsParams var0, String var1, int var2, Object var3) {
	   if ((var2 & 1) != 0) {
	      var1 = var0.filepath;
	   }
	
	   return var0.copy(var1);
	}
	
	@NotNull
	public String toString() {
	   return "FileExistsParams(filepath=" + this.filepath + ')';
	}
	
	public int hashCode() {
	   return this.filepath.hashCode();
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof FileExistsParams)) {
	      return false;
	   } else {
	      FileExistsParams var2 = (FileExistsParams)other;
	      return Intrinsics.areEqual(this.filepath, var2.filepath);
	   }
	}
}
