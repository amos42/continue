package com.github.continuedev.continueeclipseextension.type;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Problem {
	@NotNull
	private final String filepath;
	@NotNull
	private final Range range;
	@NotNull
	private final String message;
	
	public Problem(@NotNull String filepath, @NotNull Range range, @NotNull String message) {
	   Intrinsics.checkNotNullParameter(filepath, "filepath");
	   Intrinsics.checkNotNullParameter(range, "range");
	   Intrinsics.checkNotNullParameter(message, "message");
	   super();
	   this.filepath = filepath;
	   this.range = range;
	   this.message = message;
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
	public final String getMessage() {
	   return this.message;
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
	   return this.message;
	}
	
	@NotNull
	public final Problem copy(@NotNull String filepath, @NotNull Range range, @NotNull String message) {
	   Intrinsics.checkNotNullParameter(filepath, "filepath");
	   Intrinsics.checkNotNullParameter(range, "range");
	   Intrinsics.checkNotNullParameter(message, "message");
	   return new Problem(filepath, range, message);
	}
	
	// $FF: synthetic method
	public static Problem copy$default(Problem var0, String var1, Range var2, String var3, int var4, Object var5) {
	   if ((var4 & 1) != 0) {
	      var1 = var0.filepath;
	   }
	
	   if ((var4 & 2) != 0) {
	      var2 = var0.range;
	   }
	
	   if ((var4 & 4) != 0) {
	      var3 = var0.message;
	   }
	
	   return var0.copy(var1, var2, var3);
	}
	
	@NotNull
	public String toString() {
	   return "Problem(filepath=" + this.filepath + ", range=" + this.range + ", message=" + this.message + ')';
	}
	
	public int hashCode() {
	   int result = this.filepath.hashCode();
	   result = result * 31 + this.range.hashCode();
	   result = result * 31 + this.message.hashCode();
	   return result;
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof Problem)) {
	      return false;
	   } else {
	      Problem var2 = (Problem)other;
	      if (!Intrinsics.areEqual(this.filepath, var2.filepath)) {
	         return false;
	      } else if (!Intrinsics.areEqual(this.range, var2.range)) {
	         return false;
	      } else {
	         return Intrinsics.areEqual(this.message, var2.message);
	      }
	   }
	}
}
