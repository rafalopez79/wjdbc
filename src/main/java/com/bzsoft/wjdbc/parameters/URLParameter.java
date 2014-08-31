// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.parameters;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class URLParameter implements PreparedStatementParameter {

	private static final long	serialVersionUID	= 4214386658417445307L;

	private URL						value;

	public URLParameter() {
		// empty
	}

	public URLParameter(final URL value) {
		this.value = value;
	}

	public URL getValue() {
		return value;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		value = (URL) in.readObject();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeObject(value);
	}

	@Override
	public void setParameter(final PreparedStatement pstmt, final int index) throws SQLException {
		pstmt.setURL(index, value);
	}

	@Override
	public String toString() {
		return "URL: " + value;
	}
}
