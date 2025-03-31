package com.github.continuedev.continueeclipseextension.type;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class IdeSettings {
	@Nullable
	private final String remoteConfigServerUrl;
	private final int remoteConfigSyncPeriod;
	@NotNull
	private final String userToken;
	private final boolean enableControlServerBeta;
	private final boolean pauseCodebaseIndexOnStart;
	@NotNull
	private final String continueTestEnvironment;
	
	public IdeSettings(@Nullable String remoteConfigServerUrl, int remoteConfigSyncPeriod, @NotNull String userToken, boolean enableControlServerBeta, boolean pauseCodebaseIndexOnStart, @NotNull String continueTestEnvironment) {
	   Intrinsics.checkNotNullParameter(userToken, "userToken");
	   Intrinsics.checkNotNullParameter(continueTestEnvironment, "continueTestEnvironment");
	   super();
	   this.remoteConfigServerUrl = remoteConfigServerUrl;
	   this.remoteConfigSyncPeriod = remoteConfigSyncPeriod;
	   this.userToken = userToken;
	   this.enableControlServerBeta = enableControlServerBeta;
	   this.pauseCodebaseIndexOnStart = pauseCodebaseIndexOnStart;
	   this.continueTestEnvironment = continueTestEnvironment;
	}
	
	@Nullable
	public final String getRemoteConfigServerUrl() {
	   return this.remoteConfigServerUrl;
	}
	
	public final int getRemoteConfigSyncPeriod() {
	   return this.remoteConfigSyncPeriod;
	}
	
	@NotNull
	public final String getUserToken() {
	   return this.userToken;
	}
	
	public final boolean getEnableControlServerBeta() {
	   return this.enableControlServerBeta;
	}
	
	public final boolean getPauseCodebaseIndexOnStart() {
	   return this.pauseCodebaseIndexOnStart;
	}
	
	@NotNull
	public final String getContinueTestEnvironment() {
	   return this.continueTestEnvironment;
	}
	
	@Nullable
	public final String component1() {
	   return this.remoteConfigServerUrl;
	}
	
	public final int component2() {
	   return this.remoteConfigSyncPeriod;
	}
	
	@NotNull
	public final String component3() {
	   return this.userToken;
	}
	
	public final boolean component4() {
	   return this.enableControlServerBeta;
	}
	
	public final boolean component5() {
	   return this.pauseCodebaseIndexOnStart;
	}
	
	@NotNull
	public final String component6() {
	   return this.continueTestEnvironment;
	}
	
	@NotNull
	public final IdeSettings copy(@Nullable String remoteConfigServerUrl, int remoteConfigSyncPeriod, @NotNull String userToken, boolean enableControlServerBeta, boolean pauseCodebaseIndexOnStart, @NotNull String continueTestEnvironment) {
	   Intrinsics.checkNotNullParameter(userToken, "userToken");
	   Intrinsics.checkNotNullParameter(continueTestEnvironment, "continueTestEnvironment");
	   return new IdeSettings(remoteConfigServerUrl, remoteConfigSyncPeriod, userToken, enableControlServerBeta, pauseCodebaseIndexOnStart, continueTestEnvironment);
	}
	
	// $FF: synthetic method
	public static IdeSettings copy$default(IdeSettings var0, String var1, int var2, String var3, boolean var4, boolean var5, String var6, int var7, Object var8) {
	   if ((var7 & 1) != 0) {
	      var1 = var0.remoteConfigServerUrl;
	   }
	
	   if ((var7 & 2) != 0) {
	      var2 = var0.remoteConfigSyncPeriod;
	   }
	
	   if ((var7 & 4) != 0) {
	      var3 = var0.userToken;
	   }
	
	   if ((var7 & 8) != 0) {
	      var4 = var0.enableControlServerBeta;
	   }
	
	   if ((var7 & 16) != 0) {
	      var5 = var0.pauseCodebaseIndexOnStart;
	   }
	
	   if ((var7 & 32) != 0) {
	      var6 = var0.continueTestEnvironment;
	   }
	
	   return var0.copy(var1, var2, var3, var4, var5, var6);
	}
	
	@NotNull
	public String toString() {
	   return "IdeSettings(remoteConfigServerUrl=" + this.remoteConfigServerUrl + ", remoteConfigSyncPeriod=" + this.remoteConfigSyncPeriod + ", userToken=" + this.userToken + ", enableControlServerBeta=" + this.enableControlServerBeta + ", pauseCodebaseIndexOnStart=" + this.pauseCodebaseIndexOnStart + ", continueTestEnvironment=" + this.continueTestEnvironment + ')';
	}
	
	public int hashCode() {
	   int result = this.remoteConfigServerUrl == null ? 0 : this.remoteConfigServerUrl.hashCode();
	   result = result * 31 + Integer.hashCode(this.remoteConfigSyncPeriod);
	   result = result * 31 + this.userToken.hashCode();
	   result = result * 31 + Boolean.hashCode(this.enableControlServerBeta);
	   result = result * 31 + Boolean.hashCode(this.pauseCodebaseIndexOnStart);
	   result = result * 31 + this.continueTestEnvironment.hashCode();
	   return result;
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof IdeSettings)) {
	      return false;
	   } else {
	      IdeSettings var2 = (IdeSettings)other;
	      if (!Intrinsics.areEqual(this.remoteConfigServerUrl, var2.remoteConfigServerUrl)) {
	         return false;
	      } else if (this.remoteConfigSyncPeriod != var2.remoteConfigSyncPeriod) {
	         return false;
	      } else if (!Intrinsics.areEqual(this.userToken, var2.userToken)) {
	         return false;
	      } else if (this.enableControlServerBeta != var2.enableControlServerBeta) {
	         return false;
	      } else if (this.pauseCodebaseIndexOnStart != var2.pauseCodebaseIndexOnStart) {
	         return false;
	      } else {
	         return Intrinsics.areEqual(this.continueTestEnvironment, var2.continueTestEnvironment);
	      }
	   }
	}
}