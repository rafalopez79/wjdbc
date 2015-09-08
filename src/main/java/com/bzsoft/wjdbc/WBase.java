package com.bzsoft.wjdbc;

import com.bzsoft.wjdbc.command.DecoratedCommandSink;

/**
 * The Class WBase.
 */
public abstract class WBase {

	protected final long							objectUid;
	protected final DecoratedCommandSink	sink;

	/**
	 * Instantiates a new w base.
	 *
	 * @param objectuid
	 *           the objectuid
	 * @param dcs
	 *           the dcs
	 */
	protected WBase(final long objectuid, final DecoratedCommandSink dcs) {
		objectUid = objectuid;
		sink = dcs;
	}

	/**
	 * Gets the object uid.
	 *
	 * @return the object uid
	 */
	public long getObjectUID() {
		return objectUid;
	}
}
