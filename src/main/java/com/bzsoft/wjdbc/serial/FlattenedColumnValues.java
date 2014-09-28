package com.bzsoft.wjdbc.serial;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;

class FlattenedColumnValues implements Externalizable {

	private static final long	serialVersionUID	= 3691039872299578672L;

	private Object					array;
	private boolean[]				nullFlags;

	private Class<?>				clazz;
	private int						size;

	public FlattenedColumnValues() {
		// empty
	}

	protected FlattenedColumnValues(final Class<?> clazz, final int size) {
		// Any of these types ? boolean, byte, char, short, int, long, float, and
		// double
		this.clazz = clazz;
		if (clazz.isPrimitive()) {
			array = createPrimitiveTypeArray(clazz, size);
			nullFlags = new boolean[size];
		} else {
			array = Array.newInstance(clazz, size);
			nullFlags = null;
		}
		this.size = size;
	}

	private static final Object createPrimitiveTypeArray(final Class<?> clazz, final int size) {
		if (clazz == int.class) {
			return new int[size];
		} else if (clazz == short.class) {
			return new short[size];
		} else if (clazz == boolean.class) {
			return new boolean[size];
		} else if (clazz == float.class) {
			return new float[size];
		} else if (clazz == double.class) {
			return new double[size];
		} else if (clazz == long.class) {
			return new long[size];
		} else if (clazz == char.class) {
			return new char[size];
		} else if (clazz == byte.class) {
			return new byte[size];
		} else {
			return null;
		}
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		array = in.readObject();
		clazz = array.getClass().getComponentType();
		final boolean isNull = in.readBoolean();
		if (isNull) {
			nullFlags = null;
		} else {
			final int s = in.readInt();
			nullFlags = new boolean[s];
			for (int i = 0; i < s; i++) {
				nullFlags[i] = in.readBoolean();
			}
		}
		this.size = Array.getLength(array);
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeObject(array);
		if (nullFlags == null) {
			out.writeBoolean(true);
		} else {
			out.writeBoolean(false);
			out.writeInt(nullFlags.length);
			for (final boolean b : nullFlags) {
				out.writeBoolean(b);
			}
		}

	}

	void setObject(final int index, final Object value) {
		ensureCapacity(index + 1);
		Array.set(array, index, value);
	}

	void setBoolean(final int index, final boolean value) {
		ensureCapacity(index + 1);
		((boolean[]) array)[index] = value;
	}

	void setByte(final int index, final byte value) {
		ensureCapacity(index + 1);
		((byte[]) array)[index] = value;
	}

	void setShort(final int index, final short value) {
		ensureCapacity(index + 1);
		((short[]) array)[index] = value;
	}

	void setInt(final int index, final int value) {
		ensureCapacity(index + 1);
		((int[]) array)[index] = value;
	}

	void setLong(final int index, final long value) {
		ensureCapacity(index + 1);
		((long[]) array)[index] = value;
	}

	void setFloat(final int index, final float value) {
		ensureCapacity(index + 1);
		((float[]) array)[index] = value;
	}

	void setDouble(final int index, final double value) {
		ensureCapacity(index + 1);
		((double[]) array)[index] = value;
	}

	void setIsNull(final int index) {
		ensureCapacity(index + 1);
		if (nullFlags != null) {
			nullFlags[index] = true;
		}
	}

	Object getValue(final int index) {
		if (nullFlags != null && nullFlags[index]) {
			return null;
		} else if (clazz == int.class) {
			return ((int[]) array)[index];
		} else if (clazz == short.class) {
			return ((short[]) array)[index];
		} else if (clazz == boolean.class) {
			return ((boolean[]) array)[index];
		} else if (clazz == float.class) {
			return ((float[]) array)[index];
		} else if (clazz == double.class) {
			return ((double[]) array)[index];
		} else if (clazz == long.class) {
			return ((long[]) array)[index];
		} else if (clazz == char.class) {
			return ((char[]) array)[index];
		} else if (clazz == byte.class) {
			return ((byte[]) array)[index];
		} else {
			return ((Object[]) array)[index];
		}
	}

	void ensureCapacity(final int minCapacity) {
		final int oldCapacity = size;
		if (minCapacity > oldCapacity) {
			int newCapacity = oldCapacity * 3 / 2 + 1;
			if (newCapacity < minCapacity) {
				newCapacity = minCapacity;
			}
			final Object tmpArrayOfValues = array;
			if (clazz.isPrimitive()) {
				array = createPrimitiveTypeArray(clazz, newCapacity);
			} else {
				array = Array.newInstance(clazz, newCapacity);
			}
			System.arraycopy(tmpArrayOfValues, 0, array, 0, size);
			if (nullFlags != null) {
				final boolean[] tmpNullFlags = nullFlags;
				nullFlags = new boolean[newCapacity];
				System.arraycopy(tmpNullFlags, 0, nullFlags, 0, size);
			}
			size = newCapacity;
		}
	}

	static FlattenedColumnValues compress(final FlattenedColumnValues data, final int maxRows) {
		final FlattenedColumnValues out = new FlattenedColumnValues(data.clazz, maxRows);
		if (data.nullFlags != null) {
			System.arraycopy(data.nullFlags, 0, out.nullFlags, 0, maxRows);
		}
		System.arraycopy(data.array, 0, out.array, 0, maxRows);
		return out;
	}
}
