package com.bzsoft.wjdbc.serial;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;

public interface FlattenedColumnValuesParser {

	public void setObject(FlattenedColumnValues fcv, ResultSet rs, int i, int pos) throws SQLException;

	public Class<?> getSQLClass();

	public static final class NullFlattenedColumnValuesParser implements FlattenedColumnValuesParser {

		@Override
		public void setObject(final FlattenedColumnValues fcv, final ResultSet rs, final int i, final int pos) throws SQLException {
			fcv.setObject(pos, null);
			fcv.setIsNull(pos);
		}

		@Override
		public Class<?> getSQLClass() {
			return null;
		}
	}

	public static final class StringFlattenedColumnValuesParser implements FlattenedColumnValuesParser {
		@Override
		public void setObject(final FlattenedColumnValues fcv, final ResultSet rs, final int i, final int pos) throws SQLException {
			fcv.setObject(pos, rs.getString(i));
			if (rs.wasNull()) {
				fcv.setIsNull(pos);
			}
		}

		@Override
		public Class<?> getSQLClass() {
			return Object.class;
		}
	}

	public static final class NStringFlattenedColumnValuesParser implements FlattenedColumnValuesParser {
		@Override
		public void setObject(final FlattenedColumnValues fcv, final ResultSet rs, final int i, final int pos) throws SQLException {
			fcv.setObject(pos, rs.getNString(i));
			if (rs.wasNull()) {
				fcv.setIsNull(pos);
			}
		}

		@Override
		public Class<?> getSQLClass() {
			return Object.class;
		}
	}

	public static final class BigDecimalFlattenedColumnValuesParser implements FlattenedColumnValuesParser {
		@Override
		public void setObject(final FlattenedColumnValues fcv, final ResultSet rs, final int i, final int pos) throws SQLException {
			fcv.setObject(pos, rs.getBigDecimal(i));
			if (rs.wasNull()) {
				fcv.setIsNull(pos);
			}
		}

		@Override
		public Class<?> getSQLClass() {
			return Object.class;
		}
	}

	public static final class BooleanFlattenedColumnValuesParser implements FlattenedColumnValuesParser {
		@Override
		public void setObject(final FlattenedColumnValues fcv, final ResultSet rs, final int i, final int pos) throws SQLException {
			fcv.setBoolean(pos, rs.getBoolean(i));
			if (rs.wasNull()) {
				fcv.setIsNull(pos);
			}
		}

		@Override
		public Class<?> getSQLClass() {
			return boolean.class;
		}
	}

	public static final class ByteFlattenedColumnValuesParser implements FlattenedColumnValuesParser {
		@Override
		public void setObject(final FlattenedColumnValues fcv, final ResultSet rs, final int i, final int pos) throws SQLException {
			fcv.setByte(pos, rs.getByte(i));
			if (rs.wasNull()) {
				fcv.setIsNull(pos);
			}
		}

		@Override
		public Class<?> getSQLClass() {
			return byte.class;
		}
	}

	public static final class ShortFlattenedColumnValuesParser implements FlattenedColumnValuesParser {
		@Override
		public void setObject(final FlattenedColumnValues fcv, final ResultSet rs, final int i, final int pos) throws SQLException {
			fcv.setShort(pos, rs.getShort(i));
			if (rs.wasNull()) {
				fcv.setIsNull(pos);
			}
		}

		@Override
		public Class<?> getSQLClass() {
			return short.class;
		}
	}

	public static final class IntFlattenedColumnValuesParser implements FlattenedColumnValuesParser {
		@Override
		public void setObject(final FlattenedColumnValues fcv, final ResultSet rs, final int i, final int pos) throws SQLException {
			fcv.setInt(pos, rs.getInt(i));
			if (rs.wasNull()) {
				fcv.setIsNull(pos);
			}
		}

		@Override
		public Class<?> getSQLClass() {
			return int.class;
		}
	}

	public static final class LongFlattenedColumnValuesParser implements FlattenedColumnValuesParser {
		@Override
		public void setObject(final FlattenedColumnValues fcv, final ResultSet rs, final int i, final int pos) throws SQLException {
			fcv.setLong(pos, rs.getLong(i));
			if (rs.wasNull()) {
				fcv.setIsNull(pos);
			}
		}

		@Override
		public Class<?> getSQLClass() {
			return long.class;
		}
	}

	public static final class FloatFlattenedColumnValuesParser implements FlattenedColumnValuesParser {
		@Override
		public void setObject(final FlattenedColumnValues fcv, final ResultSet rs, final int i, final int pos) throws SQLException {
			fcv.setFloat(pos, rs.getFloat(i));
			if (rs.wasNull()) {
				fcv.setIsNull(pos);
			}
		}

		@Override
		public Class<?> getSQLClass() {
			return float.class;
		}
	}

	public static final class DoubleFlattenedColumnValuesParser implements FlattenedColumnValuesParser {
		@Override
		public void setObject(final FlattenedColumnValues fcv, final ResultSet rs, final int i, final int pos) throws SQLException {
			fcv.setDouble(pos, rs.getDouble(i));
			if (rs.wasNull()) {
				fcv.setIsNull(pos);
			}
		}

		@Override
		public Class<?> getSQLClass() {
			return double.class;
		}
	}

	public static final class DateFlattenedColumnValuesParser implements FlattenedColumnValuesParser {
		@Override
		public void setObject(final FlattenedColumnValues fcv, final ResultSet rs, final int i, final int pos) throws SQLException {
			fcv.setObject(pos, rs.getDate(i));
			if (rs.wasNull()) {
				fcv.setIsNull(pos);
			}
		}

		@Override
		public Class<?> getSQLClass() {
			return Object.class;
		}
	}

	public static final class TimeFlattenedColumnValuesParser implements FlattenedColumnValuesParser {
		@Override
		public void setObject(final FlattenedColumnValues fcv, final ResultSet rs, final int i, final int pos) throws SQLException {
			fcv.setObject(pos, rs.getTime(i));
			if (rs.wasNull()) {
				fcv.setIsNull(pos);
			}
		}

		@Override
		public Class<?> getSQLClass() {
			return Object.class;
		}
	}

	public static final class TimestampFlattenedColumnValuesParser implements FlattenedColumnValuesParser {
		@Override
		public void setObject(final FlattenedColumnValues fcv, final ResultSet rs, final int i, final int pos) throws SQLException {
			fcv.setObject(pos, rs.getTimestamp(i));
			if (rs.wasNull()) {
				fcv.setIsNull(pos);
			}
		}

		@Override
		public Class<?> getSQLClass() {
			return Object.class;
		}
	}

	public static final class BytesFlattenedColumnValuesParser implements FlattenedColumnValuesParser {
		@Override
		public void setObject(final FlattenedColumnValues fcv, final ResultSet rs, final int i, final int pos) throws SQLException {
			fcv.setObject(pos, rs.getBytes(i));
			if (rs.wasNull()) {
				fcv.setIsNull(pos);
			}
		}

		@Override
		public Class<?> getSQLClass() {
			return Object.class;
		}
	}

	public static final class JavaObjectFlattenedColumnValuesParser implements FlattenedColumnValuesParser {
		@Override
		public void setObject(final FlattenedColumnValues fcv, final ResultSet rs, final int i, final int pos) throws SQLException {
			fcv.setObject(pos, new SerialJavaObject(rs.getObject(i)));
			if (rs.wasNull()) {
				fcv.setIsNull(pos);
			}
		}

		@Override
		public Class<?> getSQLClass() {
			return Object.class;
		}
	}

	public static final class ClobFlattenedColumnValuesParser implements FlattenedColumnValuesParser {
		@Override
		public void setObject(final FlattenedColumnValues fcv, final ResultSet rs, final int i, final int pos) throws SQLException {
			fcv.setObject(pos, new SerialClob(rs.getClob(i)));
			if (rs.wasNull()) {
				fcv.setIsNull(pos);
			}
		}

		@Override
		public Class<?> getSQLClass() {
			return Object.class;
		}
	}

	public static final class NClobFlattenedColumnValuesParser implements FlattenedColumnValuesParser {
		@Override
		public void setObject(final FlattenedColumnValues fcv, final ResultSet rs, final int i, final int pos) throws SQLException {
			fcv.setObject(pos, new SerialNClob(rs.getNClob(i)));
			if (rs.wasNull()) {
				fcv.setIsNull(pos);
			}
		}

		@Override
		public Class<?> getSQLClass() {
			return Object.class;
		}
	}

	public static final class BlobFlattenedColumnValuesParser implements FlattenedColumnValuesParser {
		@Override
		public void setObject(final FlattenedColumnValues fcv, final ResultSet rs, final int i, final int pos) throws SQLException {
			fcv.setObject(pos, new SerialBlob(rs.getBlob(i)));
			if (rs.wasNull()) {
				fcv.setIsNull(pos);
			}
		}

		@Override
		public Class<?> getSQLClass() {
			return Object.class;
		}
	}

	public static final class ArrayFlattenedColumnValuesParser implements FlattenedColumnValuesParser {
		@Override
		public void setObject(final FlattenedColumnValues fcv, final ResultSet rs, final int i, final int pos) throws SQLException {
			fcv.setObject(pos, new SerialArray(rs.getArray(i)));
			if (rs.wasNull()) {
				fcv.setIsNull(pos);
			}
		}

		@Override
		public Class<?> getSQLClass() {
			return Object.class;
		}
	}

	public static final class StructFlattenedColumnValuesParser implements FlattenedColumnValuesParser {
		@Override
		public void setObject(final FlattenedColumnValues fcv, final ResultSet rs, final int i, final int pos) throws SQLException {
			fcv.setObject(pos, new SerialStruct((Struct) rs.getObject(i)));
			if (rs.wasNull()) {
				fcv.setIsNull(pos);
			}
		}

		@Override
		public Class<?> getSQLClass() {
			return Object.class;
		}
	}

	public static final class RowIdFlattenedColumnValuesParser implements FlattenedColumnValuesParser {
		@Override
		public void setObject(final FlattenedColumnValues fcv, final ResultSet rs, final int i, final int pos) throws SQLException {
			fcv.setObject(pos, new SerialRowId(rs.getRowId(i)));
			if (rs.wasNull()) {
				fcv.setIsNull(pos);
			}
		}

		@Override
		public Class<?> getSQLClass() {
			return Object.class;
		}
	}

	public static final class SQLXMLFlattenedColumnValuesParser implements FlattenedColumnValuesParser {
		@Override
		public void setObject(final FlattenedColumnValues fcv, final ResultSet rs, final int i, final int pos) throws SQLException {
			fcv.setObject(pos, new SerialSQLXML(rs.getSQLXML(i)));
			if (rs.wasNull()) {
				fcv.setIsNull(pos);
			}
		}

		@Override
		public Class<?> getSQLClass() {
			return Object.class;
		}
	}
}
