package com.github.continuedev.continueeclipseextension.type;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class RangeInFile {
	@NotNull
	private final String filepath;
	@NotNull
	private final Range range;
	
	public RangeInFile(@NotNull String filepath, @NotNull Range range) {
	   Intrinsics.checkNotNullParameter(filepath, "filepath");
	   Intrinsics.checkNotNullParameter(range, "range");
	   super();
	   this.filepath = filepath;
	   this.range = range;
	}
	
	@NotNull
	public final String getFilepath() {
	   return this.filepath;
	}
	
	@NotNull
	public final Range getRange() {
	   return this.range;
	}
	
	@NotNull
	public final String component1() {
	   return this.filepath;
	}
	
	@NotNull
	public final Range component2() {
	   return this.range;
	}
	
	@NotNull
	public final RangeInFile copy(@NotNull String filepath, @NotNull Range range) {
	   Intrinsics.checkNotNullParameter(filepath, "filepath");
	   Intrinsics.checkNotNullParameter(range, "range");
	   return new RangeInFile(filepath, range);
	}
	
	// $FF: synthetic method
	public static RangeInFile copy$default(RangeInFile var0, String var1, Range var2, int var3, Object var4) {
	   if ((var3 & 1) != 0) {
	      var1 = var0.filepath;
	   }
	
	   if ((var3 & 2) != 0) {
	      var2 = var0.range;
	   }
	
	   return var0.copy(var1, var2);
	}
	
	@NotNull
	public String toString() {
	   return "RangeInFile(filepath=" + this.filepath + ", range=" + this.range + ')';
	}
	
	public int hashCode() {
	   int result = this.filepath.hashCode();
	   result = result * 31 + this.range.hashCode();
	   return result;
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof RangeInFile)) {
	      return false;
	   } else {
	      RangeInFile var2 = (RangeInFile)other;
	      if (!Intrinsics.areEqual(this.filepath, var2.filepath)) {
	         return false;
	      } else {
	         return Intrinsics.areEqual(this.range, var2.range);
	      }
	   }
	}
}