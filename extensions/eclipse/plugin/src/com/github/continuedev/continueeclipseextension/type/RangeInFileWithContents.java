package com.github.continuedev.continueeclipseextension.type;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class RangeInFileWithContents {
	@NotNull
	private final String filepath;
	@NotNull
	private final Range range;
	@NotNull
	private final String contents;
	
	public RangeInFileWithContents(@NotNull String filepath, @NotNull Range range, @NotNull String contents) {
	   Intrinsics.checkNotNullParameter(filepath, "filepath");
	   Intrinsics.checkNotNullParameter(range, "range");
	   Intrinsics.checkNotNullParameter(contents, "contents");
	   super();
	   this.filepath = filepath;
	   this.range = range;
	   this.contents = contents;
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
	public final String getContents() {
	   return this.contents;
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
	public final String component3() {
	   return this.contents;
	}
	
	@NotNull
	public final RangeInFileWithContents copy(@NotNull String filepath, @NotNull Range range, @NotNull String contents) {
	   Intrinsics.checkNotNullParameter(filepath, "filepath");
	   Intrinsics.checkNotNullParameter(range, "range");
	   Intrinsics.checkNotNullParameter(contents, "contents");
	   return new RangeInFileWithContents(filepath, range, contents);
	}
	
	// $FF: synthetic method
	public static RangeInFileWithContents copy$default(RangeInFileWithContents var0, String var1, Range var2, String var3, int var4, Object var5) {
	   if ((var4 & 1) != 0) {
	      var1 = var0.filepath;
	   }
	
	   if ((var4 & 2) != 0) {
	      var2 = var0.range;
	   }
	
	   if ((var4 & 4) != 0) {
	      var3 = var0.contents;
	   }
	
	   return var0.copy(var1, var2, var3);
	}
	
	@NotNull
	public String toString() {
	   return "RangeInFileWithContents(filepath=" + this.filepath + ", range=" + this.range + ", contents=" + this.contents + ')';
	}
	
	public int hashCode() {
	   int result = this.filepath.hashCode();
	   result = result * 31 + this.range.hashCode();
	   result = result * 31 + this.contents.hashCode();
	   return result;
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof RangeInFileWithContents)) {
	      return false;
	   } else {
	      RangeInFileWithContents var2 = (RangeInFileWithContents)other;
	      if (!Intrinsics.areEqual(this.filepath, var2.filepath)) {
	         return false;
	      } else if (!Intrinsics.areEqual(this.range, var2.range)) {
	         return false;
	      } else {
	         return Intrinsics.areEqual(this.contents, var2.contents);
	      }
	   }
	}
}
