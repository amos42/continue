package com.github.continuedev.continueeclipseextension.toolWindow;

import com.github.continuedev.continueeclipseextension.activities.ContinuePluginDisposable;
import com.github.continuedev.continueeclipseextension.constants.MessageTypes;
import com.github.continuedev.continueeclipseextension.CoreMessenger;
import com.github.continuedev.continueeclipseextension.IdeProtocolClient;
import com.github.continuedev.continueeclipseextension.factories.CustomSchemeHandlerFactory;
import com.github.continuedev.continueeclipseextension.services.ContinueExtensionSettings;
import com.github.continuedev.continueeclipseextension.services.ContinuePluginService;
import com.github.continuedev.continueeclipseextension.utils.UtilsKt;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.jcef.JBCefBrowser;
import com.intellij.ui.jcef.JBCefBrowserBase;
import com.intellij.ui.jcef.JBCefBrowserJsCallKt;
import com.intellij.ui.jcef.JBCefJSQuery;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.cef.CefApp;
import org.cef.browser.CefBrowser;
import org.cef.callback.CefSchemeHandlerFactory;
import org.cef.handler.CefLoadHandler;
import org.cef.handler.CefLoadHandlerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.concurrency.Promise;

public final class ContinueBrowser {
   @NotNull
   private final Project project;
   @NotNull
   private final JBCefBrowser browser;

   public ContinueBrowser(@NotNull Project project, @NotNull String url) {
      Intrinsics.checkNotNullParameter(project, "project");
      Intrinsics.checkNotNullParameter(url, "url");
      super();
      this.project = project;
      boolean isOSREnabled = ((ContinueExtensionSettings)ServiceManager.getService(ContinueExtensionSettings.class)).getContinueState().getEnableOSR();
      JBCefBrowser myJSQueryOpenInBrowser = JBCefBrowser.createBuilder().setOffScreenRendering(isOSREnabled).build();
      Intrinsics.checkNotNullExpressionValue(myJSQueryOpenInBrowser, "build(...)");
      int var6 = 0;
      myJSQueryOpenInBrowser.getJBCefClient().setProperty("JBCefClient.JSQuery.poolSize", 200);
      Unit var8 = Unit.INSTANCE;
      this.browser = myJSQueryOpenInBrowser;
      this.registerAppSchemeHandler();
      this.browser.loadURL(url);
      Disposer.register((Disposable)ContinuePluginDisposable.Companion.getInstance(this.project), (Disposable)this.browser);
      JBCefBrowserBase var10000 = (JBCefBrowserBase)this.browser;
      Intrinsics.checkNotNull(var10000);
      JBCefJSQuery $this$_init__u24lambda_u240 = JBCefJSQuery.create(var10000);
      Intrinsics.checkNotNullExpressionValue($this$_init__u24lambda_u240, "create(...)");
      final JBCefJSQuery myJSQueryOpenInBrowser = $this$_init__u24lambda_u240;
      JBCefJSQuery var11 = $this$_init__u24lambda_u240;
      Function1 var10 = new Function1() {
         public final JBCefJSQuery.Response invoke(String msg) {
            JsonParser parser = new JsonParser();
            JsonObject messageType = parser.parse(msg).getAsJsonObject();
            Intrinsics.checkNotNullExpressionValue(messageType, "getAsJsonObject(...)");
            JsonObject json = messageType;
            final String messageType = messageType.get("messageType").getAsString();
            JsonElement data = json.get("data");
            JsonElement continuePluginService = json.get("messageId");
            final String messageId = continuePluginService != null ? continuePluginService.getAsString() : null;
            ContinuePluginService continuePluginService = (ContinuePluginService)ServiceManager.getService(ContinueBrowser.this.getProject(), ContinuePluginService.class);
            Function1 respond = new Function1() {
               public final void invoke(Object data) {
                  ContinueBrowser var10000 = ContinueBrowser.this;
                  String var2 = messageType;
                  Intrinsics.checkNotNull(var2);
                  String var10001 = var2;
                  var2 = messageId;
                  var10000.sendToWebview(var10001, data, var2 != null ? var2 : UtilsKt.uuid());
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object invoke(Object p1) {
                  this.invoke(p1);
                  return Unit.INSTANCE;
               }
            };
            if (MessageTypes.Companion.getPASS_THROUGH_TO_CORE().contains(messageType)) {
               CoreMessenger var13 = continuePluginService.getCoreMessenger();
               if (var13 != null) {
                  Intrinsics.checkNotNull(messageType);
                  var13.request(messageType, data, messageId, respond);
               }

               return null;
            } else {
               Function1 respondToWebview = new Function1() {
                  public final void invoke(Object data) {
                     ContinueBrowser var10000 = ContinueBrowser.this;
                     String var2 = messageType;
                     Intrinsics.checkNotNull(var2);
                     String var10001 = var2;
                     Pair[] var3 = new Pair[]{TuplesKt.to("status", "success"), TuplesKt.to("content", data), TuplesKt.to("done", true)};
                     Map var10002 = MapsKt.mapOf(var3);
                     String var4 = messageId;
                     var10000.sendToWebview(var10001, var10002, var4 != null ? var4 : UtilsKt.uuid());
                  }

                  // $FF: synthetic method
                  // $FF: bridge method
                  public Object invoke(Object p1) {
                     this.invoke(p1);
                     return Unit.INSTANCE;
                  }
               };
               if (msg != null) {
                  IdeProtocolClient var10 = continuePluginService.getIdeProtocolClient();
                  if (var10 != null) {
                     var10.handleMessage(msg, respondToWebview);
                  }
               }

               return null;
            }
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object invoke(Object p1) {
            return this.invoke((String)p1);
         }
      };
      var11.addHandler(ContinueBrowser::_init_$lambda$1);
      this.browser.getJBCefClient().addLoadHandler((CefLoadHandler)(new CefLoadHandlerAdapter() {
         public void onLoadingStateChange(CefBrowser browser, boolean isLoading, boolean canGoBack, boolean canGoForward) {
            if (!isLoading) {
               ContinueBrowser.this.executeJavaScript(browser, myJSQueryOpenInBrowser);
            }

         }
      }), this.browser.getCefBrowser());
   }

   @NotNull
   public final Project getProject() {
      return this.project;
   }

   private final void registerAppSchemeHandler() {
      CefApp.getInstance().registerSchemeHandlerFactory("http", "continue", (CefSchemeHandlerFactory)(new CustomSchemeHandlerFactory()));
   }

   @NotNull
   public final JBCefBrowser getBrowser() {
      return this.browser;
   }

   public final void executeJavaScript(@Nullable CefBrowser browser, @NotNull JBCefJSQuery myJSQueryOpenInBrowser) {
      Intrinsics.checkNotNullParameter(myJSQueryOpenInBrowser, "myJSQueryOpenInBrowser");
      String script = StringsKt.trimIndent("window.postIntellijMessage = function(messageType, data, messageId) {\n                const msg = JSON.stringify({messageType, data, messageId});\n                " + myJSQueryOpenInBrowser.inject("msg") + "\n            }");
      if (browser != null) {
         browser.executeJavaScript(script, browser.getURL(), 0);
      }

   }

   public final void sendToWebview(@NotNull String messageType, @Nullable Object data, @NotNull String messageId) {
      Intrinsics.checkNotNullParameter(messageType, "messageType");
      Intrinsics.checkNotNullParameter(messageId, "messageId");
      Gson var10000 = new Gson();
      Pair[] jsCode = new Pair[]{TuplesKt.to("messageId", messageId), TuplesKt.to("messageType", messageType), TuplesKt.to("data", data)};
      String jsonData = var10000.toJson(MapsKt.mapOf(jsCode));
      Intrinsics.checkNotNull(jsonData);
      String jsCode = this.buildJavaScript(jsonData);

      try {
         Promise var10 = JBCefBrowserJsCallKt.executeJavaScriptAsync(this.browser, jsCode);
         Function1 error = null.INSTANCE;
         var10.onError(ContinueBrowser::sendToWebview$lambda$2);
      } catch (IllegalStateException error) {
         String var7 = "Webview not initialized yet " + error;
         System.out.println(var7);
      }

   }

   // $FF: synthetic method
   public static void sendToWebview$default(ContinueBrowser var0, String var1, Object var2, String var3, int var4, Object var5) {
      if ((var4 & 4) != 0) {
         var3 = UtilsKt.uuid();
      }

      var0.sendToWebview(var1, var2, var3);
   }

   private final String buildJavaScript(String jsonData) {
      return "window.postMessage(" + jsonData + ", \"*\");";
   }

   private static final JBCefJSQuery.Response _init_$lambda$1(Function1 $tmp0, Object t) {
      return (JBCefJSQuery.Response)$tmp0.invoke(t);
   }

   private static final void sendToWebview$lambda$2(Function1 $tmp0, Object t) {
      $tmp0.invoke(t);
   }
}
