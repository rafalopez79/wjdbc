// VJDBC - Virtual JDBC
// Written by Hunter Payne
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.serial;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Reader;
import java.sql.NClob;
import java.sql.SQLException;

public class SerialNClob extends SerialClob implements NClob {

	private static final long	serialVersionUID	= -869122661664868443L;

	public SerialNClob() {
	}

	public SerialNClob(final NClob other) throws SQLException {
		super(other);
	}

	public SerialNClob(final Reader rd) throws SQLException {
		super(rd);
	}

	public SerialNClob(final Reader rd, final long length) throws SQLException {
		super(rd, length);
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeObject(data);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		data = (char[]) in.readObject();
	}
}
