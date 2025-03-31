package com.github.continuedev.continueeclipseextension.services;

import com.github.continuedev.continueeclipseextension.CoreMessenger;
import com.github.continuedev.continueeclipseextension.CoreMessengerManager;
import com.github.continuedev.continueeclipseextension.DiffManager;
import com.github.continuedev.continueeclipseextension.IdeProtocolClient;
import com.github.continuedev.continueeclipseextension.toolWindow.ContinueBrowser;
import com.github.continuedev.continueeclipseextension.toolWindow.ContinuePluginToolWindowFactory;
import com.github.continuedev.continueeclipseextension.utils.UtilsKt;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.Service.Level;
import com.intellij.openapi.project.DumbAware;
import java.util.concurrent.CancellationException;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Dispatchers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Service({Level.PROJECT})
public final class ContinuePluginService implements Disposable, DumbAware {
   @NotNull
   private final CoroutineScope coroutineScope = CoroutineScopeKt.CoroutineScope((CoroutineContext)Dispatchers.getMain());
   @Nullable
   private ContinuePluginToolWindowFactory.ContinuePluginWindow continuePluginWindow;
   @Nullable
   private IdeProtocolClient ideProtocolClient;
   @Nullable
   private CoreMessengerManager coreMessengerManager;
   @Nullable
   private String[] workspacePaths;
   @NotNull
   private String windowId = UtilsKt.uuid();
   @Nullable
   private DiffManager diffManager;

   @Nullable
   public final ContinuePluginToolWindowFactory.ContinuePluginWindow getContinuePluginWindow() {
      return this.continuePluginWindow;
   }

   public final void setContinuePluginWindow(@Nullable ContinuePluginToolWindowFactory.ContinuePluginWindow var1) {
      this.continuePluginWindow = var1;
   }

   @Nullable
   public final IdeProtocolClient getIdeProtocolClient() {
      return this.ideProtocolClient;
   }

   public final void setIdeProtocolClient(@Nullable IdeProtocolClient var1) {
      this.ideProtocolClient = var1;
   }

   @Nullable
   public final CoreMessengerManager getCoreMessengerManager() {
      return this.coreMessengerManager;
   }

   public final void setCoreMessengerManager(@Nullable CoreMessengerManager var1) {
      this.coreMessengerManager = var1;
   }

   @Nullable
   public final CoreMessenger getCoreMessenger() {
      CoreMessengerManager var1 = this.coreMessengerManager;
      return var1 != null ? var1.getCoreMessenger() : null;
   }

   @Nullable
   public final String[] getWorkspacePaths() {
      return this.workspacePaths;
   }

   public final void setWorkspacePaths(@Nullable String[] var1) {
      this.workspacePaths = var1;
   }

   @NotNull
   public final String getWindowId() {
      return this.windowId;
   }

   public final void setWindowId(@NotNull String var1) {
      Intrinsics.checkNotNullParameter(var1, "<set-?>");
      this.windowId = var1;
   }

   @Nullable
   public final DiffManager getDiffManager() {
      return this.diffManager;
   }

   public final void setDiffManager(@Nullable DiffManager var1) {
      this.diffManager = var1;
   }

   public void dispose() {
      CoroutineScopeKt.cancel$default(this.coroutineScope, (CancellationException)null, 1, (Object)null);
      CoreMessenger var1 = this.getCoreMessenger();
      if (var1 != null) {
         CoroutineScope it = var1.getCoroutineScope();
         if (it != null) {
            int var5 = 0;
            CoroutineScopeKt.cancel$default(it, (CancellationException)null, 1, (Object)null);
            CoreMessenger var6 = this.getCoreMessenger();
            if (var6 != null) {
               var6.killSubProcess();
            }
         }
      }

   }

   public final void sendToWebview(@NotNull String messageType, @Nullable Object data, @NotNull String messageId) {
      Intrinsics.checkNotNullParameter(messageType, "messageType");
      Intrinsics.checkNotNullParameter(messageId, "messageId");
      ContinuePluginToolWindowFactory.ContinuePluginWindow var4 = this.continuePluginWindow;
      if (var4 != null) {
         ContinueBrowser var5 = var4.getBrowser();
         if (var5 != null) {
            var5.sendToWebview(messageType, data, messageId);
         }
      }

   }

   // $FF: synthetic method
   public static void sendToWebview$default(ContinuePluginService var0, String var1, Object var2, String var3, int var4, Object var5) {
      if ((var4 & 4) != 0) {
         var3 = UtilsKt.uuid();
      }

      var0.sendToWebview(var1, var2, var3);
   }
}
