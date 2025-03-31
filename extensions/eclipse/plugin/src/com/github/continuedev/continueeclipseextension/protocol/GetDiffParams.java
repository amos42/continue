package com.github.continuedev.continueeclipseextension.protocol;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class GetDiffParams {
	private final boolean includeUnstaged;
	
	public GetDiffParams(boolean includeUnstaged) {
	   this.includeUnstaged = includeUnstaged;
	}
	
	public final boolean getIncludeUnstaged() {
	   return this.includeUnstaged;
	}
	
	public final boolean component1() {
	   return this.includeUnstaged;
	}
	
	@NotNull
	public final GetDiffParams copy(boolean includeUnstaged) {
	   return new GetDiffParams(includeUnstaged);
	}
	
	// $FF: synthetic method
	public static GetDiffParams copy$default(GetDiffParams var0, boolean var1, int var2, Object var3) {
	   if ((var2 & 1) != 0) {
	      var1 = var0.includeUnstaged;
	   }
	
	   return var0.copy(var1);
	}
	
	@NotNull
	public String toString() {
	   return "GetDiffParams(includeUnstaged=" + this.includeUnstaged + ')';
	}
	
	public int hashCode() {
	   return Boolean.hashCode(this.includeUnstaged);
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof GetDiffParams)) {
	      return false;
	   } else {
	      GetDiffParams var2 = (GetDiffParams)other;
	      return this.includeUnstaged == var2.includeUnstaged;
	   }
	}
}