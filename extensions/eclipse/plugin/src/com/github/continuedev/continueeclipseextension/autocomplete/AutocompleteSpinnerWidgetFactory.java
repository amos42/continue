package com.github.continuedev.continueeclipseextension.autocomplete;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.StatusBarWidgetFactory;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

public final class AutocompleteSpinnerWidgetFactory implements StatusBarWidgetFactory {
   @NotNull
   public final AutocompleteSpinnerWidget create(@NotNull Project project) {
      Intrinsics.checkNotNullParameter(project, "project");
      return new AutocompleteSpinnerWidget(project);
   }

   @NotNull
   public String getId() {
      return "AutocompleteSpinnerWidget";
   }

   @NotNull
   public String getDisplayName() {
      return "Continue Autocomplete";
   }

   public boolean isAvailable(@NotNull Project p0) {
      Intrinsics.checkNotNullParameter(p0, "p0");
      return true;
   }

   @NotNull
   public StatusBarWidget createWidget(@NotNull Project project) {
      Intrinsics.checkNotNullParameter(project, "project");
      return (StatusBarWidget)this.create(project);
   }

   public void disposeWidget(@NotNull StatusBarWidget p0) {
      Intrinsics.checkNotNullParameter(p0, "p0");
      Disposer.dispose((Disposable)p0);
   }

   public boolean canBeEnabledOn(@NotNull StatusBar p0) {
      Intrinsics.checkNotNullParameter(p0, "p0");
      return true;
   }
}
