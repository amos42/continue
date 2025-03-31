package com.github.continuedev.continueeclipseextension.activities;

import com.github.continuedev.continueeclipseextension.constants.ServerConstantsKt;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.StreamUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Locale;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;

public final class ContinuePluginStartupActivityKt {
	public static final void showTutorial(@NotNull Project project) {
	   Intrinsics.checkNotNullParameter(project, "project");
	   String tutorialFileName = getTutorialFileName();
	   Closeable var2 = (Closeable)ContinuePluginStartupActivity.class.getClassLoader().getResourceAsStream(tutorialFileName);
	   Throwable var3 = null;
	
	   try {
	      InputStream is = (InputStream)var2;
	      int var5 = 0;
	      if (is == null) {
	         throw new IOException("Resource not found: " + tutorialFileName);
	      }
	
	      String filepath = StreamUtil.readText(is, StandardCharsets.UTF_8);
	      Intrinsics.checkNotNullExpressionValue(filepath, "readText(...)");
	      String content = StringsKt.replace$default(filepath, "[Cmd + L]", "[Cmd + J]", false, 4, (Object)null);
	      content = StringsKt.replace$default(content, "[Cmd + Shift + L]", "[Cmd + Shift + J]", false, 4, (Object)null);
	      filepath = System.getProperty("os.name");
	      Intrinsics.checkNotNullExpressionValue(filepath, "getProperty(...)");
	      String var10000 = filepath.toLowerCase(Locale.ROOT);
	      Intrinsics.checkNotNullExpressionValue(var10000, "this as java.lang.String).toLowerCase(Locale.ROOT)");
	      if (!StringsKt.contains$default((CharSequence)var10000, (CharSequence)"mac", false, 2, (Object)null)) {
	         content = StringsKt.replace$default(content, "[Cmd + J]", "[Ctrl + J]", false, 4, (Object)null);
	         content = StringsKt.replace$default(content, "[Cmd + Shift + J]", "[Ctrl + Shift + J]", false, 4, (Object)null);
	         content = StringsKt.replace$default(content, "[Cmd + I]", "[Ctrl + I]", false, 4, (Object)null);
	         content = StringsKt.replace$default(content, "⌘", "⌃", false, 4, (Object)null);
	      }
	
	      var10000 = ServerConstantsKt.getContinueGlobalPath();
	      String[] virtualFile = new String[]{tutorialFileName};
	      filepath = Paths.get(var10000, virtualFile).toString();
	      FilesKt.writeText$default(new File(filepath), content, (Charset)null, 2, (Object)null);
	      VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(filepath);
	      ApplicationManager.getApplication().invokeLater(ContinuePluginStartupActivityKt::showTutorial$lambda$1$lambda$0);
	      Unit var13 = Unit.INSTANCE;
	   } catch (Throwable var11) {
	      var3 = var11;
	      throw var11;
	   } finally {
	      CloseableKt.closeFinally(var2, var3);
	   }
	
	}
	
	private static final String getTutorialFileName() {
	   String var1 = ApplicationNamesInfo.getInstance().getFullProductName();
	   Intrinsics.checkNotNullExpressionValue(var1, "getFullProductName(...)");
	   String var10000 = var1.toLowerCase(Locale.ROOT);
	   Intrinsics.checkNotNullExpressionValue(var10000, "this as java.lang.String).toLowerCase(Locale.ROOT)");
	   String appName = var10000;
	   return StringsKt.contains$default((CharSequence)appName, (CharSequence)"intellij", false, 2, (Object)null) ? "continue_tutorial.java" : (StringsKt.contains$default((CharSequence)appName, (CharSequence)"pycharm", false, 2, (Object)null) ? "continue_tutorial.py" : (StringsKt.contains$default((CharSequence)appName, (CharSequence)"webstorm", false, 2, (Object)null) ? "continue_tutorial.ts" : "continue_tutorial.py"));
	}
	
	private static final void showTutorial$lambda$1$lambda$0(VirtualFile $virtualFile, Project $project) {
	   if ($virtualFile != null) {
	      FileEditorManager.getInstance($project).openFile($virtualFile, true);
	   }
	
	}
}
