package com.github.continuedev.continueeclipseextension.activities;

import com.github.continuedev.continueeclipseextension.auth.AuthListener;
import com.github.continuedev.continueeclipseextension.auth.ContinueAuthService;
import com.github.continuedev.continueeclipseextension.auth.ControlPlaneSessionInfo;
import com.github.continuedev.continueeclipseextension.CoreMessenger;
import com.github.continuedev.continueeclipseextension.CoreMessengerManager;
import com.github.continuedev.continueeclipseextension.DiffManager;
import com.github.continuedev.continueeclipseextension.GetTheme;
import com.github.continuedev.continueeclipseextension.IdeProtocolClient;
import com.github.continuedev.continueeclipseextension.listeners.ContinuePluginSelectionListener;
import com.github.continuedev.continueeclipseextension.services.ContinueExtensionSettings;
import com.github.continuedev.continueeclipseextension.services.ContinuePluginService;
import com.github.continuedev.continueeclipseextension.services.SettingsListener;
import com.github.continuedev.continueeclipseextension.utils.UtilsKt;
import com.intellij.codeWithMe.ClientId;
import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.LafManagerListener;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.KeyboardShortcut;
import com.intellij.openapi.actionSystem.Shortcut;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.SelectionListener;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.KeymapManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileContentChangeEvent;
import com.intellij.openapi.vfs.newvfs.events.VFileCreateEvent;
import com.intellij.openapi.vfs.newvfs.events.VFileDeleteEvent;
import com.intellij.util.messages.MessageBusConnection;
import com.intellij.util.messages.Topic;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.KeyStroke;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Dispatchers;
import org.jetbrains.annotations.NotNull;

public final class ContinuePluginStartupActivity implements StartupActivity, DumbAware {
   public void runActivity(@NotNull Project project) {
      Intrinsics.checkNotNullParameter(project, "project");
      this.removeShortcutFromAction(this.getPlatformSpecificKeyStroke("J"));
      this.removeShortcutFromAction(this.getPlatformSpecificKeyStroke("shift J"));
      this.removeShortcutFromAction(this.getPlatformSpecificKeyStroke("I"));
      this.initializePlugin(project);
   }

   private final String getPlatformSpecificKeyStroke(String key) {
      String modifier = System.getProperty("os.name");
      Intrinsics.checkNotNullExpressionValue(modifier, "getProperty(...)");
      String var10000 = modifier.toLowerCase();
      Intrinsics.checkNotNullExpressionValue(var10000, "this as java.lang.String).toLowerCase()");
      String osName = var10000;
      modifier = StringsKt.contains$default((CharSequence)osName, (CharSequence)"mac", false, 2, (Object)null) ? "meta" : "control";
      return modifier + ' ' + key;
   }

   private final void removeShortcutFromAction(String shortcut) {
      Keymap keyStroke = KeymapManager.getInstance().getActiveKeymap();
      Intrinsics.checkNotNullExpressionValue(keyStroke, "getActiveKeymap(...)");
      Keymap keymap = keyStroke;
      KeyStroke keyStroke = KeyStroke.getKeyStroke(shortcut);
      String[] $this$any$iv = keyStroke.getActionIds(keyStroke);
      Intrinsics.checkNotNullExpressionValue($this$any$iv, "getActionIds(...)");
      String[] actionIds = $this$any$iv;
      Object[] $this$any$iv = $this$any$iv;
      int $i$f$any = 0;
      int actionId = 0;
      int shortcuts = $this$any$iv.length;

      boolean var10000;
      while(true) {
         if (actionId >= shortcuts) {
            var10000 = false;
            break;
         }

         Object element$iv = $this$any$iv[actionId];
         int shortcut = 0;
         Intrinsics.checkNotNull(element$iv);
         if (StringsKt.startsWith$default((String)element$iv, "continue", false, 2, (Object)null)) {
            var10000 = true;
            break;
         }

         ++actionId;
      }

      if (var10000) {
         int var14 = 0;

         for(int var15 = $this$any$iv.length; var14 < var15; ++var14) {
            String actionId = actionIds[var14];
            Intrinsics.checkNotNull(actionId);
            if (!StringsKt.startsWith$default(actionId, "continue", false, 2, (Object)null)) {
               Shortcut[] it = keymap.getShortcuts(actionId);
               Intrinsics.checkNotNullExpressionValue(it, "getShortcuts(...)");
               Shortcut[] shortcuts = it;
               int it = 0;

               for(int it = it.length; it < it; ++it) {
                  Shortcut shortcut = shortcuts[it];
                  if (shortcut instanceof KeyboardShortcut && Intrinsics.areEqual(((KeyboardShortcut)shortcut).getFirstKeyStroke(), keyStroke)) {
                     keymap.removeShortcut(actionId, shortcut);
                  }
               }
            }
         }

      }
   }

   private final void initializePlugin(final Project project) {
      final CoroutineScope coroutineScope = CoroutineScopeKt.CoroutineScope((CoroutineContext)Dispatchers.getIO());
      final ContinuePluginService continuePluginService = (ContinuePluginService)ServiceManager.getService(project, ContinuePluginService.class);
      BuildersKt.launch$default(coroutineScope, (CoroutineContext)null, (CoroutineStart)null, new Function2((Continuation)null) {
         int label;

         public final Object invokeSuspend(Object $result) {
            Object var42 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
               case 0:
                  ResultKt.throwOnFailure($result);
                  ContinueExtensionSettings settings = (ContinueExtensionSettings)ServiceManager.getService(ContinueExtensionSettings.class);
                  if (!settings.getContinueState().getShownWelcomeDialog()) {
                     settings.getContinueState().setShownWelcomeDialog(true);
                     ContinuePluginStartupActivityKt.showTutorial(project);
                  }

                  settings.addRemoteSyncJob();
                  ContinuePluginService diffManager = continuePluginService;
                  Intrinsics.checkNotNull(diffManager);
                  IdeProtocolClient ideProtocolClient = new IdeProtocolClient(diffManager, coroutineScope, project);
                  DiffManager diffManager = new DiffManager(project);
                  continuePluginService.setDiffManager(diffManager);
                  continuePluginService.setIdeProtocolClient(ideProtocolClient);
                  MessageBusConnection authService = ApplicationManager.getApplication().getMessageBus().connect();
                  Intrinsics.checkNotNullExpressionValue(authService, "connect(...)");
                  authService.subscribe(SettingsListener.Companion.getTOPIC(), new SettingsListener() {
                     public void settingsUpdated(ContinueExtensionSettings.ContinueState settings) {
                        Intrinsics.checkNotNullParameter(settings, "settings");
                        CoreMessenger var2 = continuePluginService.getCoreMessenger();
                        if (var2 != null) {
                           var2.request("config/ideSettingsUpdate", settings, (String)null, null.INSTANCE);
                        }

                        ContinuePluginService var3 = continuePluginService;
                        Intrinsics.checkNotNull(var3);
                        ContinuePluginService var10000 = var3;
                        Pair[] var4 = new Pair[]{TuplesKt.to("remoteConfigServerUrl", settings.getRemoteConfigServerUrl()), TuplesKt.to("remoteConfigSyncPeriod", settings.getRemoteConfigSyncPeriod()), TuplesKt.to("userToken", settings.getUserToken()), TuplesKt.to("enableControlServerBeta", settings.getEnableContinueTeamsBeta())};
                        ContinuePluginService.sendToWebview$default(var10000, "didChangeIdeSettings", MapsKt.mapOf(TuplesKt.to("settings", MapsKt.mapOf(var4))), (String)null, 4, (Object)null);
                     }
                  });
                  Topic var44 = VirtualFileManager.VFS_CHANGES;
                  Intrinsics.checkNotNullExpressionValue(var44, "VFS_CHANGES");
                  authService.subscribe(var44, new BulkFileListener() {
                     public void after(List events) {
                        Intrinsics.checkNotNullParameter(events, "events");
                        Iterable $this$filterIsInstance$iv = (Iterable)events;
                        int $i$f$filterIsInstance = 0;
                        Collection destination$iv$iv = (Collection)(new ArrayList());
                        int $i$f$filterIsInstanceTo = 0;

                        for(Object element$iv$iv : $this$filterIsInstance$iv) {
                           if (element$iv$iv instanceof VFileDeleteEvent) {
                              destination$iv$iv.add(element$iv$iv);
                           }
                        }

                        $this$filterIsInstance$iv = (Iterable)((List)destination$iv$iv);
                        $i$f$filterIsInstance = 0;
                        destination$iv$iv = (Collection)(new ArrayList());
                        $i$f$filterIsInstanceTo = 0;
                        int $i$f$forEach = 0;

                        for(Object element$iv$iv$iv : $this$filterIsInstance$iv) {
                           int element$iv$iv$iv = 0;
                           VFileDeleteEvent event = (VFileDeleteEvent)element$iv$iv$iv;
                           int var15 = 0;
                           VirtualFile event = event.getFile();
                           Intrinsics.checkNotNullExpressionValue(event, "getFile(...)");
                           String var10000 = UtilsKt.toUriOrNull(event);
                           if (var10000 != null) {
                              String it$iv$iv = var10000;
                              int it$iv$iv = 0;
                              destination$iv$iv.add(it$iv$iv);
                           }
                        }

                        List deletedURIs = (List)destination$iv$iv;
                        if (!((Collection)deletedURIs).isEmpty()) {
                           Map data = MapsKt.mapOf(TuplesKt.to("uris", deletedURIs));
                           CoreMessenger $this$forEach$iv$iv$iv = continuePluginService.getCoreMessenger();
                           if ($this$forEach$iv$iv$iv != null) {
                              $this$forEach$iv$iv$iv.request("files/deleted", data, (String)null, null.INSTANCE);
                           }
                        }

                        Iterable $this$filterIsInstance$iv = (Iterable)events;
                        int $i$f$filterIsInstance = 0;
                        Collection destination$iv$iv = (Collection)(new ArrayList());
                        int $i$f$filterIsInstanceTo = 0;

                        for(Object element$iv$iv : $this$filterIsInstance$iv) {
                           if (element$iv$iv instanceof VFileContentChangeEvent) {
                              destination$iv$iv.add(element$iv$iv);
                           }
                        }

                        $this$filterIsInstance$iv = (Iterable)((List)destination$iv$iv);
                        $i$f$filterIsInstance = 0;
                        destination$iv$iv = (Collection)(new ArrayList());
                        $i$f$filterIsInstanceTo = 0;
                        int $i$f$forEach = 0;

                        for(Object element$iv$iv$iv : $this$filterIsInstance$iv) {
                           int var63 = 0;
                           VFileContentChangeEvent event = (VFileContentChangeEvent)element$iv$iv$iv;
                           int var66 = 0;
                           VirtualFile it$iv$iv = event.getFile();
                           Intrinsics.checkNotNullExpressionValue(it$iv$iv, "getFile(...)");
                           String var72 = UtilsKt.toUriOrNull(it$iv$iv);
                           if (var72 != null) {
                              String it$iv$iv = var72;
                              int it$iv$iv = 0;
                              destination$iv$iv.add(it$iv$iv);
                           }
                        }

                        List changedURIs = (List)destination$iv$iv;
                        if (!((Collection)changedURIs).isEmpty()) {
                           Map data = MapsKt.mapOf(TuplesKt.to("uris", changedURIs));
                           CoreMessenger it = continuePluginService.getCoreMessenger();
                           if (it != null) {
                              it.request("files/changed", data, (String)null, null.INSTANCE);
                           }
                        }

                        Iterable $this$filterIsInstance$iv = (Iterable)events;
                        int $i$f$filterIsInstance = 0;
                        Collection destination$iv$iv = (Collection)(new ArrayList());
                        $i$f$forEach = 0;

                        for(Object element$iv$iv : $this$filterIsInstance$iv) {
                           if (element$iv$iv instanceof VFileCreateEvent) {
                              destination$iv$iv.add(element$iv$iv);
                           }
                        }

                        $this$filterIsInstance$iv = (Iterable)((List)destination$iv$iv);
                        $i$f$filterIsInstance = 0;
                        destination$iv$iv = (Collection)(new ArrayList());
                        $i$f$forEach = 0;
                        int $i$f$forEach = 0;

                        for(Object element$iv$iv$iv : $this$filterIsInstance$iv) {
                           int var65 = 0;
                           VFileCreateEvent event = (VFileCreateEvent)element$iv$iv$iv;
                           int var69 = 0;
                           VirtualFile it$iv$iv = event.getFile();
                           String var73 = it$iv$iv != null ? UtilsKt.toUriOrNull(it$iv$iv) : null;
                           if (var73 != null) {
                              String it$iv$iv = var73;
                              int var21 = 0;
                              destination$iv$iv.add(it$iv$iv);
                           }
                        }

                        List it = (List)destination$iv$iv;
                        int var43 = 0;
                        List $this$forEach$iv$iv$iv = !((Collection)it).isEmpty() ? it : null;
                        if ($this$forEach$iv$iv$iv != null) {
                           ContinuePluginService var39 = continuePluginService;
                           int var48 = 0;
                           Map data = MapsKt.mapOf(TuplesKt.to("uris", $this$forEach$iv$iv$iv));
                           CoreMessenger var57 = var39.getCoreMessenger();
                           if (var57 != null) {
                              var57.request("files/created", data, (String)null, null.INSTANCE);
                           }
                        }

                     }
                  });
                  Topic var45 = FileEditorManagerListener.FILE_EDITOR_MANAGER;
                  Intrinsics.checkNotNullExpressionValue(var45, "FILE_EDITOR_MANAGER");
                  authService.subscribe(var45, new FileEditorManagerListener() {
                     public void fileClosed(FileEditorManager source, VirtualFile file) {
                        Intrinsics.checkNotNullParameter(source, "source");
                        Intrinsics.checkNotNullParameter(file, "file");
                        String uri = UtilsKt.toUriOrNull(file);
                        if (uri != null) {
                           ContinuePluginService var5 = continuePluginService;
                           int var7 = 0;
                           Map data = MapsKt.mapOf(TuplesKt.to("uris", CollectionsKt.listOf(uri)));
                           CoreMessenger var9 = var5.getCoreMessenger();
                           if (var9 != null) {
                              var9.request("files/closed", data, (String)null, null.INSTANCE);
                           }
                        }

                     }

                     public void fileOpened(FileEditorManager source, VirtualFile file) {
                        Intrinsics.checkNotNullParameter(source, "source");
                        Intrinsics.checkNotNullParameter(file, "file");
                        String uri = UtilsKt.toUriOrNull(file);
                        if (uri != null) {
                           ContinuePluginService var5 = continuePluginService;
                           int var7 = 0;
                           Map data = MapsKt.mapOf(TuplesKt.to("uris", CollectionsKt.listOf(uri)));
                           CoreMessenger var9 = var5.getCoreMessenger();
                           if (var9 != null) {
                              var9.request("files/opened", data, (String)null, null.INSTANCE);
                           }
                        }

                     }
                  });
                  Topic var46 = LafManagerListener.TOPIC;
                  Intrinsics.checkNotNullExpressionValue(var46, "TOPIC");
                  authService.subscribe(var46, <undefinedtype>::invokeSuspend$lambda$0);
                  int $i$f$service = 0;
                  Class serviceClass$iv = ContinueAuthService.class;
                  Object coreMessengerManager = ApplicationManager.getApplication().getService(serviceClass$iv);
                  if (coreMessengerManager == null) {
                     String var10002 = serviceClass$iv.getName();
                     throw new RuntimeException("Cannot find service " + var10002 + " (classloader=" + serviceClass$iv.getClassLoader() + ", client=" + ClientId.Companion.getCurrentOrNull() + ")");
                  }

                  final ContinueAuthService authService = (ContinueAuthService)coreMessengerManager;
                  ControlPlaneSessionInfo initialSessionInfo = authService.loadControlPlaneSessionInfo();
                  if (initialSessionInfo != null) {
                     Map data = MapsKt.mapOf(TuplesKt.to("sessionInfo", initialSessionInfo));
                     CoreMessenger pluginService = continuePluginService.getCoreMessenger();
                     if (pluginService != null) {
                        pluginService.request("didChangeControlPlaneSessionInfo", data, (String)null, null.INSTANCE);
                     }

                     ContinuePluginService pluginService = continuePluginService;
                     Intrinsics.checkNotNull(pluginService);
                     ContinuePluginService.sendToWebview$default(pluginService, "didChangeControlPlaneSessionInfo", data, (String)null, 4, (Object)null);
                  }

                  authService.subscribe(AuthListener.Companion.getTOPIC(), new AuthListener() {
                     public void startAuthFlow() {
                        authService.startAuthFlow(project, false);
                     }

                     public void handleUpdatedSessionInfo(ControlPlaneSessionInfo sessionInfo) {
                        Map data = MapsKt.mapOf(TuplesKt.to("sessionInfo", sessionInfo));
                        CoreMessenger var3 = continuePluginService.getCoreMessenger();
                        if (var3 != null) {
                           var3.request("didChangeControlPlaneSessionInfo", data, (String)null, null.INSTANCE);
                        }

                        ContinuePluginService var4 = continuePluginService;
                        Intrinsics.checkNotNull(var4);
                        ContinuePluginService.sendToWebview$default(var4, "didChangeControlPlaneSessionInfo", data, (String)null, 4, (Object)null);
                     }
                  });
                  ContinuePluginSelectionListener listener = new ContinuePluginSelectionListener(coroutineScope);
                  ContinuePluginService pluginService = continuePluginService;
                  if (pluginService != null) {
                     Project var11 = project;
                     int var13 = 0;
                     Object[] $this$flatMap$iv = ModuleManager.Companion.getInstance(var11).getModules();
                     int $i$f$flatMap = 0;
                     Object $i$f$toTypedArray = $this$flatMap$iv;
                     Collection destination$iv$iv = (Collection)(new ArrayList());
                     int $i$f$flatMapTo = 0;
                     int $i$f$filterTo = 0;

                     for(int var20 = $this$flatMap$iv.length; $i$f$filterTo < var20; ++$i$f$filterTo) {
                        Object element$iv$iv = ((Object[])$i$f$toTypedArray)[$i$f$filterTo];
                        int var23 = 0;
                        VirtualFile[] $this$none$iv = ModuleRootManager.getInstance((Module)element$iv$iv).getContentRoots();
                        Intrinsics.checkNotNullExpressionValue($this$none$iv, "getContentRoots(...)");
                        Object[] $this$mapNotNull$iv = (Object[])$this$none$iv;
                        int $i$f$mapNotNull = 0;
                        Collection destination$iv$iv = (Collection)(new ArrayList());
                        int $i$f$mapNotNullTo = 0;
                        Object[] $this$forEach$iv$iv$iv = $this$mapNotNull$iv;
                        int $i$f$forEach = 0;
                        int var31 = 0;

                        for(int var32 = $this$mapNotNull$iv.length; var31 < var32; ++var31) {
                           Object element$iv$iv$iv = $this$forEach$iv$iv$iv[var31];
                           int var35 = 0;
                           VirtualFile it = (VirtualFile)element$iv$iv$iv;
                           int var37 = 0;
                           Intrinsics.checkNotNull(it);
                           String var72 = UtilsKt.toUriOrNull(it);
                           if (var72 != null) {
                              String it$iv$iv = var72;
                              int var40 = 0;
                              destination$iv$iv.add(it$iv$iv);
                           }
                        }

                        Iterable list$iv$iv = (Iterable)((List)destination$iv$iv);
                        CollectionsKt.addAll(destination$iv$iv, list$iv$iv);
                     }

                     List allModulePaths = (List)destination$iv$iv;
                     Iterable $this$filter$iv = (Iterable)allModulePaths;
                     int $i$f$filter = 0;
                     Collection destination$iv$iv = (Collection)(new ArrayList());
                     $i$f$filterTo = 0;

                     for(Object element$iv$iv : $this$filter$iv) {
                        String modulePath = (String)element$iv$iv;
                        int var65 = 0;
                        Iterable $this$none$iv = (Iterable)allModulePaths;
                        int $i$f$none = 0;
                        boolean var73;
                        if ($this$none$iv instanceof Collection && ((Collection)$this$none$iv).isEmpty()) {
                           var73 = true;
                        } else {
                           Iterator $this$mapNotNullTo$iv$iv = $this$none$iv.iterator();

                           while(true) {
                              if (!$this$mapNotNullTo$iv$iv.hasNext()) {
                                 var73 = true;
                                 break;
                              }

                              Object element$iv = $this$mapNotNullTo$iv$iv.next();
                              String it = (String)element$iv;
                              int var71 = 0;
                              if (!Intrinsics.areEqual(it, modulePath) && StringsKt.startsWith$default(modulePath, it, false, 2, (Object)null)) {
                                 var73 = false;
                                 break;
                              }
                           }
                        }

                        if (var73) {
                           destination$iv$iv.add(element$iv$iv);
                        }
                     }

                     List topLevelModulePaths = (List)destination$iv$iv;
                     Collection $this$toTypedArray$iv = (Collection)topLevelModulePaths;
                     $i$f$filter = 0;
                     pluginService.setWorkspacePaths((String[])$this$toTypedArray$iv.toArray(new String[0]));
                  }

                  EditorFactory.getInstance().getEventMulticaster().addSelectionListener((SelectionListener)listener, (Disposable)ContinuePluginDisposable.Companion.getInstance(project));
                  CoreMessengerManager coreMessengerManager = new CoreMessengerManager(project, ideProtocolClient, coroutineScope);
                  continuePluginService.setCoreMessengerManager(coreMessengerManager);
                  return Unit.INSTANCE;
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

         private static final void invokeSuspend$lambda$0(ContinuePluginService $continuePluginService, LafManager it) {
            Map colors = (new GetTheme()).getTheme();
            Intrinsics.checkNotNull($continuePluginService);
            ContinuePluginService.sendToWebview$default($continuePluginService, "jetbrains/setColors", colors, (String)null, 4, (Object)null);
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object invoke(Object p1, Object p2) {
            return this.invoke((CoroutineScope)p1, (Continuation)p2);
         }
      }, 3, (Object)null);
   }
}
