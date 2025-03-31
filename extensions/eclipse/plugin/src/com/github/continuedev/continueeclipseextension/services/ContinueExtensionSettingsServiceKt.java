package com.github.continuedev.continueeclipseextension.services;

import com.intellij.openapi.application.ApplicationInfo;

public final class ContinueExtensionSettingsServiceKt {
   private static final boolean shouldRenderOffScreen() {
      int minBuildNumber = 233;
      ApplicationInfo applicationInfo = ApplicationInfo.getInstance();
      int currentBuildNumber = applicationInfo.getBuild().getBaselineVersion();
      return currentBuildNumber >= minBuildNumber;
   }

   // $FF: synthetic method
   public static final boolean access$shouldRenderOffScreen() {
      return shouldRenderOffScreen();
   }
}
