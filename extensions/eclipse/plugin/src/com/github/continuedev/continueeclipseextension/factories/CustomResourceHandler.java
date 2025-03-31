package com.github.continuedev.continueeclipseextension.factories;

import com.intellij.openapi.project.DumbAware;
import java.net.URL;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.cef.callback.CefCallback;
import org.cef.handler.CefResourceHandler;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefRequest;
import org.cef.network.CefResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CustomResourceHandler implements CefResourceHandler, DumbAware {
	@NotNull
	private ResourceHandlerState state;
	@Nullable
	private String currentUrl;
	
	public CustomResourceHandler() {
	   this.state = ClosedConnection.INSTANCE;
	}
	
	public boolean processRequest(@NotNull CefRequest cefRequest, @NotNull CefCallback cefCallback) {
	   Intrinsics.checkNotNullParameter(cefRequest, "cefRequest");
	   Intrinsics.checkNotNullParameter(cefCallback, "cefCallback");
	   String url = cefRequest.getURL();
	   boolean var10000;
	   if (url != null) {
	      String pathToResource = StringsKt.replace$default(StringsKt.replace$default(url, "http://continue", "webview/", false, 4, (Object)null), "http://localhost:5173", "webview/", false, 4, (Object)null);
	      URL newUrl = this.getClass().getClassLoader().getResource(pathToResource);
	      this.state = new OpenedConnection(newUrl != null ? newUrl.openConnection() : null);
	      this.currentUrl = url;
	      cefCallback.Continue();
	      var10000 = true;
	   } else {
	      var10000 = false;
	   }
	
	   return var10000;
	}
	
	public void getResponseHeaders(@NotNull CefResponse cefResponse, @NotNull IntRef responseLength, @NotNull StringRef redirectUrl) {
	   Intrinsics.checkNotNullParameter(cefResponse, "cefResponse");
	   Intrinsics.checkNotNullParameter(responseLength, "responseLength");
	   Intrinsics.checkNotNullParameter(redirectUrl, "redirectUrl");
	   if (this.currentUrl != null) {
	      String var10000 = this.currentUrl;
	      Intrinsics.checkNotNull(var10000);
	      if (StringsKt.contains$default((CharSequence)var10000, (CharSequence)"css", false, 2, (Object)null)) {
	         cefResponse.setMimeType("text/css");
	      } else {
	         var10000 = this.currentUrl;
	         Intrinsics.checkNotNull(var10000);
	         if (StringsKt.contains$default((CharSequence)var10000, (CharSequence)"js", false, 2, (Object)null)) {
	            cefResponse.setMimeType("text/javascript");
	         } else {
	            var10000 = this.currentUrl;
	            Intrinsics.checkNotNull(var10000);
	            if (StringsKt.contains$default((CharSequence)var10000, (CharSequence)"html", false, 2, (Object)null)) {
	               cefResponse.setMimeType("text/html");
	            }
	         }
	      }
	   }
	
	   this.state.getResponseHeaders(cefResponse, responseLength, redirectUrl);
	}
	
	public boolean readResponse(@NotNull byte[] dataOut, int bytesToRead, @NotNull IntRef bytesRead, @NotNull CefCallback callback) {
	   Intrinsics.checkNotNullParameter(dataOut, "dataOut");
	   Intrinsics.checkNotNullParameter(bytesRead, "bytesRead");
	   Intrinsics.checkNotNullParameter(callback, "callback");
	   return this.state.readResponse(dataOut, bytesToRead, bytesRead, callback);
	}
	
	public void cancel() {
	   this.state.close();
	   this.state = ClosedConnection.INSTANCE;
	}
}
