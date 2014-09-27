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
}
