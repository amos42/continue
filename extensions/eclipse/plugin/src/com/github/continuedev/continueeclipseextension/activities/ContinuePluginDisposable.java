package com.github.continuedev.continueeclipseextension.activities;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.Service.Level;
import com.intellij.openapi.project.Project;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;


public final class ContinuePluginDisposable implements Disposable {
   @NotNull
   public static final Companion Companion = new Companion((DefaultConstructorMarker)null);

   public void dispose() {
   }

   @Metadata(
      mv = {1, 9, 0},
      k = 1,
      xi = 48,
      d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006¨\u0006\u0007"},
      d2 = {"Lcom/github/continuedev/continueintellijextension/activities/ContinuePluginDisposable$Companion;", "", "()V", "getInstance", "Lcom/github/continuedev/continueintellijextension/activities/ContinuePluginDisposable;", "project", "Lcom/intellij/openapi/project/Project;", "continue-intellij-extension"}
   )
   public static final class Companion {
      private Companion() {
      }

      @NotNull
      public final ContinuePluginDisposable getInstance() {
         Object var1 = ApplicationManager.getApplication().getService(ContinuePluginDisposable.class);
         Intrinsics.checkNotNullExpressionValue(var1, "getService(...)");
         return (ContinuePluginDisposable)var1;
      }

      @NotNull
      public final ContinuePluginDisposable getInstance(@NotNull Project project) {
         Intrinsics.checkNotNullParameter(project, "project");
         Object var2 = project.getService(ContinuePluginDisposable.class);
         Intrinsics.checkNotNullExpressionValue(var2, "getService(...)");
         return (ContinuePluginDisposable)var2;
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker $constructor_marker) {
         this();
      }
   }
}
