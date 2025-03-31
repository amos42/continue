package com.github.continuedev.continueeclipseextension.actions;

import com.github.continuedev.continueeclipseextension.IdeProtocolClient;
import com.github.continuedev.continueeclipseextension.services.ContinuePluginService;
import com.github.continuedev.continueeclipseextension.toolWindow.ContinuePluginToolWindowFactory;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import java.awt.Component;
import javax.swing.JComponent;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

public final class FocusContinueInputAction extends AnAction {
   public void actionPerformed(@NotNull AnActionEvent e) {
      Intrinsics.checkNotNullParameter(e, "e");
      ContinuePluginService var3 = ContinuePluginActionsKt.getContinuePluginService(e.getProject());
      if (var3 != null) {
         ContinuePluginService continuePluginService = var3;
         ContinuePluginToolWindowFactory.ContinuePluginWindow var7 = var3.getContinuePluginWindow();
         if (var7 != null) {
            JComponent var4 = var7.getContent();
            if (var4 != null) {
               Component[] var5 = var4.getComponents();
               if (var5 != null) {
                  Component var6 = var5[0];
                  if (var6 != null) {
                     var6.requestFocus();
                  }
               }
            }
         }

         ContinuePluginService.sendToWebview$default(continuePluginService, "focusContinueInputWithNewSession", (Object)null, (String)null, 4, (Object)null);
         IdeProtocolClient var8 = continuePluginService.getIdeProtocolClient();
         if (var8 != null) {
            IdeProtocolClient.sendHighlightedCode$default(var8, false, 1, (Object)null);
         }

      }
   }
}
