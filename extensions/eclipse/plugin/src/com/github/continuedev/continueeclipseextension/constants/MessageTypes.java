package com.github.continuedev.continueeclipseextension.constants;

import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

public final class MessageTypes {
   @NotNull
   public static final Companion Companion = new Companion((DefaultConstructorMarker)null);
   @NotNull
   private static final List ideMessageTypes;
   @NotNull
   private static final List PASS_THROUGH_TO_WEBVIEW;
   @NotNull
   private static final List PASS_THROUGH_TO_CORE;

   static {
      String[] var0 = new String[]{
    		  "readRangeInFile", "isTelemetryEnabled", "getUniqueId", "getWorkspaceConfigs", "getDiff", "getTerminalContents", "getWorkspaceDirs", "showLines", "writeFile", "fileExists", "showVirtualFile", "openFile", "runCommand", "saveFile", "readFile", "showDiff", "getOpenFiles", "getCurrentFile", "getPinnedFiles", "getSearchResults", "getProblems", "subprocess", "getBranch", "getTags", "getIdeInfo", "getIdeSettings", "getRepoName", "listDir", "getGitRootPath", "getFileStats", "insertAtCursor", "applyToFile", "getGitHubAuthToken", "setGitHubAuthToken", "getControlPlaneSessionInfo", "logoutOfControlPlane", "getTerminalContents", "showToast", "openUrl", "toggleDevTools", "showTutorial", "copyText", "jetbrains/isOSREnabled", "jetbrains/getColors", "jetbrains/onLoad"
      };
      ideMessageTypes = CollectionsKt.listOf(var0);
      var0 = new String[]{"configUpdate", "getDefaultModelTitle", "indexProgress", "indexing/statusUpdate", "addContextItem", "refreshSubmenuItems", "isContinueInputFocused", "didChangeAvailableProfiles", "setTTSActive", "getWebviewHistoryLength", "getCurrentSessionId", "docs/suggestions", "didCloseFiles", "didSelectOrganization"};
      PASS_THROUGH_TO_WEBVIEW = CollectionsKt.listOf(var0);
      var0 = new String[]{"abort", "history/list", "history/delete", "history/load", "history/save", "devdata/log", "config/addModel", "config/addContextProvider", "config/newPromptFile", "config/ideSettingsUpdate", "config/getSerializedProfileInfo", "config/deleteModel", "config/listProfiles", "config/refreshProfiles", "config/openProfile", "config/updateSharedConfig", "config/updateSelectedModel", "context/getContextItems", "context/getSymbolsForFiles", "context/loadSubmenuItems", "context/addDocs", "context/removeDocs", "context/indexDocs", "autocomplete/complete", "autocomplete/cancel", "autocomplete/accept", "tts/kill", "llm/complete", "llm/streamChat", "llm/listModels", "streamDiffLines", "chatDescriber/describe", "stats/getTokensPerDay", "stats/getTokensPerModel", "index/setPaused", "index/forceReIndex", "index/forceReIndexFiles", "index/indexingProgressBarInitialized", "indexing/reindex", "indexing/abort", "indexing/setPaused", "docs/getSuggestedDocs", "docs/initStatuses", "docs/getDetails", "completeOnboarding", "addAutocompleteModel", "didChangeSelectedProfile", "didChangeSelectedOrg", "tools/call", "controlPlane/openUrl", "controlPlane/listOrganizations"};
      PASS_THROUGH_TO_CORE = CollectionsKt.listOf(var0);
   }

   public static final class Companion {
      private Companion() {
      }

      @NotNull
      public final List getIdeMessageTypes() {
         return MessageTypes.ideMessageTypes;
      }

      @NotNull
      public final List getPASS_THROUGH_TO_WEBVIEW() {
         return MessageTypes.PASS_THROUGH_TO_WEBVIEW;
      }

      @NotNull
      public final List getPASS_THROUGH_TO_CORE() {
         return MessageTypes.PASS_THROUGH_TO_CORE;
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker $constructor_marker) {
         this();
      }
   }
}
