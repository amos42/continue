package com.github.continuedev.continueeclipseextension.type;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class IndexTag {
	@NotNull
	private final String artifactId;
	@NotNull
	private final String branch;
	@NotNull
	private final String directory;
	
	public IndexTag(@NotNull String artifactId, @NotNull String branch, @NotNull String directory) {
	   Intrinsics.checkNotNullParameter(artifactId, "artifactId");
	   Intrinsics.checkNotNullParameter(branch, "branch");
	   Intrinsics.checkNotNullParameter(directory, "directory");
	   super();
	   this.artifactId = artifactId;
	   this.branch = branch;
	   this.directory = directory;
	}
	
	@NotNull
	public final String getArtifactId() {
	   return this.artifactId;
	}
	
	@NotNull
	public final String getBranch() {
	   return this.branch;
	}
	
	@NotNull
	public final String getDirectory() {
	   return this.directory;
	}
	
	@NotNull
	public final String component1() {
	   return this.artifactId;
	}
	
	@NotNull
	public final String component2() {
	   return this.branch;
	}
	
	@NotNull
	public final String component3() {
	   return this.directory;
	}
	
	@NotNull
	public final IndexTag copy(@NotNull String artifactId, @NotNull String branch, @NotNull String directory) {
	   Intrinsics.checkNotNullParameter(artifactId, "artifactId");
	   Intrinsics.checkNotNullParameter(branch, "branch");
	   Intrinsics.checkNotNullParameter(directory, "directory");
	   return new IndexTag(artifactId, branch, directory);
	}
	
	// $FF: synthetic method
	public static IndexTag copy$default(IndexTag var0, String var1, String var2, String var3, int var4, Object var5) {
	   if ((var4 & 1) != 0) {
	      var1 = var0.artifactId;
	   }
	
	   if ((var4 & 2) != 0) {
	      var2 = var0.branch;
	   }
	
	   if ((var4 & 4) != 0) {
	      var3 = var0.directory;
	   }
	
	   return var0.copy(var1, var2, var3);
	}
	
	@NotNull
	public String toString() {
	   return "IndexTag(artifactId=" + this.artifactId + ", branch=" + this.branch + ", directory=" + this.directory + ')';
	}
	
	public int hashCode() {
	   int result = this.artifactId.hashCode();
	   result = result * 31 + this.branch.hashCode();
	   result = result * 31 + this.directory.hashCode();
	   return result;
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof IndexTag)) {
	      return false;
	   } else {
	      IndexTag var2 = (IndexTag)other;
	      if (!Intrinsics.areEqual(this.artifactId, var2.artifactId)) {
	         return false;
	      } else if (!Intrinsics.areEqual(this.branch, var2.branch)) {
	         return false;
	      } else {
	         return Intrinsics.areEqual(this.directory, var2.directory);
	      }
	   }
	}
}