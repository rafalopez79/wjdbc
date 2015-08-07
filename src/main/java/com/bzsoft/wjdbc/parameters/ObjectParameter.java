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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (scale == null ? 0 : scale.hashCode());
		result = prime * result + (targetSqlType == null ? 0 : targetSqlType.hashCode());
		result = prime * result + (value == null ? 0 : value.hashCode());
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
		final ObjectParameter other = (ObjectParameter) obj;
		if (scale == null) {
			if (other.scale != null) {
				return false;
			}
		} else if (!scale.equals(other.scale)) {
			return false;
		}
		if (targetSqlType == null) {
			if (other.targetSqlType != null) {
				return false;
			}
		} else if (!targetSqlType.equals(other.targetSqlType)) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}


}
