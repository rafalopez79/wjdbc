package com.bzsoft.wjdbc.parameters;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NullParameter implements PreparedStatementParameter {

	private static final long	serialVersionUID	= 2061806736191837513L;

	private int						sqlType;
	private String					typeName;

	public NullParameter() {
		// empty
	}

	public NullParameter(final int sqltype, final String typename) {
		sqlType = sqltype;
		typeName = typename;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		sqlType = in.readInt();
		typeName = in.readUTF();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeInt(sqlType);
		out.writeUTF(typeName);
	}

	@Override
	public void setParameter(final PreparedStatement pstmt, final int index) throws SQLException {
		if (typeName == null) {
			pstmt.setNull(index, sqlType);
		} else {
			pstmt.setNull(index, sqlType, typeName);
		}
	}

	@Override
	public String toString() {
		return "Null, SQL-Type: " + sqlType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + sqlType;
		result = prime * result + (typeName == null ? 0 : typeName.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final NullParameter other = (NullParameter) obj;
		if (sqlType != other.sqlType) {
			return false;
		}
		if (typeName == null) {
			if (other.typeName != null) {
				return false;
			}
		} else if (!typeName.equals(other.typeName)) {
			return false;
		}
		return true;
	}


}
