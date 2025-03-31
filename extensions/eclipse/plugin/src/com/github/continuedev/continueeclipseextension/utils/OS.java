package com.github.continuedev.continueeclipseextension.utils;

import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import org.jetbrains.annotations.NotNull;

public enum OS {
   MAC,
   WINDOWS,
   LINUX;

   // $FF: synthetic field
   private static final EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

   @NotNull
   public static EnumEntries getEntries() {
      return $ENTRIES;
   }

   // $FF: synthetic method
   private static final OS[] $values() {
      OS[] var0 = new OS[]{MAC, WINDOWS, LINUX};
      return var0;
   }
}
