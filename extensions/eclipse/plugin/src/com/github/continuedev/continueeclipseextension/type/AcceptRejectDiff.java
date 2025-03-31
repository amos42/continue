package com.github.continuedev.continueeclipseextension.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class AcceptRejectDiff {
	private final boolean accepted;
	private final int stepIndex;
	
	public AcceptRejectDiff(boolean accepted, int stepIndex) {
	   this.accepted = accepted;
	   this.stepIndex = stepIndex;
	}
	
	public final boolean getAccepted() {
	   return this.accepted;
	}
	
	public final int getStepIndex() {
	   return this.stepIndex;
	}
	
	public final boolean component1() {
	   return this.accepted;
	}
	
	public final int component2() {
	   return this.stepIndex;
	}
	
	@NotNull
	public final AcceptRejectDiff copy(boolean accepted, int stepIndex) {
	   return new AcceptRejectDiff(accepted, stepIndex);
	}
	
	// $FF: synthetic method
	public static AcceptRejectDiff copy$default(AcceptRejectDiff var0, boolean var1, int var2, int var3, Object var4) {
	   if ((var3 & 1) != 0) {
	      var1 = var0.accepted;
	   }
	
	   if ((var3 & 2) != 0) {
	      var2 = var0.stepIndex;
	   }
	
	   return var0.copy(var1, var2);
	}
	
	@NotNull
	public String toString() {
	   return "AcceptRejectDiff(accepted=" + this.accepted + ", stepIndex=" + this.stepIndex + ')';
	}
	
	public int hashCode() {
	   int result = Boolean.hashCode(this.accepted);
	   result = result * 31 + Integer.hashCode(this.stepIndex);
	   return result;
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof AcceptRejectDiff)) {
	      return false;
	   } else {
	      AcceptRejectDiff var2 = (AcceptRejectDiff)other;
	      if (this.accepted != var2.accepted) {
	         return false;
	      } else {
	         return this.stepIndex == var2.stepIndex;
	      }
	   }
	}
}