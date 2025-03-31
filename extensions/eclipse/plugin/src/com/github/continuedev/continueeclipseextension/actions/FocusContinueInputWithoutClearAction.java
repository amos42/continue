package com.github.continuedev.continueeclipseextension.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

public final class FocusContinueInputWithoutClearAction extends AnAction {
   public void actionPerformed(@NotNull AnActionEvent e) {
      Intrinsics.checkNotNullParameter(e, "e");
      Project project = e.getProject();
      ContinuePluginActionsKt.focusContinueInput(project);
   }
}
