package com.github.continuedev.continueeclipseextension.factories;

import kotlin.jvm.internal.Intrinsics;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefSchemeHandlerFactory;
import org.cef.handler.CefResourceHandler;
import org.cef.network.CefRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CustomSchemeHandlerFactory implements CefSchemeHandlerFactory {
   @NotNull
   public CefResourceHandler create(@Nullable CefBrowser browser, @Nullable CefFrame frame, @NotNull String schemeName, @NotNull CefRequest request) {
      Intrinsics.checkNotNullParameter(schemeName, "schemeName");
      Intrinsics.checkNotNullParameter(request, "request");
      return new CustomResourceHandler();
   }
}
