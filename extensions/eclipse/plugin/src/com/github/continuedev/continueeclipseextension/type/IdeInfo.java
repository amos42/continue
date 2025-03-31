package com.github.continuedev.continueeclipseextension.type;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class IdeInfo {
	@NotNull
	private final IdeType ideType;
	@NotNull
	private final String name;
	@NotNull
	private final String version;
	@NotNull
	private final String remoteName;
	@NotNull
	private final String extensionVersion;
	
	public IdeInfo(@NotNull IdeType ideType, @NotNull String name, @NotNull String version, @NotNull String remoteName, @NotNull String extensionVersion) {
	   Intrinsics.checkNotNullParameter(ideType, "ideType");
	   Intrinsics.checkNotNullParameter(name, "name");
	   Intrinsics.checkNotNullParameter(version, "version");
	   Intrinsics.checkNotNullParameter(remoteName, "remoteName");
	   Intrinsics.checkNotNullParameter(extensionVersion, "extensionVersion");
	   super();
	   this.ideType = ideType;
	   this.name = name;
	   this.version = version;
	   this.remoteName = remoteName;
	   this.extensionVersion = extensionVersion;
	}
	
	@NotNull
	public final IdeType getIdeType() {
	   return this.ideType;
	}
	
	@NotNull
	public final String getName() {
	   return this.name;
	}
	
	@NotNull
	public final String getVersion() {
	   return this.version;
	}
	
	@NotNull
	public final String getRemoteName() {
	   return this.remoteName;
	}
	
	@NotNull
	public final String getExtensionVersion() {
	   return this.extensionVersion;
	}
	
	@NotNull
	public final IdeType component1() {
	   return this.ideType;
	}
	
	@NotNull
	public final String component2() {
	   return this.name;
	}
	
	@NotNull
	public final String component3() {
	   return this.version;
	}
	
	@NotNull
	public final String component4() {
	   return this.remoteName;
	}
	
	@NotNull
	public final String component5() {
	   return this.extensionVersion;
	}
	
	@NotNull
	public final IdeInfo copy(@NotNull IdeType ideType, @NotNull String name, @NotNull String version, @NotNull String remoteName, @NotNull String extensionVersion) {
	   Intrinsics.checkNotNullParameter(ideType, "ideType");
	   Intrinsics.checkNotNullParameter(name, "name");
	   Intrinsics.checkNotNullParameter(version, "version");
	   Intrinsics.checkNotNullParameter(remoteName, "remoteName");
	   Intrinsics.checkNotNullParameter(extensionVersion, "extensionVersion");
	   return new IdeInfo(ideType, name, version, remoteName, extensionVersion);
	}
	
	// $FF: synthetic method
	public static IdeInfo copy$default(IdeInfo var0, IdeType var1, String var2, String var3, String var4, String var5, int var6, Object var7) {
	   if ((var6 & 1) != 0) {
	      var1 = var0.ideType;
	   }
	
	   if ((var6 & 2) != 0) {
	      var2 = var0.name;
	   }
	
	   if ((var6 & 4) != 0) {
	      var3 = var0.version;
	   }
	
	   if ((var6 & 8) != 0) {
	      var4 = var0.remoteName;
	   }
	
	   if ((var6 & 16) != 0) {
	      var5 = var0.extensionVersion;
	   }
	
	   return var0.copy(var1, var2, var3, var4, var5);
	}
	
	@NotNull
	public String toString() {
	   return "IdeInfo(ideType=" + this.ideType + ", name=" + this.name + ", version=" + this.version + ", remoteName=" + this.remoteName + ", extensionVersion=" + this.extensionVersion + ')';
	}
	
	public int hashCode() {
	   int result = this.ideType.hashCode();
	   result = result * 31 + this.name.hashCode();
	   result = result * 31 + this.version.hashCode();
	   result = result * 31 + this.remoteName.hashCode();
	   result = result * 31 + this.extensionVersion.hashCode();
	   return result;
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof IdeInfo)) {
	      return false;
	   } else {
	      IdeInfo var2 = (IdeInfo)other;
	      if (this.ideType != var2.ideType) {
	         return false;
	      } else if (!Intrinsics.areEqual(this.name, var2.name)) {
	         return false;
	      } else if (!Intrinsics.areEqual(this.version, var2.version)) {
	         return false;
	      } else if (!Intrinsics.areEqual(this.remoteName, var2.remoteName)) {
	         return false;
	      } else {
	         return Intrinsics.areEqual(this.extensionVersion, var2.extensionVersion);
	      }
	   }
	}
}