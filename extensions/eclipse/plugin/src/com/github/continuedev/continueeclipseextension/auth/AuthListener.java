package com.github.continuedev.continueeclipseextension.auth;

import com.intellij.util.messages.Topic;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface AuthListener {
   @NotNull
   Companion Companion = AuthListener.Companion.$$INSTANCE;

   void startAuthFlow();

   void handleUpdatedSessionInfo(@Nullable ControlPlaneSessionInfo var1);

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
         Topic var0 = Topic.create("StartAuthFlow", AuthListener.class);
         Intrinsics.checkNotNullExpressionValue(var0, "create(...)");
         TOPIC = var0;
      }
   }
}
