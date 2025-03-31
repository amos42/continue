/*
 * Copyright © 2017 jjYBdx4IL (https://github.com/jjYBdx4IL)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.continuedev.continueeclipseextension.utils;

import java.awt.Desktop.Action;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.text.StringsKt;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Desktop {
   @NotNull
   public static final Desktop INSTANCE = new Desktop();
   private static final Logger LOG = LoggerFactory.getLogger(Desktop.class);

   private Desktop() {
   }

   public final boolean browse(@NotNull URI uri) {
      Intrinsics.checkNotNullParameter(uri, "uri");
      if (this.browseDESKTOP(uri)) {
         return true;
      } else {
         String var2 = uri.toString();
         Intrinsics.checkNotNullExpressionValue(var2, "toString(...)");
         if (this.openSystemSpecific(var2)) {
            return true;
         } else {
            LOG.warn("failed to browse {}", uri);
            return false;
         }
      }
   }

   public final boolean open(@NotNull File file) {
      Intrinsics.checkNotNullParameter(file, "file");
      if (this.openDESKTOP(file)) {
         return true;
      } else {
         String var2 = file.getPath();
         Intrinsics.checkNotNullExpressionValue(var2, "getPath(...)");
         if (this.openSystemSpecific(var2)) {
            return true;
         } else {
            LOG.warn("failed to open {}", file.getAbsolutePath());
            return false;
         }
      }
   }

   public final boolean edit(@NotNull File file) {
      Intrinsics.checkNotNullParameter(file, "file");
      if (this.editDESKTOP(file)) {
         return true;
      } else {
         String var2 = file.getPath();
         Intrinsics.checkNotNullExpressionValue(var2, "getPath(...)");
         if (this.openSystemSpecific(var2)) {
            return true;
         } else {
            LOG.warn("failed to edit {}", file.getAbsolutePath());
            return false;
         }
      }
   }

   private final boolean openSystemSpecific(String what) {
      if (SystemUtils.IS_OS_LINUX) {
         if (this.isXDG() && this.runCommand("xdg-open", "%s", what)) {
            return true;
         }

         if (this.isKDE() && this.runCommand("kde-open", "%s", what)) {
            return true;
         }

         if (this.isGNOME() && this.runCommand("gnome-open", "%s", what)) {
            return true;
         }

         if (this.runCommand("kde-open", "%s", what)) {
            return true;
         }

         if (this.runCommand("gnome-open", "%s", what)) {
            return true;
         }
      }

      if (SystemUtils.IS_OS_MAC && this.runCommand("open", "%s", what)) {
         return true;
      } else {
         return SystemUtils.IS_OS_WINDOWS && this.runCommand("explorer", "%s", what);
      }
   }

   private final boolean browseDESKTOP(URI uri) {
      boolean var2;
      try {
         if (!java.awt.Desktop.isDesktopSupported()) {
            LOG.debug("Platform is not supported.");
            return false;
         }

         if (!java.awt.Desktop.getDesktop().isSupported(Action.BROWSE)) {
            LOG.debug("BROWSE is not supported.");
            return false;
         }

         LOG.info("Trying to use Desktop.getDesktop().browse() with {}", uri.toString());
         java.awt.Desktop.getDesktop().browse(uri);
         var2 = true;
      } catch (Throwable t) {
         LOG.error("Error using desktop browse.", t);
         var2 = false;
      }

      return var2;
   }

   private final boolean openDESKTOP(File file) {
      boolean var2;
      try {
         if (!java.awt.Desktop.isDesktopSupported()) {
            LOG.debug("Platform is not supported.");
            return false;
         }

         if (!java.awt.Desktop.getDesktop().isSupported(Action.OPEN)) {
            LOG.debug("OPEN is not supported.");
            return false;
         }

         LOG.info("Trying to use Desktop.getDesktop().open() with {}", file.toString());
         java.awt.Desktop.getDesktop().open(file);
         var2 = true;
      } catch (Throwable t) {
         LOG.error("Error using desktop open.", t);
         var2 = false;
      }

      return var2;
   }

   private final boolean editDESKTOP(File file) {
      boolean var2;
      try {
         if (!java.awt.Desktop.isDesktopSupported()) {
            LOG.debug("Platform is not supported.");
            return false;
         }

         if (!java.awt.Desktop.getDesktop().isSupported(Action.EDIT)) {
            LOG.debug("EDIT is not supported.");
            return false;
         }

         LOG.info("Trying to use Desktop.getDesktop().edit() with {}", file);
         java.awt.Desktop.getDesktop().edit(file);
         var2 = true;
      } catch (Throwable t) {
         LOG.error("Error using desktop edit.", t);
         var2 = false;
      }

      return var2;
   }

   private final boolean runCommand(String command, String args, String file) {
      Logger var10000 = LOG;
      Object[] parts = new Object[]{command, args, file};
      var10000.info("Trying to exec:\n   cmd = {}\n   args = {}\n   %s = {}", parts);
      String[] parts = this.prepareCommand(command, args, file);

      boolean p;
      try {
         Process p = Runtime.getRuntime().exec(parts);
         boolean var13;
         if (p == null) {
            var13 = false;
         } else {
            int retval;
            try {
               retval = p.exitValue();
               if (retval == 0) {
                  LOG.error("Process ended immediately.");
                  var13 = false;
               } else {
                  LOG.error("Process crashed.");
                  var13 = false;
               }

               retval = var13;
            } catch (IllegalThreadStateException var8) {
               LOG.error("Process is running.");
               retval = 1;
            }

            var13 = (boolean)retval;
         }

         p = var13;
      } catch (IOException e) {
         LOG.error("Error running command.", (Throwable)e);
         p = false;
      }

      return p;
   }

   private final String[] prepareCommand(String command, String args, String file) {
      List parts = (List)(new ArrayList());
      parts.add(command);
      if (args != null) {
         CharSequence var10000 = (CharSequence)args;
         String[] $i$f$toTypedArray = new String[]{" "};
         List $this$toTypedArray$iv = StringsKt.split$default(var10000, $i$f$toTypedArray, false, 0, 6, (Object)null);
         if ($this$toTypedArray$iv != null) {
            Iterable $this$forEach$iv = (Iterable)$this$toTypedArray$iv;
            int $i$f$forEach = 0;

            for(Object element$iv : $this$forEach$iv) {
               String s = (String)element$iv;
               int var11 = 0;
               StringCompanionObject var12 = StringCompanionObject.INSTANCE;
               Object[] var13 = new Object[]{file};
               String var10001 = String.format(s, Arrays.copyOf(var13, var13.length));
               Intrinsics.checkNotNullExpressionValue(var10001, "format(format, *args)");
               String var17 = var10001;
               parts.add(StringsKt.trim((CharSequence)var17).toString());
            }
         }
      }

      Collection $this$toTypedArray$iv = (Collection)parts;
      int $i$f$toTypedArray = 0;
      return (String[])$this$toTypedArray$iv.toArray(new String[0]);
   }

   private final boolean isXDG() {
      String xdgSessionId = System.getenv("XDG_SESSION_ID");
      CharSequence var2 = (CharSequence)xdgSessionId;
      return var2 != null && var2.length() != 0;
   }

   private final boolean isGNOME() {
      String gdmSession = System.getenv("GDMSESSION");
      boolean var3;
      if (gdmSession != null) {
         String var10000 = gdmSession.toLowerCase(Locale.ROOT);
         Intrinsics.checkNotNullExpressionValue(var10000, "this as java.lang.String).toLowerCase(Locale.ROOT)");
         String var2 = var10000;
         if (var2 != null) {
            var3 = StringsKt.contains$default((CharSequence)var2, (CharSequence)"gnome", false, 2, (Object)null);
            return var3;
         }
      }

      var3 = false;
      return var3;
   }

   private final boolean isKDE() {
      String gdmSession = System.getenv("GDMSESSION");
      boolean var3;
      if (gdmSession != null) {
         String var10000 = gdmSession.toLowerCase(Locale.ROOT);
         Intrinsics.checkNotNullExpressionValue(var10000, "this as java.lang.String).toLowerCase(Locale.ROOT)");
         String var2 = var10000;
         if (var2 != null) {
            var3 = StringsKt.contains$default((CharSequence)var2, (CharSequence)"kde", false, 2, (Object)null);
            return var3;
         }
      }

      var3 = false;
      return var3;
   }
}
