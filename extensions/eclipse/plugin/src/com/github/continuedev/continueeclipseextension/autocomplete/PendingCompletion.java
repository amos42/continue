package com.github.continuedev.continueeclipseextension.autocomplete;

import com.intellij.openapi.editor.Editor;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PendingCompletion {
   @NotNull
   private final Editor editor;
   private int offset;
   @NotNull
   private final String completionId;
   @Nullable
   private String text;

   public PendingCompletion(@NotNull Editor editor, int offset, @NotNull String completionId, @Nullable String text) {
      Intrinsics.checkNotNullParameter(editor, "editor");
      Intrinsics.checkNotNullParameter(completionId, "completionId");
      super();
      this.editor = editor;
      this.offset = offset;
      this.completionId = completionId;
      this.text = text;
   }

   @NotNull
   public final Editor getEditor() {
      return this.editor;
   }

   public final int getOffset() {
      return this.offset;
   }

   public final void setOffset(int var1) {
      this.offset = var1;
   }

   @NotNull
   public final String getCompletionId() {
      return this.completionId;
   }

   @Nullable
   public final String getText() {
      return this.text;
   }

   public final void setText(@Nullable String var1) {
      this.text = var1;
   }

   @NotNull
   public final Editor component1() {
      return this.editor;
   }

   public final int component2() {
      return this.offset;
   }

   @NotNull
   public final String component3() {
      return this.completionId;
   }

   @Nullable
   public final String component4() {
      return this.text;
   }

   @NotNull
   public final PendingCompletion copy(@NotNull Editor editor, int offset, @NotNull String completionId, @Nullable String text) {
      Intrinsics.checkNotNullParameter(editor, "editor");
      Intrinsics.checkNotNullParameter(completionId, "completionId");
      return new PendingCompletion(editor, offset, completionId, text);
   }

   // $FF: synthetic method
   public static PendingCompletion copy$default(PendingCompletion var0, Editor var1, int var2, String var3, String var4, int var5, Object var6) {
      if ((var5 & 1) != 0) {
         var1 = var0.editor;
      }

      if ((var5 & 2) != 0) {
         var2 = var0.offset;
      }

      if ((var5 & 4) != 0) {
         var3 = var0.completionId;
      }

      if ((var5 & 8) != 0) {
         var4 = var0.text;
      }

      return var0.copy(var1, var2, var3, var4);
   }

   @NotNull
   public String toString() {
      return "PendingCompletion(editor=" + this.editor + ", offset=" + this.offset + ", completionId=" + this.completionId + ", text=" + this.text + ')';
   }

   public int hashCode() {
      int result = this.editor.hashCode();
      result = result * 31 + Integer.hashCode(this.offset);
      result = result * 31 + this.completionId.hashCode();
      result = result * 31 + (this.text == null ? 0 : this.text.hashCode());
      return result;
   }

   public boolean equals(@Nullable Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof PendingCompletion)) {
         return false;
      } else {
         PendingCompletion var2 = (PendingCompletion)other;
         if (!Intrinsics.areEqual(this.editor, var2.editor)) {
            return false;
         } else if (this.offset != var2.offset) {
            return false;
         } else if (!Intrinsics.areEqual(this.completionId, var2.completionId)) {
            return false;
         } else {
            return Intrinsics.areEqual(this.text, var2.text);
         }
      }
   }
}
