// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc;

import com.bzsoft.wjdbc.command.DecoratedCommandSink;

/**
 * Base class for all virtual JDBC-Classes. All VJDBC-Objects have a unique id
 * and a reference to a command sink which can process commands generated by the
 * VJDBC object.
 */
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
