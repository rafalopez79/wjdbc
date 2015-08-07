package com.bzsoft.wjdbc.parameters;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;

public class TimeParameter implements PreparedStatementParameter {

	private static final long	serialVersionUID	= -3833958578075965947L;

	private Time					value;
	private Calendar				calendar;

	public TimeParameter() {
		// empty
	}

	public TimeParameter(final Time val, final Calendar cal) {
		value = val;
		calendar = cal;
	}

	public Time getValue() {
		return value;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		value = (Time) in.readObject();
		calendar = (Calendar) in.readObject();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeObject(value);
		out.writeObject(calendar);
	}

	@Override
	public void setParameter(final PreparedStatement pstmt, final int index) throws SQLException {
		if (calendar == null) {
			pstmt.setTime(index, value);
		} else {
			pstmt.setTime(index, value, calendar);
		}
	}

	@Override
	public String toString() {
		return "Time: " + value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (calendar == null ? 0 : calendar.hashCode());
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
		final TimeParameter other = (TimeParameter) obj;
		if (calendar == null) {
			if (other.calendar != null) {
				return false;
			}
		} else if (!calendar.equals(other.calendar)) {
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
