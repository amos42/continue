package com.github.continuedev.continueeclipseextension.factories;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.cef.callback.CefCallback;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefResponse;
import org.jetbrains.annotations.NotNull;

public abstract class ResourceHandlerState {
	private ResourceHandlerState() {
	}
	
	public void getResponseHeaders(@NotNull CefResponse cefResponse, @NotNull IntRef responseLength, @NotNull StringRef redirectUrl) {
	   Intrinsics.checkNotNullParameter(cefResponse, "cefResponse");
	   Intrinsics.checkNotNullParameter(responseLength, "responseLength");
	   Intrinsics.checkNotNullParameter(redirectUrl, "redirectUrl");
	}
	
	public boolean readResponse(@NotNull byte[] dataOut, int bytesToRead, @NotNull IntRef bytesRead, @NotNull CefCallback callback) {
	   Intrinsics.checkNotNullParameter(dataOut, "dataOut");
	   Intrinsics.checkNotNullParameter(bytesRead, "bytesRead");
	   Intrinsics.checkNotNullParameter(callback, "callback");
	   return false;
	}
	
	public void close() {
	}
	
	// $FF: synthetic method
	public ResourceHandlerState(DefaultConstructorMarker $constructor_marker) {
	   this();
	}
}
