package com.bzsoft.wjdbc;

import com.bzsoft.wjdbc.command.DecoratedCommandSink;

public abstract class WBase {

	protected final long							objectUid;
	protected final DecoratedCommandSink	sink;

	protected WBase(final long objectuid, final DecoratedCommandSink dcs) {
		objectUid = objectuid;
		sink = dcs;
	}

	public long getObjectUID() {
		return objectUid;
	}
}
