package com.github.continuedev.continueeclipseextension.autocomplete;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.Project;
import kotlin.jvm.internal.Intrinsics;

public final class AcceptAutocompleteAction extends EditorAction {
   public AcceptAutocompleteAction() {
      super(new EditorActionHandler() {
         protected void doExecute(Editor editor, Caret caret, DataContext dataContext) {
            Intrinsics.checkNotNullParameter(editor, "editor");
            ApplicationManager.getApplication().runWriteAction(<undefinedtype>::doExecute$lambda$0);
         }

         protected boolean isEnabledForCaret(Editor editor, Caret caret, DataContext dataContext) {
            Intrinsics.checkNotNullParameter(editor, "editor");
            Intrinsics.checkNotNullParameter(caret, "caret");
            Project enabled = editor.getProject();
            AutocompleteService var10000;
            if (enabled != null) {
               ComponentManager $this$service$iv = (ComponentManager)enabled;
               int $i$f$service = 0;
               Class serviceClass$iv = AutocompleteService.class;
               Object var9 = $this$service$iv.getService(serviceClass$iv);
               if (var9 == null) {
                  throw new IllegalStateException(("Cannot find service " + serviceClass$iv.getName() + " in " + $this$service$iv + " (classloader=" + serviceClass$iv.getClassLoader()).toString());
               }

               var10000 = (AutocompleteService)var9;
            } else {
               var10000 = null;
            }

            AutocompleteService autocompleteService;
            Editor var10001;
            label34: {
               autocompleteService = var10000;
               if (autocompleteService != null) {
                  PendingCompletion var11 = autocompleteService.getPendingCompletion();
                  if (var11 != null) {
                     var10001 = var11.getEditor();
                     break label34;
                  }
               }

               var10001 = null;
            }

            label29: {
               if (Intrinsics.areEqual(editor, var10001)) {
                  PendingCompletion var12 = autocompleteService.getPendingCompletion();
                  if ((var12 != null ? var12.getText() : null) != null) {
                     var13 = true;
                     break label29;
                  }
               }

               var13 = false;
            }

            boolean enabled = var13;
            return enabled;
         }

         private static final void doExecute$lambda$0(Editor $editor) {
            Project var1 = $editor.getProject();
            if (var1 != null) {
               ComponentManager $this$service$iv = (ComponentManager)var1;
               int $i$f$service = 0;
               Class serviceClass$iv = AutocompleteService.class;
               Object var6 = $this$service$iv.getService(serviceClass$iv);
               if (var6 == null) {
                  throw new IllegalStateException(("Cannot find service " + serviceClass$iv.getName() + " in " + $this$service$iv + " (classloader=" + serviceClass$iv.getClassLoader()).toString());
               }

               AutocompleteService var2 = (AutocompleteService)var6;
               var2.accept();
            }

         }
      });
   }
}
