package com.github.continuedev.continueeclipseextension.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FileStats {
	private final long lastModified;
	private final long size;
	
	public FileStats(long lastModified, long size) {
	   this.lastModified = lastModified;
	   this.size = size;
	}
	
	public final long getLastModified() {
	   return this.lastModified;
	}
	
	public final long getSize() {
	   return this.size;
	}
	
	public final long component1() {
	   return this.lastModified;
	}
	
	public final long component2() {
	   return this.size;
	}
	
	@NotNull
	public final FileStats copy(long lastModified, long size) {
	   return new FileStats(lastModified, size);
	}
	
	// $FF: synthetic method
	public static FileStats copy$default(FileStats var0, long var1, long var3, int var5, Object var6) {
	   if ((var5 & 1) != 0) {
	      var1 = var0.lastModified;
	   }
	
	   if ((var5 & 2) != 0) {
	      var3 = var0.size;
	   }
	
	   return var0.copy(var1, var3);
	}
	
	@NotNull
	public String toString() {
	   return "FileStats(lastModified=" + this.lastModified + ", size=" + this.size + ')';
	}
	
	public int hashCode() {
	   int result = Long.hashCode(this.lastModified);
	   result = result * 31 + Long.hashCode(this.size);
	   return result;
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof FileStats)) {
	      return false;
	   } else {
	      FileStats var2 = (FileStats)other;
	      if (this.lastModified != var2.lastModified) {
	         return false;
	      } else {
	         return this.size == var2.size;
	      }
	   }
	}
}