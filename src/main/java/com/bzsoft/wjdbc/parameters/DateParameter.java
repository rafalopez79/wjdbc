package com.bzsoft.wjdbc.parameters;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

public class DateParameter implements PreparedStatementParameter {

	private static final long	serialVersionUID	= 5153278906714835319L;

	private Date					value;
	private Calendar				calendar;

	public DateParameter() {
		// empty
	}

	public DateParameter(final Date val, final Calendar cal) {
		value = val;
		calendar = cal;
	}

	public Date getValue() {
		return value;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		value = (Date) in.readObject();
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
			pstmt.setDate(index, value);
		} else {
			pstmt.setDate(index, value, calendar);
		}
	}

	@Override
	public String toString() {
		return "Date: " + value;
	}
}
