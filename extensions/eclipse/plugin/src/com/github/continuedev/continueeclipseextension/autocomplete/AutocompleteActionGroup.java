package com.github.continuedev.continueeclipseextension.autocomplete;

import com.github.continuedev.continueeclipseextension.services.ContinueExtensionSettings;
import com.intellij.codeWithMe.ClientId;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import org.jetbrains.annotations.NotNull;

@SourceDebugExtension({"SMAP\nAutocompleteActionGroup.kt\nKotlin\n*S Kotlin\n*F\n+ 1 AutocompleteActionGroup.kt\ncom/github/continuedev/continueintellijextension/autocomplete/AutocompleteActionGroup\n+ 2 service.kt\ncom/intellij/openapi/components/ServiceKt\n*L\n1#1,29:1\n39#2,3:30\n*S KotlinDebug\n*F\n+ 1 AutocompleteActionGroup.kt\ncom/github/continuedev/continueintellijextension/autocomplete/AutocompleteActionGroup\n*L\n18#1:30,3\n*E\n"})
public final class AutocompleteActionGroup extends DefaultActionGroup {
   @NotNull
   public ActionUpdateThread getActionUpdateThread() {
      return ActionUpdateThread.EDT;
   }

   public void update(@NotNull AnActionEvent e) {
      Intrinsics.checkNotNullParameter(e, "e");
      super.update(e);
      this.removeAll();
      int $i$f$service = 0;
      Class serviceClass$iv = ContinueExtensionSettings.class;
      Object var5 = ApplicationManager.getApplication().getService(serviceClass$iv);
      if (var5 == null) {
         String var10002 = serviceClass$iv.getName();
         throw new RuntimeException("Cannot find service " + var10002 + " (classloader=" + serviceClass$iv.getClassLoader() + ", client=" + ClientId.Companion.getCurrentOrNull() + ")");
      } else {
         ContinueExtensionSettings continueSettingsService = (ContinueExtensionSettings)var5;
         if (continueSettingsService.getContinueState().getEnableTabAutocomplete()) {
            AnAction[] var6 = new AnAction[]{new DisableTabAutocompleteAction()};
            this.addAll(var6);
         } else {
            AnAction[] var7 = new AnAction[]{new EnableTabAutocompleteAction()};
            this.addAll(var7);
         }

      }
   }
}
