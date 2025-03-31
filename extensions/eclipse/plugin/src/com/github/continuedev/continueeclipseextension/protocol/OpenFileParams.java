package com.github.continuedev.continueeclipseextension.protocol;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class OpenFileParams {
   @NotNull
   private final String path;

   public OpenFileParams(@NotNull String path) {
      super();
      Intrinsics.checkNotNullParameter(path, "path");
      this.path = path;
   }

   @NotNull
   public final String getPath() {
      return this.path;
   }

   @NotNull
   public final String component1() {
      return this.path;
   }

   @NotNull
   public final OpenFileParams copy(@NotNull String path) {
      Intrinsics.checkNotNullParameter(path, "path");
      return new OpenFileParams(path);
   }

   // $FF: synthetic method
   public static OpenFileParams copy$default(OpenFileParams var0, String var1, int var2, Object var3) {
      if ((var2 & 1) != 0) {
         var1 = var0.path;
      }

      return var0.copy(var1);
   }

   @NotNull
   public String toString() {
      return "OpenFileParams(path=" + this.path + ')';
   }

   public int hashCode() {
      return this.path.hashCode();
   }

   public boolean equals(@Nullable Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof OpenFileParams)) {
         return false;
      } else {
         OpenFileParams var2 = (OpenFileParams)other;
         return Intrinsics.areEqual(this.path, var2.path);
      }
   }
}
