package com.github.continuedev.continueeclipseextension.type;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ContinueRcJson {
	@NotNull
	private final ConfigMergeType mergeBehavior;
	
	public ContinueRcJson(@NotNull ConfigMergeType mergeBehavior) {
	   Intrinsics.checkNotNullParameter(mergeBehavior, "mergeBehavior");
	   super();
	   this.mergeBehavior = mergeBehavior;
	}
	
	@NotNull
	public final ConfigMergeType getMergeBehavior() {
	   return this.mergeBehavior;
	}
	
	@NotNull
	public final ConfigMergeType component1() {
	   return this.mergeBehavior;
	}
	
	@NotNull
	public final ContinueRcJson copy(@NotNull ConfigMergeType mergeBehavior) {
	   Intrinsics.checkNotNullParameter(mergeBehavior, "mergeBehavior");
	   return new ContinueRcJson(mergeBehavior);
	}
	
	// $FF: synthetic method
	public static ContinueRcJson copy$default(ContinueRcJson var0, ConfigMergeType var1, int var2, Object var3) {
	   if ((var2 & 1) != 0) {
	      var1 = var0.mergeBehavior;
	   }
	
	   return var0.copy(var1);
	}
	
	@NotNull
	public String toString() {
	   return "ContinueRcJson(mergeBehavior=" + this.mergeBehavior + ')';
	}
	
	public int hashCode() {
	   return this.mergeBehavior.hashCode();
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof ContinueRcJson)) {
	      return false;
	   } else {
	      ContinueRcJson var2 = (ContinueRcJson)other;
	      return this.mergeBehavior == var2.mergeBehavior;
	   }
	}
}

