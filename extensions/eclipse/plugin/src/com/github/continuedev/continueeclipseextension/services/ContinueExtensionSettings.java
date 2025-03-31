package com.github.continuedev.continueeclipseextension.services;

import com.github.continuedev.continueeclipseextension.constants.ServerConstantsKt;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.concurrency.AppExecutorUtil;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import kotlinx.serialization.DeserializationStrategy;
import kotlinx.serialization.KSerializer;
import kotlinx.serialization.SerializersKt;
import kotlinx.serialization.StringFormat;
import kotlinx.serialization.json.Json;
import kotlinx.serialization.modules.SerializersModule;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
   name = "com.github.continuedev.continueintellijextension.services.ContinueExtensionSettings",
   storages = {@Storage("ContinueExtensionSettings.xml")}
)
@SourceDebugExtension({"SMAP\nContinueExtensionSettingsService.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ContinueExtensionSettingsService.kt\ncom/github/continuedev/continueintellijextension/services/ContinueExtensionSettings\n+ 2 SerialFormat.kt\nkotlinx/serialization/SerialFormatKt\n+ 3 Serializers.kt\nkotlinx/serialization/SerializersKt__SerializersKt\n+ 4 Platform.common.kt\nkotlinx/serialization/internal/Platform_commonKt\n*L\n1#1,289:1\n123#2:290\n32#3:291\n80#4:292\n*S KotlinDebug\n*F\n+ 1 ContinueExtensionSettingsService.kt\ncom/github/continuedev/continueintellijextension/services/ContinueExtensionSettings\n*L\n149#1:290\n149#1:291\n149#1:292\n*E\n"})
public class ContinueExtensionSettings implements PersistentStateComponent {
   @NotNull
   public static final Companion Companion = new Companion((DefaultConstructorMarker)null);
   @NotNull
   private ContinueState continueState = new ContinueState();
   @Nullable
   private ScheduledFuture remoteSyncFuture;

   @NotNull
   public final ContinueState getContinueState() {
      return this.continueState;
   }

   public final void setContinueState(@NotNull ContinueState var1) {
      Intrinsics.checkNotNullParameter(var1, "<set-?>");
      this.continueState = var1;
   }

   @NotNull
   public ContinueState getState() {
      return this.continueState;
   }

   public void loadState(@NotNull ContinueState state) {
      Intrinsics.checkNotNullParameter(state, "state");
      this.continueState = state;
   }

   private final void syncRemoteConfig() {
      ContinueState state = Companion.getInstance().continueState;
      if (state.getRemoteConfigServerUrl() != null) {
         String var10000 = state.getRemoteConfigServerUrl();
         Intrinsics.checkNotNull(var10000);
         if (((CharSequence)var10000).length() > 0) {
            OkHttpClient client = new OkHttpClient();
            String requestBuilder = state.getRemoteConfigServerUrl();
            String baseUrl = requestBuilder != null ? StringsKt.removeSuffix(requestBuilder, (CharSequence)"/") : null;
            Request.Builder requestBuilder = (new Request.Builder()).url(baseUrl + "/sync");
            if (state.getUserToken() != null) {
               requestBuilder.addHeader("Authorization", "Bearer " + state.getUserToken());
            }

            Request request = requestBuilder.build();
            Object configResponse = null;

            try {
               Closeable file = (Closeable)client.newCall(request).execute();
               Throwable var8 = null;

               try {
                  label228: {
                     Response response = (Response)file;
                     int var10 = 0;
                     if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                     }

                     ResponseBody var11 = response.body();
                     if (var11 != null) {
                        String var12 = var11.string();
                        if (var12 != null) {
                           String responseBody = var12;
                           int var15 = 0;

                           try {
                              StringFormat $this$decodeFromString$iv = (StringFormat)Json.Default;
                              int $i$f$decodeFromString = 0;
                              SerializersModule $this$serializer$iv$iv = $this$decodeFromString$iv.getSerializersModule();
                              int $i$f$serializer = 0;
                              KSerializer $this$cast$iv$iv$iv = SerializersKt.serializer($this$serializer$iv$iv, Reflection.typeOf(ContinueRemoteConfigSyncResponse.class));
                              int $i$f$cast = 0;
                              Intrinsics.checkNotNull($this$cast$iv$iv$iv, "null cannot be cast to non-null type kotlinx.serialization.KSerializer<T of kotlinx.serialization.internal.Platform_commonKt.cast>");
                              configResponse = $this$decodeFromString$iv.decodeFromString((DeserializationStrategy)$this$cast$iv$iv$iv, responseBody);
                           } catch (Exception e) {
                              e.printStackTrace();
                              return;
                           }

                           Unit var38 = Unit.INSTANCE;
                           break label228;
                        }
                     }

                     Object var37 = null;
                  }
               } catch (Throwable var27) {
                  var8 = var27;
                  throw var27;
               } finally {
                  CloseableKt.closeFinally(file, var8);
               }
            } catch (IOException e) {
               e.printStackTrace();
               return;
            }

            label181: {
               ContinueRemoteConfigSyncResponse var31 = (ContinueRemoteConfigSyncResponse)configResponse;
               if (var31 != null) {
                  String var35 = var31.getConfigJson();
                  if (var35 != null) {
                     var39 = ((CharSequence)var35).length() > 0;
                     break label181;
                  }
               }

               var39 = null;
            }

            Intrinsics.checkNotNull(var39);
            if (var39) {
               File file = new File(ServerConstantsKt.getConfigJsonPath(request.url().host()));
               Intrinsics.checkNotNull(configResponse);
               String var10001 = ((ContinueRemoteConfigSyncResponse)configResponse).getConfigJson();
               Intrinsics.checkNotNull(var10001);
               FilesKt.writeText$default(file, var10001, (Charset)null, 2, (Object)null);
            }

            label175: {
               ContinueRemoteConfigSyncResponse var33 = (ContinueRemoteConfigSyncResponse)configResponse;
               if (var33 != null) {
                  String var36 = var33.getConfigJs();
                  if (var36 != null) {
                     var40 = ((CharSequence)var36).length() > 0;
                     break label175;
                  }
               }

               var40 = null;
            }

            Intrinsics.checkNotNull(var40);
            if (var40) {
               File file = new File(ServerConstantsKt.getConfigJsPath(request.url().host()));
               Intrinsics.checkNotNull(configResponse);
               String var41 = ((ContinueRemoteConfigSyncResponse)configResponse).getConfigJs();
               Intrinsics.checkNotNull(var41);
               FilesKt.writeText$default(file, var41, (Charset)null, 2, (Object)null);
            }
         }
      }

   }

   public final void addRemoteSyncJob() {
      if (this.remoteSyncFuture != null) {
         ScheduledFuture var1 = this.remoteSyncFuture;
         if (var1 != null) {
            var1.cancel(false);
         }
      }

      Companion.getInstance().remoteSyncFuture = AppExecutorUtil.getAppScheduledExecutorService().scheduleWithFixedDelay(ContinueExtensionSettings::addRemoteSyncJob$lambda$2, 0L, (long)this.continueState.getRemoteConfigSyncPeriod(), TimeUnit.MINUTES);
   }

   private static final void addRemoteSyncJob$lambda$2(ContinueExtensionSettings this$0) {
      this$0.syncRemoteConfig();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object getState() {
      return this.getState();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void loadState(Object p0) {
      this.loadState((ContinueState)p0);
   }

   public static final class ContinueState {
      @Nullable
      private String lastSelectedInlineEditModel;
      private boolean shownWelcomeDialog;
      @Nullable
      private String remoteConfigServerUrl;
      private int remoteConfigSyncPeriod = 60;
      @Nullable
      private String userToken;
      private boolean enableTabAutocomplete = true;
      @Nullable
      private String ghAuthToken;
      private boolean enableContinueTeamsBeta;
      private boolean enableOSR = ContinueExtensionSettingsServiceKt.access$shouldRenderOffScreen();
      private boolean displayEditorTooltip = true;
      private boolean showIDECompletionSideBySide;
      @NotNull
      private String continueTestEnvironment = "production";

      @Nullable
      public final String getLastSelectedInlineEditModel() {
         return this.lastSelectedInlineEditModel;
      }

      public final void setLastSelectedInlineEditModel(@Nullable String var1) {
         this.lastSelectedInlineEditModel = var1;
      }

      public final boolean getShownWelcomeDialog() {
         return this.shownWelcomeDialog;
      }

      public final void setShownWelcomeDialog(boolean var1) {
         this.shownWelcomeDialog = var1;
      }

      @Nullable
      public final String getRemoteConfigServerUrl() {
         return this.remoteConfigServerUrl;
      }

      public final void setRemoteConfigServerUrl(@Nullable String var1) {
         this.remoteConfigServerUrl = var1;
      }

      public final int getRemoteConfigSyncPeriod() {
         return this.remoteConfigSyncPeriod;
      }

      public final void setRemoteConfigSyncPeriod(int var1) {
         this.remoteConfigSyncPeriod = var1;
      }

      @Nullable
      public final String getUserToken() {
         return this.userToken;
      }

      public final void setUserToken(@Nullable String var1) {
         this.userToken = var1;
      }

      public final boolean getEnableTabAutocomplete() {
         return this.enableTabAutocomplete;
      }

      public final void setEnableTabAutocomplete(boolean var1) {
         this.enableTabAutocomplete = var1;
      }

      @Nullable
      public final String getGhAuthToken() {
         return this.ghAuthToken;
      }

      public final void setGhAuthToken(@Nullable String var1) {
         this.ghAuthToken = var1;
      }

      public final boolean getEnableContinueTeamsBeta() {
         return this.enableContinueTeamsBeta;
      }

      public final void setEnableContinueTeamsBeta(boolean var1) {
         this.enableContinueTeamsBeta = var1;
      }

      public final boolean getEnableOSR() {
         return this.enableOSR;
      }

      public final void setEnableOSR(boolean var1) {
         this.enableOSR = var1;
      }

      public final boolean getDisplayEditorTooltip() {
         return this.displayEditorTooltip;
      }

      public final void setDisplayEditorTooltip(boolean var1) {
         this.displayEditorTooltip = var1;
      }

      public final boolean getShowIDECompletionSideBySide() {
         return this.showIDECompletionSideBySide;
      }

      public final void setShowIDECompletionSideBySide(boolean var1) {
         this.showIDECompletionSideBySide = var1;
      }

      @NotNull
      public final String getContinueTestEnvironment() {
         return this.continueTestEnvironment;
      }

      public final void setContinueTestEnvironment(@NotNull String var1) {
         Intrinsics.checkNotNullParameter(var1, "<set-?>");
         this.continueTestEnvironment = var1;
      }
   }

   public static final class Companion {
      private Companion() {
      }

      @NotNull
      public final ContinueExtensionSettings getInstance() {
         Object var1 = ServiceManager.getService(ContinueExtensionSettings.class);
         Intrinsics.checkNotNullExpressionValue(var1, "getService(...)");
         return (ContinueExtensionSettings)var1;
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker $constructor_marker) {
         this();
      }
   }
}