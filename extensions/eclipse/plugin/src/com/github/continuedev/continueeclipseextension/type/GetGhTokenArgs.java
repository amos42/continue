package com.github.continuedev.continueeclipseextension.type;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class GetGhTokenArgs {
   @Nullable
   private final String force;

   public GetGhTokenArgs(@Nullable String force) {
      this.force = force;
   }

   @Nullable
   public final String getForce() {
      return this.force;
   }

   @Nullable
   public final String component1() {
      return this.force;
   }

   @NotNull
   public final GetGhTokenArgs copy(@Nullable String force) {
      return new GetGhTokenArgs(force);
   }

   // $FF: synthetic method
   public static GetGhTokenArgs copy$default(GetGhTokenArgs var0, String var1, int var2, Object var3) {
      if ((var2 & 1) != 0) {
         var1 = var0.force;
      }

      return var0.copy(var1);
   }

   @NotNull
   public String toString() {
      return "GetGhTokenArgs(force=" + this.force + ')';
   }

   public int hashCode() {
      return this.force == null ? 0 : this.force.hashCode();
   }

   public boolean equals(@Nullable Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof GetGhTokenArgs)) {
         return false;
      } else {
         GetGhTokenArgs var2 = (GetGhTokenArgs)other;
         return Intrinsics.areEqual(this.force, var2.force);
      }
   }
}