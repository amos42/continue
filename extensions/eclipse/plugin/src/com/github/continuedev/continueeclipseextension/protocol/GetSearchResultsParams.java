package com.github.continuedev.continueeclipseextension.protocol;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class GetSearchResultsParams {
	@NotNull
	private final String query;
	
	public GetSearchResultsParams(@NotNull String query) {
	   super();
	   Intrinsics.checkNotNullParameter(query, "query");
	   this.query = query;
	}
	
	@NotNull
	public final String getQuery() {
	   return this.query;
	}
	
	@NotNull
	public final String component1() {
	   return this.query;
	}
	
	@NotNull
	public final GetSearchResultsParams copy(@NotNull String query) {
	   Intrinsics.checkNotNullParameter(query, "query");
	   return new GetSearchResultsParams(query);
	}
	
	// $FF: synthetic method
	public static GetSearchResultsParams copy$default(GetSearchResultsParams var0, String var1, int var2, Object var3) {
	   if ((var2 & 1) != 0) {
	      var1 = var0.query;
	   }
	
	   return var0.copy(var1);
	}
	
	@NotNull
	public String toString() {
	   return "GetSearchResultsParams(query=" + this.query + ')';
	}
	
	public int hashCode() {
	   return this.query.hashCode();
	}
	
	public boolean equals(@Nullable Object other) {
	   if (this == other) {
	      return true;
	   } else if (!(other instanceof GetSearchResultsParams)) {
	      return false;
	   } else {
	      GetSearchResultsParams var2 = (GetSearchResultsParams)other;
	      return Intrinsics.areEqual(this.query, var2.query);
	   }
	}
}