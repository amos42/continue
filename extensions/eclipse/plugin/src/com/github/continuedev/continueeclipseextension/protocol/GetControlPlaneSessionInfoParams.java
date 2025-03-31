package com.github.continuedev.continueeclipseextension.protocol;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class GetControlPlaneSessionInfoParams {
   private final boolean silent;
   private final boolean useOnboarding;

   public GetControlPlaneSessionInfoParams(boolean silent, boolean useOnboarding) {
      this.silent = silent;
      this.useOnboarding = useOnboarding;
   }

   public final boolean getSilent() {
      return this.silent;
   }

   public final boolean getUseOnboarding() {
      return this.useOnboarding;
   }

   public final boolean component1() {
      return this.silent;
   }

   public final boolean component2() {
      return this.useOnboarding;
   }

   @NotNull
   public final GetControlPlaneSessionInfoParams copy(boolean silent, boolean useOnboarding) {
      return new GetControlPlaneSessionInfoParams(silent, useOnboarding);
   }

   // $FF: synthetic method
   public static GetControlPlaneSessionInfoParams copy$default(GetControlPlaneSessionInfoParams var0, boolean var1, boolean var2, int var3, Object var4) {
      if ((var3 & 1) != 0) {
         var1 = var0.silent;
      }

      if ((var3 & 2) != 0) {
         var2 = var0.useOnboarding;
      }

      return var0.copy(var1, var2);
   }

   @NotNull
   public String toString() {
      return "GetControlPlaneSessionInfoParams(silent=" + this.silent + ", useOnboarding=" + this.useOnboarding + ')';
   }

   public int hashCode() {
      int result = Boolean.hashCode(this.silent);
      result = result * 31 + Boolean.hashCode(this.useOnboarding);
      return result;
   }

   public boolean equals(@Nullable Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof GetControlPlaneSessionInfoParams)) {
         return false;
      } else {
         GetControlPlaneSessionInfoParams var2 = (GetControlPlaneSessionInfoParams)other;
         if (this.silent != var2.silent) {
            return false;
         } else {
            return this.useOnboarding == var2.useOnboarding;
         }
      }
   }
}

