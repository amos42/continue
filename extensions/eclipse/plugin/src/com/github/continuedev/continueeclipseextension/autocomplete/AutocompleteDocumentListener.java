package com.github.continuedev.continueeclipseextension.autocomplete;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

public final class AutocompleteDocumentListener implements DocumentListener {
	public final class documentChanged implements Runnable {
		   // $FF: synthetic field
		   final AutocompleteService $service$inlined;
		   // $FF: synthetic field
		   final AutocompleteDocumentListener this$0;

		   public documentChanged(AutocompleteService var1, AutocompleteDocumentListener var2) {
		      this.$service$inlined = var1;
		      this.this$0 = var2;
		   }

		   public final void run() {
		      int var1 = 0;
		      this.$service$inlined.triggerCompletion(AutocompleteDocumentListener.access$getEditor$p(this.this$0));
		   }
	}
	
   @NotNull
   private final FileEditorManager editorManager;
   @NotNull
   private final Editor editor;
   
   public AutocompleteDocumentListener(@NotNull FileEditorManager editorManager, @NotNull Editor editor) {
      Intrinsics.checkNotNullParameter(editorManager, "editorManager");
      Intrinsics.checkNotNullParameter(editor, "editor");
      super();
      this.editorManager = editorManager;
      this.editor = editor;
   }

   public void documentChanged(@NotNull DocumentEvent event) {
      Intrinsics.checkNotNullParameter(event, "event");
      if (Intrinsics.areEqual(this.editor, this.editorManager.getSelectedTextEditor())) {
         Project modalityState$iv = this.editor.getProject();
         if (modalityState$iv != null) {
            ComponentManager $this$service$iv = (ComponentManager)modalityState$iv;
            int $i$f$service = 0;
            Class serviceClass$iv = AutocompleteService.class;
            Object var8 = $this$service$iv.getService(serviceClass$iv);
            if (var8 == null) {
               throw new IllegalStateException(("Cannot find service " + serviceClass$iv.getName() + " in " + $this$service$iv + " (classloader=" + serviceClass$iv.getClassLoader()).toString());
            } else {
               AutocompleteService $i$f$invokeLater = (AutocompleteService)var8;
               AutocompleteService service = $i$f$invokeLater;
               if (!$i$f$invokeLater.getLastChangeWasPartialAccept()) {
                  ModalityState modalityState$iv = null;
                  int $i$f$invokeLater = 0;
                  Application var10000 = ApplicationManager.getApplication();
                  Runnable var10001 = new AutocompleteDocumentListener$documentChanged$$inlined$invokeLater$default$1(service, this);
                  ModalityState var10002 = ModalityState.defaultModalityState();
                  Intrinsics.checkNotNullExpressionValue(var10002, "defaultModalityState()");
                  var10000.invokeLater(var10001, var10002);
               }
            }
         }
      }
   }

   // $FF: synthetic method
   public static final Editor access$getEditor$p(AutocompleteDocumentListener $this) {
      return $this.editor;
   }
}
