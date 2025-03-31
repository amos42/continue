package com.github.continuedev.continueeclipseextension.actions;

import com.github.continuedev.continueeclipseextension.services.ContinuePluginService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import java.util.Map;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

public final class ViewHistoryAction extends AnAction {
   public void actionPerformed(@NotNull AnActionEvent e) {
      Intrinsics.checkNotNullParameter(e, "e");
      ContinuePluginService params = ContinuePluginActionsKt.getContinuePluginService(e.getProject());
      if (params != null) {
         ContinuePluginService continuePluginService = params;
         Pair[] var4 = new Pair[]{TuplesKt.to("path", "/history"), TuplesKt.to("toggle", true)};
         Map var5 = MapsKt.mapOf(var4);
         ContinuePluginService.sendToWebview$default(continuePluginService, "navigateTo", var5, (String)null, 4, (Object)null);
      }
   }
}
