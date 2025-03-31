package com.github.continuedev.continueeclipseextension.actions;

import com.github.continuedev.continueeclipseextension.IdeProtocolClient;
import com.github.continuedev.continueeclipseextension.services.ContinuePluginService;
import com.github.continuedev.continueeclipseextension.toolWindow.ContinuePluginToolWindowFactory;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import java.awt.Component;
import javax.swing.JComponent;
import kotlin.Metadata;
import org.jetbrains.annotations.Nullable;

public final class ContinuePluginActionsKt {
   @Nullable
   public static final ContinuePluginService getPluginService(@Nullable Project project) {
      return project == null ? null : (ContinuePluginService)ServiceManager.getService(project, ContinuePluginService.class);
   }

   @Nullable
   public static final ContinuePluginService getContinuePluginService(@Nullable Project project) {
      if (project != null) {
         ToolWindowManager toolWindowManager = ToolWindowManager.Companion.getInstance(project);
         ToolWindow toolWindow = toolWindowManager.getToolWindow("Continue");
         if (toolWindow != null && !toolWindow.isVisible()) {
            toolWindow.activate((Runnable)null);
         }
      }

      return getPluginService(project);
   }

   public static final void focusContinueInput(@Nullable Project project) {
      ContinuePluginService var2 = getContinuePluginService(project);
      if (var2 != null) {
         ContinuePluginService continuePluginService = var2;
         ContinuePluginToolWindowFactory.ContinuePluginWindow var6 = var2.getContinuePluginWindow();
         if (var6 != null) {
            JComponent var3 = var6.getContent();
            if (var3 != null) {
               Component[] var4 = var3.getComponents();
               if (var4 != null) {
                  Component var5 = var4[0];
                  if (var5 != null) {
                     var5.requestFocus();
                  }
               }
            }
         }

         ContinuePluginService.sendToWebview$default(continuePluginService, "focusContinueInputWithoutClear", (Object)null, (String)null, 4, (Object)null);
         IdeProtocolClient var7 = continuePluginService.getIdeProtocolClient();
         if (var7 != null) {
            IdeProtocolClient.sendHighlightedCode$default(var7, false, 1, (Object)null);
         }

      }
   }
}

