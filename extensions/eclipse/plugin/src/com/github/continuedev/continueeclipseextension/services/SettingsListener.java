package com.github.continuedev.continueeclipseextension.services;

import com.intellij.util.messages.Topic;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

public interface SettingsListener {
   @NotNull
   Companion Companion = SettingsListener.Companion.$$INSTANCE;

   void settingsUpdated(@NotNull ContinueExtensionSettings.ContinueState var1);

   public static final class Companion {
      // $FF: synthetic field
      static final Companion $$INSTANCE = new Companion();
      @NotNull
      private static final Topic TOPIC;

      private Companion() {
      }

      @NotNull
      public final Topic getTOPIC() {
         return TOPIC;
      }

      static {
         Topic var0 = Topic.create("SettingsUpdate", SettingsListener.class);
         Intrinsics.checkNotNullExpressionValue(var0, "create(...)");
         TOPIC = var0;
      }
   }
}