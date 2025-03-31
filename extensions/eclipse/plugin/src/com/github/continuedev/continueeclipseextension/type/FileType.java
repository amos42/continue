package com.github.continuedev.continueeclipseextension.type;

import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import org.jetbrains.annotations.NotNull;

public enum FileType {
	private final int value;
	UNKNOWN(0),
	FILE(1),
	DIRECTORY(2),
	SYMBOLIC_LINK(64);
	
	// $FF: synthetic field
	private static final EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);
	
	private FileType(int value) {
	   this.value = value;
	}
	
	public final int getValue() {
	   return this.value;
	}
	
	@NotNull
	public static EnumEntries getEntries() {
	   return $ENTRIES;
	}
	
	// $FF: synthetic method
	private static final FileType[] $values() {
	   FileType[] var0 = new FileType[]{UNKNOWN, FILE, DIRECTORY, SYMBOLIC_LINK};
	   return var0;
	}
}
