package com.bzsoft.wjdbc.command;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ConnectResult implements Externalizable {

	private long		uid;
	private boolean	trace;

	public ConnectResult() {
		// empty
	}

	public ConnectResult(final long uid, final boolean trace) {
		this.uid = uid;
		this.trace = trace;
	}

	public long getUid() {
		return uid;
	}

	public boolean isTrace() {
		return trace;
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeLong(uid);
		out.writeBoolean(trace);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		uid = in.readLong();
		trace = in.readBoolean();
	}
}