package com.github.continuedev.continueeclipseextension.type;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Thread {
	@NotNull
	private final String name;
	private final int id;
	
	public Thread(@NotNull String name, int id) {
	   Intrinsics.checkNotNullParameter(name, "name");
	   super();
	   this.name = name;
	   this.id = id;
	}
	
	@NotNull
	public final String getName() {
	   return this.name;
	}
	
	public final int getId() {
	   return this.id;
	}
	
	@NotNull
	public final String component1() {
	   return this.name;
	}
	
	public final int component2() {
	   return this.id;
	}
	
	@NotNull
	public final Thread copy(@NotNull String name, int id) {
	   Intrinsics.checkNotNullParameter(name, "name");
	   return new Thread(name, id);
	}
	
	// $FF: synthetic method
	public static Thread copy$default(Thread var0, String var1, int var2, int var3, Object var4) {
	   if ((var3 & 1) != 0) {
	      var1 = var0.name;
	   }
	
	   if ((var3 & 2) != 0) {
	      var2 = var0.id;
	   }
	
	   return var0.copy(var1, var2);
	}
	
	@NotNull
	public String toString() {
	   return "Thread(name=" + this.name + ", id=" + this.id + ')';
	}
	
	public int hashCode() {
	   int result = this.name.hashCode();
	   result = result * 31 + Integer.hashCode(this.id);
	   return result;
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof Thread)) {
	      return false;
	   } else {
	      Thread var2 = (Thread)other;
	      if (!Intrinsics.areEqual(this.name, var2.name)) {
	         return false;
	      } else {
	         return this.id == var2.id;
	      }
	   }
	}
}