package com.github.continuedev.continueeclipseextension.auth;

import com.github.continuedev.continueeclipseextension.CoreMessenger;
import com.github.continuedev.continueeclipseextension.services.ContinueExtensionSettings;
import com.github.continuedev.continueeclipseextension.services.ContinuePluginService;
import com.google.gson.Gson;
import com.intellij.codeWithMe.ClientId;
import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.remoteServer.util.CloudConfigurationUtil;
import java.net.URL;
import java.util.Map;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.MapsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.DelayKt;
import kotlinx.coroutines.Dispatchers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Service
public final class ContinueAuthService {
   @NotNull
   public static final Companion Companion = new Companion((DefaultConstructorMarker)null);
   @NotNull
   private final CoroutineScope coroutineScope = CoroutineScopeKt.CoroutineScope((CoroutineContext)Dispatchers.getIO());
   @NotNull
   private static final String CREDENTIALS_USER = "ContinueAuthUser";
   @NotNull
   private static final String ACCESS_TOKEN_KEY = "ContinueAccessToken";
   @NotNull
   private static final String REFRESH_TOKEN_KEY = "ContinueRefreshToken";
   @NotNull
   private static final String ACCOUNT_ID_KEY = "ContinueAccountId";
   @NotNull
   private static final String ACCOUNT_LABEL_KEY = "ContinueAccountLabel";

   public ContinueAuthService() {
      int $i$f$service = 0;
      Class serviceClass$iv = ContinueExtensionSettings.class;
      Object var4 = ApplicationManager.getApplication().getService(serviceClass$iv);
      if (var4 == null) {
         String var10002 = serviceClass$iv.getName();
         throw new RuntimeException("Cannot find service " + var10002 + " (classloader=" + serviceClass$iv.getClassLoader() + ", client=" + ClientId.Companion.getCurrentOrNull() + ")");
      } else {
         ContinueExtensionSettings settings = (ContinueExtensionSettings)var4;
         if (settings.getContinueState().getEnableContinueTeamsBeta()) {
            this.setupRefreshTokenInterval();
         }

      }
   }

   private final String getControlPlaneUrl() {
      int $i$f$service = 0;
      Class serviceClass$iv = ContinueExtensionSettings.class;
      Object var4 = ApplicationManager.getApplication().getService(serviceClass$iv);
      if (var4 == null) {
         String var10002 = serviceClass$iv.getName();
         throw new RuntimeException("Cannot find service " + var10002 + " (classloader=" + serviceClass$iv.getClassLoader() + ", client=" + ClientId.Companion.getCurrentOrNull() + ")");
      } else {
         switch (env) {
            case "none":
               return "https://control-plane-api-service-i3dqylpbqa-uc.a.run.app";
            case "test":
               return "https://api-test.continue.dev";
            case "local":
               return "http://localhost:3001";
            case "production":
               return "https://api.continue.dev";
         }

         return "https://control-plane-api-service-i3dqylpbqa-uc.a.run.app";
      }
   }

   public final void startAuthFlow(@NotNull Project project, boolean useOnboarding) {
      Intrinsics.checkNotNullParameter(project, "project");
      this.openSignInPage(project, useOnboarding);
      ApplicationManager.getApplication().invokeLater(ContinueAuthService::startAuthFlow$lambda$0);
   }

   public final void signOut() {
      this.setAccessToken("");
      this.setRefreshToken("");
      this.setAccountId("");
      this.setAccountLabel("");
   }

   private final void updateRefreshToken(final String token) {
      BuildersKt.launch$default(this.coroutineScope, (CoroutineContext)null, (CoroutineStart)null, new Function2((Continuation)null) {
         int label;

         public final Object invokeSuspend(Object $result) {
            Exception var10000;
            label89: {
               Object var12 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
               Object var25;
               switch (this.label) {
                  case 0:
                     ResultKt.throwOnFailure($result);

                     try {
                        ContinueAuthService var26 = ContinueAuthService.this;
                        String var31 = token;
                        Continuation var10002 = (Continuation)this;
                        this.label = 1;
                        var25 = var26.refreshToken(var31, var10002);
                     } catch (Exception var14) {
                        var10000 = var14;
                        boolean var30 = false;
                        break label89;
                     }

                     if (var25 == var12) {
                        return var12;
                     }
                     break;
                  case 1:
                     try {
                        ResultKt.throwOnFailure($result);
                        var25 = $result;
                        break;
                     } catch (Exception var15) {
                        var10000 = var15;
                        boolean var10001 = false;
                        break label89;
                     }
                  default:
                     throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
               }

               try {
                  Map response = (Map)var25;
                  Intrinsics.checkNotNull(response);
                  Object refreshToken = response.get("accessToken");
                  String accessToken = refreshToken instanceof String ? (String)refreshToken : null;
                  Object user = response.get("refreshToken");
                  String refreshToken = user instanceof String ? (String)user : null;
                  Object firstName = response.get("user");
                  Map user = firstName instanceof Map ? (Map)firstName : null;
                  Object lastName = user != null ? user.get("firstName") : null;
                  String firstName = lastName instanceof String ? (String)lastName : null;
                  Object label = user != null ? user.get("lastName") : null;
                  String lastName = label instanceof String ? (String)label : null;
                  String label = firstName + ' ' + lastName;
                  Object email = user != null ? user.get("id") : null;
                  if (email instanceof String) {
                     String var27 = (String)email;
                  } else {
                     var25 = null;
                  }

                  Object sessionInfo = user != null ? user.get("email") : null;
                  String email = sessionInfo instanceof String ? (String)sessionInfo : null;
                  ContinueAuthService var29 = ContinueAuthService.this;
                  Intrinsics.checkNotNull(refreshToken);
                  var29.setRefreshToken(refreshToken);
                  Intrinsics.checkNotNull(accessToken);
                  Intrinsics.checkNotNull(email);
                  ControlPlaneSessionInfo sessionInfo = new ControlPlaneSessionInfo(accessToken, new ControlPlaneSessionInfo.Account(email, label));
                  ContinueAuthService.this.setControlPlaneSessionInfo(sessionInfo);
                  ((AuthListener)ApplicationManager.getApplication().getMessageBus().syncPublisher(AuthListener.Companion.getTOPIC())).handleUpdatedSessionInfo(sessionInfo);
                  return Unit.INSTANCE;
               } catch (Exception var13) {
                  var10000 = var13;
                  boolean var32 = false;
               }
            }

            Exception e = var10000;
            String accessToken = "Exception while refreshing token: " + e.getMessage();
            System.out.println(accessToken);
            return Unit.INSTANCE;
         }

         public final Continuation create(Object value, Continuation $completion) {
            return (Continuation)(new <anonymous constructor>($completion));
         }

         public final Object invoke(CoroutineScope p1, Continuation p2) {
            return ((<undefinedtype>)this.create(p1, p2)).invokeSuspend(Unit.INSTANCE);
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object invoke(Object p1, Object p2) {
            return this.invoke((CoroutineScope)p1, (Continuation)p2);
         }
      }, 3, (Object)null);
   }

   private final void setupRefreshTokenInterval() {
      BuildersKt.launch$default(this.coroutineScope, (CoroutineContext)null, (CoroutineStart)null, new Function2((Continuation)null) {
         int label;

         public final Object invokeSuspend(Object $result) {
            Object var3 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
               case 0:
                  ResultKt.throwOnFailure($result);
                  break;
               case 1:
                  ResultKt.throwOnFailure($result);
                  break;
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            Continuation var10001;
            do {
               String refreshToken = ContinueAuthService.this.getRefreshToken();
               if (refreshToken != null) {
                  ContinueAuthService.this.updateRefreshToken(refreshToken);
               }

               var10001 = (Continuation)this;
               this.label = 1;
            } while(DelayKt.delay(900000L, var10001) != var3);

            return var3;
         }

         public final Continuation create(Object value, Continuation $completion) {
            return (Continuation)(new <anonymous constructor>($completion));
         }

         public final Object invoke(CoroutineScope p1, Continuation p2) {
            return ((<undefinedtype>)this.create(p1, p2)).invokeSuspend(Unit.INSTANCE);
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object invoke(Object p1, Object p2) {
            return this.invoke((CoroutineScope)p1, (Continuation)p2);
         }
      }, 3, (Object)null);
   }

   private final Object refreshToken(final String refreshToken, Continuation $completion) {
      return BuildersKt.withContext((CoroutineContext)Dispatchers.getIO(), new Function2((Continuation)null) {
         int label;

         public final Object invokeSuspend(Object $result) {
            Object var12 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
               case 0:
                  ResultKt.throwOnFailure($result);
                  OkHttpClient client = new OkHttpClient();
                  URL url = (new URL(ContinueAuthService.this.getControlPlaneUrl())).toURI().resolve("/auth/refresh").toURL();
                  Map jsonBody = MapsKt.mapOf(TuplesKt.to("refreshToken", refreshToken));
                  String jsonString = (new Gson()).toJson(jsonBody);
                  RequestBody.Companion var10000 = RequestBody.Companion;
                  Intrinsics.checkNotNull(jsonString);
                  RequestBody requestBody = var10000.create(jsonString, MediaType.Companion.get("application/json"));
                  Request.Builder var14 = new Request.Builder();
                  Intrinsics.checkNotNull(url);
                  Request request = var14.url(url).post(requestBody).header("Content-Type", "application/json").build();
                  Response response = client.newCall(request).execute();
                  ResponseBody gson = response.body();
                  String responseBody = gson != null ? gson.string() : null;
                  Gson gson = new Gson();
                  Map responseMap = (Map)gson.fromJson(responseBody, Map.class);
                  return responseMap;
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
         }

         public final Continuation create(Object value, Continuation $completion) {
            return (Continuation)(new <anonymous constructor>($completion));
         }

         public final Object invoke(CoroutineScope p1, Continuation p2) {
            return ((<undefinedtype>)this.create(p1, p2)).invokeSuspend(Unit.INSTANCE);
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object invoke(Object p1, Object p2) {
            return this.invoke((CoroutineScope)p1, (Continuation)p2);
         }
      }, $completion);
   }

   private final void openSignInPage(Project project, boolean useOnboarding) {
      ComponentManager $this$service$iv = (ComponentManager)project;
      int $i$f$service = 0;
      Class serviceClass$iv = ContinuePluginService.class;
      Object var7 = $this$service$iv.getService(serviceClass$iv);
      if (var7 == null) {
         throw new IllegalStateException(("Cannot find service " + serviceClass$iv.getName() + " in " + $this$service$iv + " (classloader=" + serviceClass$iv.getClassLoader()).toString());
      } else {
         CoreMessenger coreMessenger = ((ContinuePluginService)var7).getCoreMessenger();
         if (coreMessenger != null) {
            coreMessenger.request("auth/getAuthUrl", MapsKt.mapOf(TuplesKt.to("useOnboarding", useOnboarding)), (String)null, null.INSTANCE);
         }

      }
   }

   private final String retrieveSecret(String key) {
      String attributes;
      try {
         CredentialAttributes attributes = CloudConfigurationUtil.createCredentialAttributes(key, "ContinueAuthUser");
         PasswordSafe passwordSafe = PasswordSafe.Companion.getInstance();
         Intrinsics.checkNotNull(attributes);
         Credentials credentials = passwordSafe.get(attributes);
         attributes = credentials != null ? credentials.getPasswordAsString() : null;
      } catch (Exception e) {
         String credentials = "Error retrieving secret for key " + key + ": " + e.getMessage();
         System.out.println(credentials);
         attributes = null;
      }

      return attributes;
   }

   private final void storeSecret(String key, String secret) {
      try {
         CredentialAttributes attributes = CloudConfigurationUtil.createCredentialAttributes(key, "ContinueAuthUser");
         PasswordSafe passwordSafe = PasswordSafe.Companion.getInstance();
         Credentials credentials = new Credentials("ContinueAuthUser", secret);
         Intrinsics.checkNotNull(attributes);
         passwordSafe.set(attributes, credentials);
      } catch (Exception e) {
         String passwordSafe = "Error storing secret for key " + key + ": " + e.getMessage();
         System.out.println(passwordSafe);
      }

   }

   private final String getAccessToken() {
      return this.retrieveSecret("ContinueAccessToken");
   }

   private final void setAccessToken(String token) {
      this.storeSecret("ContinueAccessToken", token);
   }

   private final String getRefreshToken() {
      return this.retrieveSecret("ContinueRefreshToken");
   }

   private final void setRefreshToken(String token) {
      this.storeSecret("ContinueRefreshToken", token);
   }

   @Nullable
   public final String getAccountId() {
      return PropertiesComponent.getInstance().getValue("ContinueAccountId");
   }

   public final void setAccountId(@NotNull String id) {
      Intrinsics.checkNotNullParameter(id, "id");
      PropertiesComponent.getInstance().setValue("ContinueAccountId", id);
   }

   @Nullable
   public final String getAccountLabel() {
      return PropertiesComponent.getInstance().getValue("ContinueAccountLabel");
   }

   public final void setAccountLabel(@NotNull String label) {
      Intrinsics.checkNotNullParameter(label, "label");
      PropertiesComponent.getInstance().setValue("ContinueAccountLabel", label);
   }

   @Nullable
   public final ControlPlaneSessionInfo loadControlPlaneSessionInfo() {
      String accessToken = this.getAccessToken();
      String accountId = this.getAccountId();
      String accountLabel = this.getAccountLabel();
      return accessToken != null && !Intrinsics.areEqual(accessToken, "") && accountId != null && accountLabel != null ? new ControlPlaneSessionInfo(accessToken, new ControlPlaneSessionInfo.Account(accountId, accountLabel)) : null;
   }

   public final void setControlPlaneSessionInfo(@NotNull ControlPlaneSessionInfo info) {
      Intrinsics.checkNotNullParameter(info, "info");
      this.setAccessToken(info.getAccessToken());
      this.setAccountId(info.getAccount().getId());
      this.setAccountLabel(info.getAccount().getLabel());
   }

   private static final void startAuthFlow$lambda$0(boolean $useOnboarding, final ContinueAuthService this$0) {
      ContinueAuthDialog dialog = new ContinueAuthDialog($useOnboarding, new Function1() {
         public final void invoke(String token) {
            Intrinsics.checkNotNullParameter(token, "token");
            this$0.updateRefreshToken(token);
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object invoke(Object p1) {
            this.invoke((String)p1);
            return Unit.INSTANCE;
         }
      });
      dialog.show();
   }

   public static final class Companion {
      private Companion() {
      }

      @NotNull
      public final ContinueAuthService getInstance() {
         int $i$f$service = 0;
         Class serviceClass$iv = ContinueAuthService.class;
         Object var3 = ApplicationManager.getApplication().getService(serviceClass$iv);
         if (var3 == null) {
            String var10002 = serviceClass$iv.getName();
            throw new RuntimeException("Cannot find service " + var10002 + " (classloader=" + serviceClass$iv.getClassLoader() + ", client=" + ClientId.Companion.getCurrentOrNull() + ")");
         } else {
            return (ContinueAuthService)var3;
         }
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker $constructor_marker) {
         this();
      }
   }
}
