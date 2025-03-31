package com.github.continuedev.continueeclipseextension.factories;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.cef.callback.CefCallback;
import org.cef.handler.CefLoadHandler.ErrorCode;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class OpenedConnection extends ResourceHandlerState {
	@Nullable
	private final URLConnection connection;
	@NotNull
	private final Lazy inputStream$delegate;
	
	public OpenedConnection(@Nullable URLConnection connection) {
	   super((DefaultConstructorMarker)null);
	   this.connection = connection;
	   this.inputStream$delegate = LazyKt.lazy(new Function0() {
	      public final InputStream invoke() {
	         URLConnection var1 = OpenedConnection.this.connection;
	         return var1 != null ? var1.getInputStream() : null;
	      }
	
	      // $FF: synthetic method
	      // $FF: bridge method
	      public Object invoke() {
	         return this.invoke();
	      }
	   });
	}
	
	private final InputStream getInputStream() {
	   Lazy var1 = this.inputStream$delegate;
	   Object var2 = null;
	   return (InputStream)var1.getValue();
	}
	
	public void getResponseHeaders(@NotNull CefResponse cefResponse, @NotNull IntRef responseLength, @NotNull StringRef redirectUrl) {
	   Intrinsics.checkNotNullParameter(cefResponse, "cefResponse");
	   Intrinsics.checkNotNullParameter(responseLength, "responseLength");
	   Intrinsics.checkNotNullParameter(redirectUrl, "redirectUrl");
	
	   try {
	      if (this.connection != null) {
	         String url = this.connection.getURL().toString();
	         Intrinsics.checkNotNullExpressionValue(url, "toString(...)");
	         url = StringsKt.substringAfterLast(url, "jar!/", url);
	         if (StringsKt.contains$default((CharSequence)url, (CharSequence)"css", false, 2, (Object)null)) {
	            cefResponse.setMimeType("text/css");
	         } else if (StringsKt.contains$default((CharSequence)url, (CharSequence)"js", false, 2, (Object)null)) {
	            cefResponse.setMimeType("text/javascript");
	         } else if (StringsKt.contains$default((CharSequence)url, (CharSequence)"html", false, 2, (Object)null)) {
	            cefResponse.setMimeType("text/html");
	         } else {
	            cefResponse.setMimeType(this.connection.getContentType());
	         }
	
	         InputStream var6 = this.getInputStream();
	         int var10001;
	         if (var6 != null) {
	            int var7 = var6.available();
	            var10001 = var7;
	         } else {
	            var10001 = 0;
	         }
	
	         responseLength.set(var10001);
	         cefResponse.setStatus(200);
	      } else {
	         cefResponse.setError(ErrorCode.ERR_FAILED);
	         cefResponse.setStatusText("Connection is null");
	         cefResponse.setStatus(500);
	      }
	   } catch (IOException e) {
	      cefResponse.setError(ErrorCode.ERR_FILE_NOT_FOUND);
	      cefResponse.setStatusText(e.getLocalizedMessage());
	      cefResponse.setStatus(404);
	   }
	
	}
	
	public boolean readResponse(@NotNull byte[] dataOut, int bytesToRead, @NotNull IntRef bytesRead, @NotNull CefCallback callback) {
	   Intrinsics.checkNotNullParameter(dataOut, "dataOut");
	   Intrinsics.checkNotNullParameter(bytesRead, "bytesRead");
	   Intrinsics.checkNotNullParameter(callback, "callback");
	   InputStream inputStream = this.getInputStream();
	   if (inputStream != null) {
	      int var8 = 0;
	      int availableSize = inputStream.available();
	      boolean var10000;
	      if (availableSize > 0) {
	         int maxBytesToRead = Math.min(availableSize, bytesToRead);
	         int realBytesRead = inputStream.read(dataOut, 0, maxBytesToRead);
	         bytesRead.set(realBytesRead);
	         var10000 = true;
	      } else {
	         inputStream.close();
	         var10000 = false;
	      }
	
	      return var10000;
	   } else {
	      return false;
	   }
	}
	
	public void close() {
	   InputStream var1 = this.getInputStream();
	   if (var1 != null) {
	      var1.close();
	   }
	
	}
}