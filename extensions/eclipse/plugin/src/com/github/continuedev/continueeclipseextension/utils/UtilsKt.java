package com.github.continuedev.continueeclipseextension.utils;

import com.intellij.openapi.vfs.VirtualFile;
import java.net.NetworkInterface;
import java.net.URI;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Locale;
import java.util.UUID;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class UtilsKt {
   public static final int getMetaKey() {
      OS var0 = getOS();
      int var1 = UtilsKt.WhenMappings.$EnumSwitchMapping$0[var0.ordinal()];
      short var10000;
      switch (var1) {
         case 1:
            var10000 = 157;
            break;
         case 2:
            var10000 = 17;
            break;
         case 3:
            var10000 = 17;
            break;
         default:
            throw new NoWhenBranchMatchedException();
      }

      return var10000;
   }

   @NotNull
   public static final OS getOS() {
      String os = System.getProperty("os.name");
      Intrinsics.checkNotNullExpressionValue(os, "getProperty(...)");
      String var10000 = os.toLowerCase(Locale.ROOT);
      Intrinsics.checkNotNullExpressionValue(var10000, "this as java.lang.String).toLowerCase(Locale.ROOT)");
      String osName = var10000;
      OS os = !StringsKt.contains$default((CharSequence)osName, (CharSequence)"mac", false, 2, (Object)null) && !StringsKt.contains$default((CharSequence)osName, (CharSequence)"darwin", false, 2, (Object)null) ? (StringsKt.contains$default((CharSequence)osName, (CharSequence)"win", false, 2, (Object)null) ? OS.WINDOWS : (!StringsKt.contains$default((CharSequence)osName, (CharSequence)"nix", false, 2, (Object)null) && !StringsKt.contains$default((CharSequence)osName, (CharSequence)"nux", false, 2, (Object)null) && !StringsKt.contains$default((CharSequence)osName, (CharSequence)"aix", false, 2, (Object)null) ? OS.LINUX : OS.LINUX)) : OS.MAC;
      return os;
   }

   @NotNull
   public static final String getMetaKeyLabel() {
      OS var0 = getOS();
      int var1 = UtilsKt.WhenMappings.$EnumSwitchMapping$0[var0.ordinal()];
      String var10000;
      switch (var1) {
         case 1:
            var10000 = "⌘";
            break;
         case 2:
            var10000 = "^";
            break;
         case 3:
            var10000 = "^";
            break;
         default:
            throw new NoWhenBranchMatchedException();
      }

      return var10000;
   }

   @NotNull
   public static final String getAltKeyLabel() {
      OS var0 = getOS();
      int var1 = UtilsKt.WhenMappings.$EnumSwitchMapping$0[var0.ordinal()];
      String var10000;
      switch (var1) {
         case 1:
            var10000 = "⌥";
            break;
         case 2:
            var10000 = "Alt";
            break;
         case 3:
            var10000 = "Alt";
            break;
         default:
            throw new NoWhenBranchMatchedException();
      }

      return var10000;
   }

   @NotNull
   public static final String getShiftKeyLabel() {
      OS var0 = getOS();
      int var1 = UtilsKt.WhenMappings.$EnumSwitchMapping$0[var0.ordinal()];
      String var10000;
      switch (var1) {
         case 1:
            var10000 = "⇧";
            break;
         case 2:
         case 3:
            var10000 = "↑";
            break;
         default:
            throw new NoWhenBranchMatchedException();
      }

      return var10000;
   }

   @NotNull
   public static final String getMachineUniqueID() {
      StringBuilder sb = new StringBuilder();
      Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();

      while(networkInterfaces.hasMoreElements()) {
         NetworkInterface networkInterface = (NetworkInterface)networkInterfaces.nextElement();
         byte[] mac = networkInterface.getHardwareAddress();
         if (mac != null) {
            int i = 0;

            for(int var5 = mac.length; i < var5; ++i) {
               StringCompanionObject var6 = StringCompanionObject.INSTANCE;
               String var7 = "%02X%s";
               Object[] var8 = new Object[]{mac[i], i < mac.length - 1 ? "-" : ""};
               String var10001 = String.format(var7, Arrays.copyOf(var8, var8.length));
               Intrinsics.checkNotNullExpressionValue(var10001, "format(format, *args)");
               sb.append(var10001);
            }

            String var9 = sb.toString();
            Intrinsics.checkNotNullExpressionValue(var9, "toString(...)");
            return var9;
         }
      }

      return "No MAC Address Found";
   }

   @NotNull
   public static final String uuid() {
      String var0 = UUID.randomUUID().toString();
      Intrinsics.checkNotNullExpressionValue(var0, "toString(...)");
      return var0;
   }

   @Nullable
   public static final String toUriOrNull(@NotNull VirtualFile $this$toUriOrNull) {
      Intrinsics.checkNotNullParameter($this$toUriOrNull, "<this>");
      Path var1 = $this$toUriOrNull.getFileSystem().getNioPath($this$toUriOrNull);
      String var10000;
      if (var1 != null) {
         URI var2 = var1.toUri();
         if (var2 != null) {
            String var3 = var2.toString();
            if (var3 != null) {
               var10000 = StringsKt.removeSuffix(var3, (CharSequence)"/");
               return var10000;
            }
         }
      }

      var10000 = null;
      return var10000;
   }

   // $FF: synthetic class
   public class WhenMappings {
      // $FF: synthetic field
      public static final int[] $EnumSwitchMapping$0;

      static {
         int[] var0 = new int[OS.values().length];

         try {
            var0[OS.MAC.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[OS.WINDOWS.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[OS.LINUX.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
         }

         $EnumSwitchMapping$0 = var0;
      }
   }
}
