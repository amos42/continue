package com.github.continuedev.continueeclipseextension.factories;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefResponse;
import org.jetbrains.annotations.NotNull;

public final class ClosedConnection extends ResourceHandlerState {
   @NotNull
   public static final ClosedConnection INSTANCE = new ClosedConnection();

   private ClosedConnection() {
      super((DefaultConstructorMarker)null);
   }

   public void getResponseHeaders(@NotNull CefResponse cefResponse, @NotNull IntRef responseLength, @NotNull StringRef redirectUrl) {
      Intrinsics.checkNotNullParameter(cefResponse, "cefResponse");
      Intrinsics.checkNotNullParameter(responseLength, "responseLength");
      Intrinsics.checkNotNullParameter(redirectUrl, "redirectUrl");
      cefResponse.setStatus(404);
   }
}
