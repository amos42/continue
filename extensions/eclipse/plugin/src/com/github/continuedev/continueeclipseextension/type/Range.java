package com.github.continuedev.continueeclipseextension.type;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Range {
	@NotNull
	private final Position start;
	@NotNull
	private final Position end;
	
	public Range(@NotNull Position start, @NotNull Position end) {
	   Intrinsics.checkNotNullParameter(start, "start");
	   Intrinsics.checkNotNullParameter(end, "end");
	   super();
	   this.start = start;
	   this.end = end;
	}
	
	@NotNull
	public final Position getStart() {
	   return this.start;
	}
	
	@NotNull
	public final Position getEnd() {
	   return this.end;
	}
	
	@NotNull
	public final Position component1() {
	   return this.start;
	}
	
	@NotNull
	public final Position component2() {
	   return this.end;
	}
	
	@NotNull
	public final Range copy(@NotNull Position start, @NotNull Position end) {
	   Intrinsics.checkNotNullParameter(start, "start");
	   Intrinsics.checkNotNullParameter(end, "end");
	   return new Range(start, end);
	}
	
	// $FF: synthetic method
	public static Range copy$default(Range var0, Position var1, Position var2, int var3, Object var4) {
	   if ((var3 & 1) != 0) {
	      var1 = var0.start;
	   }
	
	   if ((var3 & 2) != 0) {
	      var2 = var0.end;
	   }
	
	   return var0.copy(var1, var2);
	}
	
	@NotNull
	public String toString() {
	   return "Range(start=" + this.start + ", end=" + this.end + ')';
	}
	
	public int hashCode() {
	   int result = this.start.hashCode();
	   result = result * 31 + this.end.hashCode();
	   return result;
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof Range)) {
	      return false;
	   } else {
	      Range var2 = (Range)other;
	      if (!Intrinsics.areEqual(this.start, var2.start)) {
	         return false;
	      } else {
	         return Intrinsics.areEqual(this.end, var2.end);
	      }
	   }
	}
}