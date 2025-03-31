package com.github.continuedev.continueeclipseextension.autocomplete;

import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorKind;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.project.Project;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

public final class AutocompleteCaretListener implements CaretListener {
   public void caretPositionChanged(@NotNull CaretEvent event) {
      Intrinsics.checkNotNullParameter(event, "event");
      if (event.getEditor().getEditorKind() == EditorKind.MAIN_EDITOR) {
         Caret editor = event.getCaret();
         if (editor != null) {
            int offset = editor.getOffset();
            Editor autocompleteService = editor.getEditor();
            Intrinsics.checkNotNullExpressionValue(autocompleteService, "getEditor(...)");
            Project pending = autocompleteService.getProject();
            if (pending != null) {
               ComponentManager $this$service$iv = (ComponentManager)pending;
               int $i$f$service = 0;
               Class serviceClass$iv = AutocompleteService.class;
               Object var11 = $this$service$iv.getService(serviceClass$iv);
               if (var11 == null) {
                  throw new IllegalStateException(("Cannot find service " + serviceClass$iv.getName() + " in " + $this$service$iv + " (classloader=" + serviceClass$iv.getClassLoader()).toString());
               } else {
                  AutocompleteService var7 = (AutocompleteService)var11;
                  if (var7.getLastChangeWasPartialAccept()) {
                     var7.setLastChangeWasPartialAccept(false);
                  } else {
                     PendingCompletion pending = var7.getPendingCompletion();
                     if (pending == null || !Intrinsics.areEqual(pending.getEditor(), autocompleteService) || pending.getOffset() != offset) {
                        AutocompleteService.clearCompletions$default(var7, autocompleteService, (PendingCompletion)null, 2, (Object)null);
                     }
                  }
               }
            }
         }
      }
   }
}
