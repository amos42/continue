package com.github.continuedev.continueeclipseextension.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DeleteAtIndex {
	private final int index;
	
	public DeleteAtIndex(int index) {
	   this.index = index;
	}
	
	public final int getIndex() {
	   return this.index;
	}
	
	public final int component1() {
	   return this.index;
	}
	
	@NotNull
	public final DeleteAtIndex copy(int index) {
	   return new DeleteAtIndex(index);
	}
	
	// $FF: synthetic method
	public static DeleteAtIndex copy$default(DeleteAtIndex var0, int var1, int var2, Object var3) {
	   if ((var2 & 1) != 0) {
	      var1 = var0.index;
	   }
	
	   return var0.copy(var1);
	}
	
	@NotNull
	public String toString() {
	   return "DeleteAtIndex(index=" + this.index + ')';
	}
	
	public int hashCode() {
	   return Integer.hashCode(this.index);
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof DeleteAtIndex)) {
	      return false;
	   } else {
	      DeleteAtIndex var2 = (DeleteAtIndex)other;
	      return this.index == var2.index;
	   }
	}
}