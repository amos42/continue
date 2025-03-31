package com.github.continuedev.continueeclipseextension.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Position {
	private final int line;
	private final int character;
	
	public Position(int line, int character) {
	   this.line = line;
	   this.character = character;
	}
	
	public final int getLine() {
	   return this.line;
	}
	
	public final int getCharacter() {
	   return this.character;
	}
	
	public final int component1() {
	   return this.line;
	}
	
	public final int component2() {
	   return this.character;
	}
	
	@NotNull
	public final Position copy(int line, int character) {
	   return new Position(line, character);
	}
	
	// $FF: synthetic method
	public static Position copy$default(Position var0, int var1, int var2, int var3, Object var4) {
	   if ((var3 & 1) != 0) {
	      var1 = var0.line;
	   }
	
	   if ((var3 & 2) != 0) {
	      var2 = var0.character;
	   }
	
	   return var0.copy(var1, var2);
	}
	
	@NotNull
	public String toString() {
	   return "Position(line=" + this.line + ", character=" + this.character + ')';
	}
	
	public int hashCode() {
	   int result = Integer.hashCode(this.line);
	   result = result * 31 + Integer.hashCode(this.character);
	   return result;
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof Position)) {
	      return false;
	   } else {
	      Position var2 = (Position)other;
	      if (this.line != var2.line) {
	         return false;
	      } else {
	         return this.character == var2.character;
	      }
	   }
	}
}