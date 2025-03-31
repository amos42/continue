package com.github.continuedev.continueeclipseextension;

import com.intellij.DynamicBundle;
import java.util.Arrays;
import java.util.function.Supplier;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public final class MyBundle extends DynamicBundle {
	@NotNull
	public static final MyBundle INSTANCE = new MyBundle();
	
	private MyBundle() {
	   super("messages.MyBundle");
	}
	
	@JvmStatic
	@NotNull
	public static final String message(@PropertyKey(resourceBundle = "messages.MyBundle") @NotNull String key, @NotNull Object... params) {
	   Intrinsics.checkNotNullParameter(key, "key");
	   Intrinsics.checkNotNullParameter(params, "params");
	   String var2 = INSTANCE.getMessage(key, Arrays.copyOf(params, params.length));
	   Intrinsics.checkNotNullExpressionValue(var2, "getMessage(...)");
	   return var2;
	}
	
	@JvmStatic
	@NotNull
	public static final Supplier messagePointer(@PropertyKey(resourceBundle = "messages.MyBundle") @NotNull String key, @NotNull Object... params) {
	   Intrinsics.checkNotNullParameter(key, "key");
	   Intrinsics.checkNotNullParameter(params, "params");
	   Supplier var2 = INSTANCE.getLazyMessage(key, Arrays.copyOf(params, params.length));
	   Intrinsics.checkNotNullExpressionValue(var2, "getLazyMessage(...)");
	   return var2;
	}
}
