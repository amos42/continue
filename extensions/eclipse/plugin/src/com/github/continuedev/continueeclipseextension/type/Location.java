package com.github.continuedev.continueeclipseextension.type;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Location {
	@NotNull
	private final String filepath;
	@NotNull
	private final Position position;
	
	public Location(@NotNull String filepath, @NotNull Position position) {
	   Intrinsics.checkNotNullParameter(filepath, "filepath");
	   Intrinsics.checkNotNullParameter(position, "position");
	   super();
	   this.filepath = filepath;
	   this.position = position;
	}
	
	@NotNull
	public final String getFilepath() {
	   return this.filepath;
	}
	
	@NotNull
	public final Position getPosition() {
	   return this.position;
	}
	
	@NotNull
	public final String component1() {
	   return this.filepath;
	}
	
	@NotNull
	public final Position component2() {
	   return this.position;
	}
	
	@NotNull
	public final Location copy(@NotNull String filepath, @NotNull Position position) {
	   Intrinsics.checkNotNullParameter(filepath, "filepath");
	   Intrinsics.checkNotNullParameter(position, "position");
	   return new Location(filepath, position);
	}
	
	// $FF: synthetic method
	public static Location copy$default(Location var0, String var1, Position var2, int var3, Object var4) {
	   if ((var3 & 1) != 0) {
	      var1 = var0.filepath;
	   }
	
	   if ((var3 & 2) != 0) {
	      var2 = var0.position;
	   }
	
	   return var0.copy(var1, var2);
	}
	
	@NotNull
	public String toString() {
	   return "Location(filepath=" + this.filepath + ", position=" + this.position + ')';
	}
	
	public int hashCode() {
	   int result = this.filepath.hashCode();
	   result = result * 31 + this.position.hashCode();
	   return result;
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof Location)) {
	      return false;
	   } else {
	      Location var2 = (Location)other;
	      if (!Intrinsics.areEqual(this.filepath, var2.filepath)) {
	         return false;
	      } else {
	         return Intrinsics.areEqual(this.position, var2.position);
	      }
	   }
	}
}
