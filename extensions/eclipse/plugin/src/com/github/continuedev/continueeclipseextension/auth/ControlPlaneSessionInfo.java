package com.github.continuedev.continueeclipseextension.auth;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ControlPlaneSessionInfo {
	@NotNull
	private final String accessToken;
	@NotNull
	private final Account account;
	
	public ControlPlaneSessionInfo(@NotNull String accessToken, @NotNull Account account) {
	   super();
	   Intrinsics.checkNotNullParameter(accessToken, "accessToken");
	   Intrinsics.checkNotNullParameter(account, "account");
	   this.accessToken = accessToken;
	   this.account = account;
	}
	
	@NotNull
	public final String getAccessToken() {
	   return this.accessToken;
	}
	
	@NotNull
	public final Account getAccount() {
	   return this.account;
	}
	
	@NotNull
	public final String component1() {
	   return this.accessToken;
	}
	
	@NotNull
	public final Account component2() {
	   return this.account;
	}
	
	@NotNull
	public final ControlPlaneSessionInfo copy(@NotNull String accessToken, @NotNull Account account) {
	   Intrinsics.checkNotNullParameter(accessToken, "accessToken");
	   Intrinsics.checkNotNullParameter(account, "account");
	   return new ControlPlaneSessionInfo(accessToken, account);
	}
	
	// $FF: synthetic method
	public static ControlPlaneSessionInfo copy$default(ControlPlaneSessionInfo var0, String var1, Account var2, int var3, Object var4) {
	   if ((var3 & 1) != 0) {
	      var1 = var0.accessToken;
	   }
	
	   if ((var3 & 2) != 0) {
	      var2 = var0.account;
	   }
	
	   return var0.copy(var1, var2);
	}
	
	@NotNull
	public String toString() {
	   return "ControlPlaneSessionInfo(accessToken=" + this.accessToken + ", account=" + this.account + ')';
	}
	
	public int hashCode() {
	   int result = this.accessToken.hashCode();
	   result = result * 31 + this.account.hashCode();
	   return result;
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof ControlPlaneSessionInfo)) {
	      return false;
	   } else {
	      ControlPlaneSessionInfo var2 = (ControlPlaneSessionInfo)other;
	      if (!Intrinsics.areEqual(this.accessToken, var2.accessToken)) {
	         return false;
	      } else {
	         return Intrinsics.areEqual(this.account, var2.account);
	      }
	   }
	}
	
	public static final class Account {
	   @NotNull
	   private final String id;
	   @NotNull
	   private final String label;
	
	   public Account(@NotNull String id, @NotNull String label) {
	      Intrinsics.checkNotNullParameter(id, "id");
	      Intrinsics.checkNotNullParameter(label, "label");
	      super();
	      this.id = id;
	      this.label = label;
	   }
	
	   @NotNull
	   public final String getId() {
	      return this.id;
	   }
	
	   @NotNull
	   public final String getLabel() {
	      return this.label;
	   }
	
	   @NotNull
	   public final String component1() {
	      return this.id;
	   }
	
	   @NotNull
	   public final String component2() {
	      return this.label;
	   }
	
	   @NotNull
	   public final Account copy(@NotNull String id, @NotNull String label) {
	      Intrinsics.checkNotNullParameter(id, "id");
	      Intrinsics.checkNotNullParameter(label, "label");
	      return new Account(id, label);
	   }
	
	   // $FF: synthetic method
	   public static Account copy$default(Account var0, String var1, String var2, int var3, Object var4) {
	      if ((var3 & 1) != 0) {
	         var1 = var0.id;
	      }
	
	      if ((var3 & 2) != 0) {
	         var2 = var0.label;
	      }
	
	      return var0.copy(var1, var2);
	   }
	
	   @NotNull
	   public String toString() {
	      return "Account(id=" + this.id + ", label=" + this.label + ')';
	   }
	
	   public int hashCode() {
	      int result = this.id.hashCode();
	      result = result * 31 + this.label.hashCode();
	      return result;
	   }
	
	   public boolean equals(@Nullable Object other) {
	      if (this == other) {
	         return true;
	      } else if (!(other instanceof Account)) {
	         return false;
	      } else {
	         Account var2 = (Account)other;
	         if (!Intrinsics.areEqual(this.id, var2.id)) {
	            return false;
	         } else {
	            return Intrinsics.areEqual(this.label, var2.label);
	         }
	      }
	   }
	}
}
