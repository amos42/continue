package com.github.continuedev.continueeclipseextension.protocol;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ShowDiffParams {
	@NotNull
	private final String filepath;
	@NotNull
	private final String newContents;
	private final int stepIndex;
	
	public ShowDiffParams(@NotNull String filepath, @NotNull String newContents, int stepIndex) {
	   super();
	   Intrinsics.checkNotNullParameter(filepath, "filepath");
	   Intrinsics.checkNotNullParameter(newContents, "newContents");
	   this.filepath = filepath;
	   this.newContents = newContents;
	   this.stepIndex = stepIndex;
	}
	
	@NotNull
	public final String getFilepath() {
	   return this.filepath;
	}
	
	@NotNull
	public final String getNewContents() {
	   return this.newContents;
	}
	
	public final int getStepIndex() {
	   return this.stepIndex;
	}
	
	@NotNull
	public final String component1() {
	   return this.filepath;
	}
	
	@NotNull
	public final String component2() {
	   return this.newContents;
	}
	
	public final int component3() {
	   return this.stepIndex;
	}
	
	@NotNull
	public final ShowDiffParams copy(@NotNull String filepath, @NotNull String newContents, int stepIndex) {
	   Intrinsics.checkNotNullParameter(filepath, "filepath");
	   Intrinsics.checkNotNullParameter(newContents, "newContents");
	   return new ShowDiffParams(filepath, newContents, stepIndex);
	}
	
	// $FF: synthetic method
	public static ShowDiffParams copy$default(ShowDiffParams var0, String var1, String var2, int var3, int var4, Object var5) {
	   if ((var4 & 1) != 0) {
	      var1 = var0.filepath;
	   }
	
	   if ((var4 & 2) != 0) {
	      var2 = var0.newContents;
	   }
	
	   if ((var4 & 4) != 0) {
	      var3 = var0.stepIndex;
	   }
	
	   return var0.copy(var1, var2, var3);
	}
	
	@NotNull
	public String toString() {
	   return "ShowDiffParams(filepath=" + this.filepath + ", newContents=" + this.newContents + ", stepIndex=" + this.stepIndex + ')';
	}
	
	public int hashCode() {
	   int result = this.filepath.hashCode();
	   result = result * 31 + this.newContents.hashCode();
	   result = result * 31 + Integer.hashCode(this.stepIndex);
	   return result;
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof ShowDiffParams)) {
	      return false;
	   } else {
	      ShowDiffParams var2 = (ShowDiffParams)other;
	      if (!Intrinsics.areEqual(this.filepath, var2.filepath)) {
	         return false;
	      } else if (!Intrinsics.areEqual(this.newContents, var2.newContents)) {
	         return false;
	      } else {
	         return this.stepIndex == var2.stepIndex;
	      }
	   }
	}
}
