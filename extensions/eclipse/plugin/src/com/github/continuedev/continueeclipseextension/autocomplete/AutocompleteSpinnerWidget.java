package com.github.continuedev.continueeclipseextension.autocomplete;

import com.github.continuedev.continueeclipseextension.activities.ContinuePluginDisposable;
import com.github.continuedev.continueeclipseextension.services.ContinueExtensionSettings;
import com.intellij.codeWithMe.ClientId;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.wm.impl.status.EditorBasedWidget;
import com.intellij.ui.AnimatedIcon;
import com.intellij.util.Consumer;
import javax.swing.Icon;
import javax.swing.JLabel;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class AutocompleteSpinnerWidget extends EditorBasedWidget implements StatusBarWidget.IconPresentation, Disposable {
   @NotNull
   public static final Companion Companion = new Companion((DefaultConstructorMarker)null);
   @NotNull
   private final JLabel iconLabel;
   private boolean isLoading;
   @NotNull
   private final AnimatedIcon.Default animatedIcon;
   @NotNull
   public static final String ID = "AutocompleteSpinnerWidget";

   public AutocompleteSpinnerWidget(@NotNull Project project) {
      Intrinsics.checkNotNullParameter(project, "project");
      super(project);
      this.iconLabel = new JLabel();
      this.animatedIcon = new AnimatedIcon.Default();
      Disposer.register((Disposable)ContinuePluginDisposable.Companion.getInstance(project), this);
      this.updateIcon();
   }

   public final void show() {
      String var1 = "Showing autocomplete spinner widget";
      System.out.println(var1);
   }

   public void dispose() {
   }

   @NotNull
   public String ID() {
      return "AutocompleteSpinnerWidget";
   }

   @NotNull
   public String getTooltipText() {
      int $i$f$service = 0;
      Class serviceClass$iv = ContinueExtensionSettings.class;
      Object var4 = ApplicationManager.getApplication().getService(serviceClass$iv);
      if (var4 == null) {
         String var10002 = serviceClass$iv.getName();
         throw new RuntimeException("Cannot find service " + var10002 + " (classloader=" + serviceClass$iv.getClassLoader() + ", client=" + ClientId.Companion.getCurrentOrNull() + ")");
      } else {
         boolean enabled = ((ContinueExtensionSettings)var4).getState().getEnableTabAutocomplete();
         return enabled ? "Continue autocomplete enabled" : "Continue autocomplete disabled";
      }
   }

   @Nullable
   public Consumer getClickConsumer() {
      return null;
   }

   @NotNull
   public Icon getIcon() {
      Icon var10000;
      if (this.isLoading) {
         var10000 = (Icon)this.animatedIcon;
      } else {
         Icon var1 = IconLoader.getIcon("/icons/continue.svg", this.getClass());
         Intrinsics.checkNotNullExpressionValue(var1, "getIcon(...)");
         var10000 = var1;
      }

      return var10000;
   }

   public final void setLoading(boolean loading) {
      this.isLoading = loading;
      this.updateIcon();
   }

   private final void updateIcon() {
      this.iconLabel.setIcon(this.getIcon());
      StatusBar statusBar = WindowManager.getInstance().getStatusBar(this.getProject());
      if (statusBar != null) {
         statusBar.updateWidget(this.ID());
      }

   }

   public void install(@NotNull StatusBar statusBar) {
      Intrinsics.checkNotNullParameter(statusBar, "statusBar");
      this.updateIcon();
   }

   @NotNull
   public StatusBarWidget.WidgetPresentation getPresentation() {
      return (StatusBarWidget.WidgetPresentation)this;
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker $constructor_marker) {
         this();
      }
   }
}
