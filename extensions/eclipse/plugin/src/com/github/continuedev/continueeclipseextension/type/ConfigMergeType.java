package com.github.continuedev.continueeclipseextension.type;

import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import org.jetbrains.annotations.NotNull;

public enum ConfigMergeType {
	MERGE,
	OVERWRITE;
	
	// $FF: synthetic field
	private static final EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);
	
	@NotNull
	public static EnumEntries getEntries() {
	   return $ENTRIES;
	}
	
	// $FF: synthetic method
	private static final ConfigMergeType[] $values() {
	   ConfigMergeType[] var0 = new ConfigMergeType[]{MERGE, OVERWRITE};
	   return var0;
	}
}