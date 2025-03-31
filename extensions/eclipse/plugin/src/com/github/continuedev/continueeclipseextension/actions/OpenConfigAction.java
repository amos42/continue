package com.github.continuedev.continueeclipseextension.actions;

import com.github.continuedev.continueeclipseextension.services.ContinuePluginService;
import com.github.continuedev.continueeclipseextension.toolWindow.ContinuePluginToolWindowFactory;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import java.awt.Component;
import java.util.Map;
import javax.swing.JComponent;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

public final class OpenConfigAction extends AnAction {
	public void actionPerformed(@NotNull AnActionEvent e) {
	   Intrinsics.checkNotNullParameter(e, "e");
	   ContinuePluginService params = ContinuePluginActionsKt.getContinuePluginService(e.getProject());
	   if (params != null) {
	      ContinuePluginService continuePluginService = params;
	      ContinuePluginToolWindowFactory.ContinuePluginWindow var7 = params.getContinuePluginWindow();
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
	
	      Pair[] var9 = new Pair[]{TuplesKt.to("path", "/config"), TuplesKt.to("toggle", true)};
	      Map params = MapsKt.mapOf(var9);
	      ContinuePluginService.sendToWebview$default(continuePluginService, "navigateTo", params, (String)null, 4, (Object)null);
	   }
	}
}