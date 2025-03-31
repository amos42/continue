package com.github.continuedev.continueeclipseextension.type;

import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import org.jetbrains.annotations.NotNull;

public enum ToastType {
	@NotNull
	private final String value;
	INFO("info"),
	ERROR("error"),
	WARNING("warning");
	
	// $FF: synthetic field
	private static final EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);
	
	private ToastType(String value) {
	   this.value = value;
	}
	
	@NotNull
	public final String getValue() {
	   return this.value;
	}
	
	@NotNull
	public static EnumEntries getEntries() {
	   return $ENTRIES;
	}
	
	// $FF: synthetic method
	private static final ToastType[] $values() {
	   ToastType[] var0 = new ToastType[]{INFO, ERROR, WARNING};
	   return var0;
	}
}