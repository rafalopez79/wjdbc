// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.parameters;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ObjectParameter implements PreparedStatementParameter {

	private static final long	serialVersionUID	= -9065375715201787003L;

	private Object					value;
	private Integer				targetSqlType;
	private Integer				scale;

	public ObjectParameter() {
		// empty
	}

	public ObjectParameter(final Object value, final Integer targetSqlType, final Integer scale) {
		this.value = value;
		this.targetSqlType = targetSqlType;
		this.scale = scale;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		value = in.readObject();
		targetSqlType = (Integer) in.readObject();
		scale = (Integer) in.readObject();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeObject(value);
		out.writeObject(targetSqlType);
		out.writeObject(scale);
	}

	@Override
	public void setParameter(final PreparedStatement pstmt, final int index) throws SQLException {
		if (scale == null) {
			if (targetSqlType == null) {
				pstmt.setObject(index, value);
			} else {
				pstmt.setObject(index, value, targetSqlType.intValue());
			}
		} else {
			pstmt.setObject(index, value, targetSqlType.intValue(), scale.intValue());
		}
	}

	@Override
	public String toString() {
		return "Object: " + value;
	}
}
