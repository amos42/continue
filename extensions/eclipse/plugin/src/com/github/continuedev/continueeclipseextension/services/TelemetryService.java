package com.github.continuedev.continueeclipseextension.services;

import com.intellij.openapi.components.Service;
import com.posthog.java.PostHog;
import java.util.Map;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Service
public final class TelemetryService {
   @NotNull
   private final String POSTHOG_API_KEY = "phc_JS6XFROuNbhJtVCEdTSYk6gl5ArRrTNMpCcguAXlSPs";
   @Nullable
   private PostHog posthog;
   @Nullable
   private String distinctId;

   public final void setup(@NotNull String distinctId) {
      Intrinsics.checkNotNullParameter(distinctId, "distinctId");
      this.posthog = (new PostHog.Builder(this.POSTHOG_API_KEY)).host("https://app.posthog.com").build();
      this.distinctId = distinctId;
   }

   public final void capture(@NotNull String eventName, @NotNull Map properties) {
      Intrinsics.checkNotNullParameter(eventName, "eventName");
      Intrinsics.checkNotNullParameter(properties, "properties");
      if (this.posthog != null && this.distinctId != null) {
         try {
            PostHog var3 = this.posthog;
            if (var3 != null) {
               var3.capture(this.distinctId, eventName, properties);
            }
         } catch (Exception var4) {
         }

      }
   }

   public final void shutdown() {
      PostHog var1 = this.posthog;
      if (var1 != null) {
         var1.shutdown();
      }

   }
}
