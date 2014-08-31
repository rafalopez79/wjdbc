// VJDBC - Virtual JDBC
// Written by Hunter Payne
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.parameters;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLXML;

import com.bzsoft.wjdbc.serial.SerialSQLXML;


public class SQLXMLParameter implements PreparedStatementParameter {

	private static final long	serialVersionUID	= 8647675527971168478L;

	private SerialSQLXML			value;

	public SQLXMLParameter() {
		// empty
	}

	public SQLXMLParameter(final SQLXML value) throws SQLException {
		this.value = new SerialSQLXML(value);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		value = (SerialSQLXML) in.readObject();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeObject(value);
	}

	@Override
	public void setParameter(final PreparedStatement pstmt, final int index) throws SQLException {
		pstmt.setSQLXML(index, value);
	}

	@Override
	public String toString() {
		try {
			return "SQLXML: " + value.getString();
		} catch (final SQLException sqle) {
			// empty
		}
		return "SQLXML: fail";
	}
}
