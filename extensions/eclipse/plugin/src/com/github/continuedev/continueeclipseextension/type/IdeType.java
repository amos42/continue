package com.github.continuedev.continueeclipseextension.type;

import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import org.jetbrains.annotations.NotNull;

public enum IdeType {
   @NotNull
   private final String value;
   JETBRAINS("jetbrains"),
   VSCODE("vscode");

   // $FF: synthetic field
   private static final EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

   private IdeType(String value) {
      this.value = value;
   }

   @NotNull
   public final String getValue() {
      return this.value;
   }

   @NotNull
   public static EnumEntries getEntries() {
      return $ENTRIES;
   }

   // $FF: synthetic method
   private static final IdeType[] $values() {
      IdeType[] var0 = new IdeType[]{JETBRAINS, VSCODE};
      return var0;
   }
}




