package com.github.continuedev.continueeclipseextension.services;

import org.jetbrains.annotations.Nullable;

public final class ContinueRemoteConfigSyncResponse {
	@Nullable
	private String configJson;
	@Nullable
	private String configJs;
	
	@Nullable
	public final String getConfigJson() {
	 return this.configJson;
	}
	
	public final void setConfigJson(@Nullable String var1) {
	 this.configJson = var1;
	}
	
	@Nullable
	public final String getConfigJs() {
	 return this.configJs;
	}
	
	public final void setConfigJs(@Nullable String var1) {
	 this.configJs = var1;
	}
}