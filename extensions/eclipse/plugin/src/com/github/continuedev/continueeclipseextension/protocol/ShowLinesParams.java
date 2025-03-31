package com.github.continuedev.continueeclipseextension.protocol;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ShowLinesParams {
	@NotNull
	private final String filepath;
	private final int startLine;
	private final int endLine;
	
	public ShowLinesParams(@NotNull String filepath, int startLine, int endLine) {
	   super();
	   Intrinsics.checkNotNullParameter(filepath, "filepath");
	   this.filepath = filepath;
	   this.startLine = startLine;
	   this.endLine = endLine;
	}
	
	@NotNull
	public final String getFilepath() {
	   return this.filepath;
	}
	
	public final int getStartLine() {
	   return this.startLine;
	}
	
	public final int getEndLine() {
	   return this.endLine;
	}
	
	@NotNull
	public final String component1() {
	   return this.filepath;
	}
	
	public final int component2() {
	   return this.startLine;
	}
	
	public final int component3() {
	   return this.endLine;
	}
	
	@NotNull
	public final ShowLinesParams copy(@NotNull String filepath, int startLine, int endLine) {
	   Intrinsics.checkNotNullParameter(filepath, "filepath");
	   return new ShowLinesParams(filepath, startLine, endLine);
	}
	
	// $FF: synthetic method
	public static ShowLinesParams copy$default(ShowLinesParams var0, String var1, int var2, int var3, int var4, Object var5) {
	   if ((var4 & 1) != 0) {
	      var1 = var0.filepath;
	   }
	
	   if ((var4 & 2) != 0) {
	      var2 = var0.startLine;
	   }
	
	   if ((var4 & 4) != 0) {
	      var3 = var0.endLine;
	   }
	
	   return var0.copy(var1, var2, var3);
	}
	
	@NotNull
	public String toString() {
	   return "ShowLinesParams(filepath=" + this.filepath + ", startLine=" + this.startLine + ", endLine=" + this.endLine + ')';
	}
	
	public int hashCode() {
	   int result = this.filepath.hashCode();
	   result = result * 31 + Integer.hashCode(this.startLine);
	   result = result * 31 + Integer.hashCode(this.endLine);
	   return result;
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof ShowLinesParams)) {
	      return false;
	   } else {
	      ShowLinesParams var2 = (ShowLinesParams)other;
	      if (!Intrinsics.areEqual(this.filepath, var2.filepath)) {
	         return false;
	      } else if (this.startLine != var2.startLine) {
	         return false;
	      } else {
	         return this.endLine == var2.endLine;
	      }
	   }
	}
}
