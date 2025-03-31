package com.github.continuedev.continueeclipseextension.actions;

import com.github.continuedev.continueeclipseextension.DiffManager;
import com.github.continuedev.continueeclipseextension.editor.DiffStreamService;
import com.github.continuedev.continueeclipseextension.services.ContinuePluginService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import org.jetbrains.annotations.NotNull;

public final class AcceptDiffAction extends AnAction {
   public void actionPerformed(@NotNull AnActionEvent e) {
      Intrinsics.checkNotNullParameter(e, "e");
      this.acceptHorizontalDiff(e);
      this.acceptVerticalDiff(e);
   }

   private final void acceptHorizontalDiff(AnActionEvent e) {
      ContinuePluginService var3 = ContinuePluginActionsKt.getPluginService(e.getProject());
      if (var3 != null) {
         DiffManager var4 = var3.getDiffManager();
         if (var4 != null) {
            var4.acceptDiff((String)null);
         }

      }
   }

   private final void acceptVerticalDiff(AnActionEvent e) {
      Project diffStreamService = e.getProject();
      if (diffStreamService != null) {
         Project project = diffStreamService;
         Editor $this$service$iv = (Editor)e.getData(PlatformDataKeys.EDITOR);
         Editor var10000;
         if ($this$service$iv != null) {
            var10000 = $this$service$iv;
         } else {
            Editor $i$f$service = FileEditorManager.getInstance(diffStreamService).getSelectedTextEditor();
            if ($i$f$service == null) {
               return;
            }

            var10000 = $i$f$service;
         }

         Editor var9 = var10000;
         Editor editor = var9;
         ComponentManager $this$service$iv = (ComponentManager)project;
         int $i$f$service = 0;
         Class serviceClass$iv = DiffStreamService.class;
         Object var8 = $this$service$iv.getService(serviceClass$iv);
         if (var8 == null) {
            throw new IllegalStateException(("Cannot find service " + serviceClass$iv.getName() + " in " + $this$service$iv + " (classloader=" + serviceClass$iv.getClassLoader()).toString());
         } else {
            DiffStreamService diffStreamService = (DiffStreamService)var8;
            diffStreamService.accept(editor);
         }
      }
   }
}
