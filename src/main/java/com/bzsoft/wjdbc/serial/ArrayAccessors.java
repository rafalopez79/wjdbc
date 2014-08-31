//VJDBC - Virtual JDBC
//Written by Michael Link
//Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.serial;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

final class ArrayAccessors {

	private static final Map<Class<?>, ArrayAccess>	arrayAccessors;
	private static final ArrayAccess						objectArrayFiller;

	static {
		final Map<Class<?>, ArrayAccess> map = new HashMap<Class<?>, ArrayAccess>();
		map.put(boolean.class, new ArrayAccessBoolean());
		map.put(byte.class, new ArrayAccessByte());
		map.put(char.class, new ArrayAccessCharacter());
		map.put(short.class, new ArrayAccessShort());
		map.put(int.class, new ArrayAccessInteger());
		map.put(long.class, new ArrayAccessLong());
		map.put(float.class, new ArrayAccessFloat());
		map.put(double.class, new ArrayAccessDouble());
		arrayAccessors = Collections.unmodifiableMap(map);
		objectArrayFiller = new ArrayAccessObject();
	}

	static ArrayAccess getArrayAccessorForPrimitiveType(final Class<?> primitiveType) {
		return arrayAccessors.get(primitiveType);
	}

	static ArrayAccess getObjectArrayAccessor() {
		return objectArrayFiller;
	}

	private static final class ArrayAccessBoolean implements ArrayAccess {
		@Override
		public Object getValue(final Object array, final int index, final boolean[] nullFlags) {
			if (nullFlags[index]) {
				return null;
			}
			return Boolean.valueOf(Array.getBoolean(array, index));
		}
	}

	private static final class ArrayAccessByte implements ArrayAccess {
		@Override
		public Object getValue(final Object array, final int index, final boolean[] nullFlags) {
			if (nullFlags[index]) {
				return null;
			}
			return Byte.valueOf(Array.getByte(array, index));
		}
	}

	private static final class ArrayAccessCharacter implements ArrayAccess {
		@Override
		public Object getValue(final Object array, final int index, final boolean[] nullFlags) {
			if (nullFlags[index]) {
				return null;
			}
			return Character.valueOf(Array.getChar(array, index));
		}
	}

	private static final class ArrayAccessShort implements ArrayAccess {
		@Override
		public Object getValue(final Object array, final int index, final boolean[] nullFlags) {
			if (nullFlags[index]) {
				return null;
			}
			return Short.valueOf(Array.getShort(array, index));
		}
	}

	private static final class ArrayAccessInteger implements ArrayAccess {
		@Override
		public Object getValue(final Object array, final int index, final boolean[] nullFlags) {
			if (nullFlags[index]) {
				return null;
			}
			return Integer.valueOf(Array.getInt(array, index));
		}
	}

	private static final class ArrayAccessLong implements ArrayAccess {
		@Override
		public Object getValue(final Object array, final int index, final boolean[] nullFlags) {
			if (nullFlags[index]) {
				return null;
			}
			return Long.valueOf(Array.getLong(array, index));
		}
	}

	private static final class ArrayAccessFloat implements ArrayAccess {
		@Override
		public Object getValue(final Object array, final int index, final boolean[] nullFlags) {
			if (nullFlags[index]) {
				return null;
			}
			return Float.valueOf(Array.getFloat(array, index));
		}
	}

	private static final class ArrayAccessDouble implements ArrayAccess {
		@Override
		public Object getValue(final Object array, final int index, final boolean[] nullFlags) {
			if (nullFlags[index]) {
				return null;
			}
			return Double.valueOf(Array.getDouble(array, index));
		}
	}

	private static final class ArrayAccessObject implements ArrayAccess {
		@Override
		public Object getValue(final Object array, final int index, final boolean[] nullFlags) {
			return Array.get(array, index);
		}
	}
}