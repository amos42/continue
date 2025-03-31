package com.github.continuedev.continueeclipseextension.type;

import java.util.List;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IDE {
   @Nullable
   Object getIdeInfo(@NotNull Continuation var1);

   @Nullable
   Object getIdeSettings(@NotNull Continuation var1);

   @Nullable
   Object getDiff(boolean var1, @NotNull Continuation var2);

   @Nullable
   Object getClipboardContent(@NotNull Continuation var1);

   @Nullable
   Object isTelemetryEnabled(@NotNull Continuation var1);

   @Nullable
   Object getUniqueId(@NotNull Continuation var1);

   @Nullable
   Object getTerminalContents(@NotNull Continuation var1);

   @Nullable
   Object getDebugLocals(int var1, @NotNull Continuation var2);

   @Nullable
   Object getTopLevelCallStackSources(int var1, int var2, @NotNull Continuation var3);

   @Nullable
   Object getAvailableThreads(@NotNull Continuation var1);

   @Nullable
   Object getWorkspaceDirs(@NotNull Continuation var1);

   @Nullable
   Object getWorkspaceConfigs(@NotNull Continuation var1);

   @Nullable
   Object fileExists(@NotNull String var1, @NotNull Continuation var2);

   @Nullable
   Object writeFile(@NotNull String var1, @NotNull String var2, @NotNull Continuation var3);

   @Nullable
   Object showVirtualFile(@NotNull String var1, @NotNull String var2, @NotNull Continuation var3);

   @Nullable
   Object getContinueDir(@NotNull Continuation var1);

   @Nullable
   Object openFile(@NotNull String var1, @NotNull Continuation var2);

   @Nullable
   Object openUrl(@NotNull String var1, @NotNull Continuation var2);

   @Nullable
   Object runCommand(@NotNull String var1, @NotNull Continuation var2);

   @Nullable
   Object saveFile(@NotNull String var1, @NotNull Continuation var2);

   @Nullable
   Object readFile(@NotNull String var1, @NotNull Continuation var2);

   @Nullable
   Object readRangeInFile(@NotNull String var1, @NotNull Range var2, @NotNull Continuation var3);

   @Nullable
   Object showLines(@NotNull String var1, int var2, int var3, @NotNull Continuation var4);

   @Nullable
   Object showDiff(@NotNull String var1, @NotNull String var2, int var3, @NotNull Continuation var4);

   @Nullable
   Object getOpenFiles(@NotNull Continuation var1);

   @Nullable
   Object getCurrentFile(@NotNull Continuation var1);

   @Nullable
   Object getPinnedFiles(@NotNull Continuation var1);

   @Nullable
   Object getSearchResults(@NotNull String var1, @NotNull Continuation var2);

   @Nullable
   Object subprocess(@NotNull String var1, @Nullable String var2, @NotNull Continuation var3);

   @Nullable
   Object getProblems(@Nullable String var1, @NotNull Continuation var2);

   @Nullable
   Object getBranch(@NotNull String var1, @NotNull Continuation var2);

   @Nullable
   Object getTags(@NotNull String var1, @NotNull Continuation var2);

   @Nullable
   Object getRepoName(@NotNull String var1, @NotNull Continuation var2);

   @Nullable
   Object showToast(@NotNull ToastType var1, @NotNull String var2, @NotNull Object[] var3, @NotNull Continuation var4);

   @Nullable
   Object getGitRootPath(@NotNull String var1, @NotNull Continuation var2);

   @Nullable
   Object listDir(@NotNull String var1, @NotNull Continuation var2);

   @Nullable
   Object getFileStats(@NotNull List var1, @NotNull Continuation var2);

   @Nullable
   Object getGitHubAuthToken(@NotNull GetGhTokenArgs var1, @NotNull Continuation var2);

   @Nullable
   Object gotoDefinition(@NotNull Location var1, @NotNull Continuation var2);

   void onDidChangeActiveTextEditor(@NotNull Function1 var1);

   public static final class DefaultImpls {
      // $FF: synthetic method
      public static Object subprocess$default(IDE var0, String var1, String var2, Continuation var3, int var4, Object var5) {
         if (var5 != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: subprocess");
         } else {
            if ((var4 & 2) != 0) {
               var2 = null;
            }

            return var0.subprocess(var1, var2, var3);
         }
      }

      // $FF: synthetic method
      public static Object getProblems$default(IDE var0, String var1, Continuation var2, int var3, Object var4) {
         if (var4 != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: getProblems");
         } else {
            if ((var3 & 1) != 0) {
               var1 = null;
            }

            return var0.getProblems(var1, var2);
         }
      }
   }
}
