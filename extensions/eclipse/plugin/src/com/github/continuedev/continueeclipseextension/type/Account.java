package com.github.continuedev.continueeclipseextension.type;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Account {
	@NotNull
	private final String label;
	@NotNull
	private final String id;
	
	public Account(@NotNull String label, @NotNull String id) {
	   Intrinsics.checkNotNullParameter(label, "label");
	   Intrinsics.checkNotNullParameter(id, "id");
	   super();
	   this.label = label;
	   this.id = id;
	}
	
	@NotNull
	public final String getLabel() {
	   return this.label;
	}
	
	@NotNull
	public final String getId() {
	   return this.id;
	}
	
	@NotNull
	public final String component1() {
	   return this.label;
	}
	
	@NotNull
	public final String component2() {
	   return this.id;
	}
	
	@NotNull
	public final Account copy(@NotNull String label, @NotNull String id) {
	   Intrinsics.checkNotNullParameter(label, "label");
	   Intrinsics.checkNotNullParameter(id, "id");
	   return new Account(label, id);
	}
	
	// $FF: synthetic method
	public static Account copy$default(Account var0, String var1, String var2, int var3, Object var4) {
	   if ((var3 & 1) != 0) {
	      var1 = var0.label;
	   }
	
	   if ((var3 & 2) != 0) {
	      var2 = var0.id;
	   }
	
	   return var0.copy(var1, var2);
	}
	
	@NotNull
	public String toString() {
	   return "Account(label=" + this.label + ", id=" + this.id + ')';
	}
	
	public int hashCode() {
	   int result = this.label.hashCode();
	   result = result * 31 + this.id.hashCode();
	   return result;
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof Account)) {
	      return false;
	   } else {
	      Account var2 = (Account)other;
	      if (!Intrinsics.areEqual(this.label, var2.label)) {
	         return false;
	      } else {
	         return Intrinsics.areEqual(this.id, var2.id);
	      }
	   }
	}
}