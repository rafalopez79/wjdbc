package com.bzsoft.wjdbc.serial;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;

class FlattenedColumnValues implements Externalizable {

	private static final long		serialVersionUID	= 3691039872299578672L;

	private Object						arrayOfValues;
	private boolean[]					nullFlags;

	private transient Class<?>		clazz;
	private transient ArrayAccess	arrayAccessor;

	public FlattenedColumnValues() {
		// empty
	}

	FlattenedColumnValues(final Class<?> clazz, final int size) {
		// Any of these types ? boolean, byte, char, short, int, long, float, and
		// double
		this.clazz = clazz;
		if (clazz.isPrimitive()) {
			arrayOfValues = Array.newInstance(clazz, size);
			nullFlags = new boolean[size];
			arrayAccessor = ArrayAccessors.getArrayAccessorForPrimitiveType(clazz);
		} else {
			arrayOfValues = Array.newInstance(clazz, size);
			nullFlags = null;
			arrayAccessor = ArrayAccessors.getObjectArrayAccessor();
		}
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		arrayOfValues = in.readObject();
		final boolean isNull = in.readBoolean();
		if (isNull) {
			nullFlags = null;
		} else {
			final int size = in.readInt();
			nullFlags = new boolean[size];
			for (int i = 0; i < size; i++) {
				nullFlags[i] = in.readBoolean();
			}
		}
		final Class<?> componentType = arrayOfValues.getClass().getComponentType();
		if (componentType.isPrimitive()) {
			arrayAccessor = ArrayAccessors.getArrayAccessorForPrimitiveType(componentType);
		} else {
			arrayAccessor = ArrayAccessors.getObjectArrayAccessor();
		}
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeObject(arrayOfValues);
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
		Array.set(arrayOfValues, index, value);
	}

	void setBoolean(final int index, final boolean value) {
		ensureCapacity(index + 1);
		Array.setBoolean(arrayOfValues, index, value);
	}

	void setByte(final int index, final byte value) {
		ensureCapacity(index + 1);
		Array.setByte(arrayOfValues, index, value);
	}

	void setShort(final int index, final short value) {
		ensureCapacity(index + 1);
		Array.setShort(arrayOfValues, index, value);
	}

	void setInt(final int index, final int value) {
		ensureCapacity(index + 1);
		Array.setInt(arrayOfValues, index, value);
	}

	void setLong(final int index, final long value) {
		ensureCapacity(index + 1);
		Array.setLong(arrayOfValues, index, value);
	}

	void setFloat(final int index, final float value) {
		ensureCapacity(index + 1);
		Array.setFloat(arrayOfValues, index, value);
	}

	void setDouble(final int index, final double value) {
		ensureCapacity(index + 1);
		Array.setDouble(arrayOfValues, index, value);
	}

	void setIsNull(final int index) {
		ensureCapacity(index + 1);
		if (nullFlags != null) {
			nullFlags[index] = true;
		}
	}

	Object getValue(final int index) {
		return arrayAccessor.getValue(arrayOfValues, index, nullFlags);
	}

	void ensureCapacity(final int minCapacity) {
		final int oldCapacity = Array.getLength(arrayOfValues);
		if (minCapacity > oldCapacity) {
			int newCapacity = oldCapacity * 3 / 2 + 1;
			if (newCapacity < minCapacity) {
				newCapacity = minCapacity;
			}
			final Object tmpArrayOfValues = arrayOfValues;
			arrayOfValues = Array.newInstance(tmpArrayOfValues.getClass().getComponentType(), newCapacity);
			System.arraycopy(tmpArrayOfValues, 0, arrayOfValues, 0, Array.getLength(tmpArrayOfValues));
			if (nullFlags != null) {
				final boolean[] tmpNullFlags = nullFlags;
				nullFlags = new boolean[newCapacity];
				System.arraycopy(tmpNullFlags, 0, nullFlags, 0, tmpNullFlags.length);
			}
		}
	}

	static FlattenedColumnValues compress(final FlattenedColumnValues data, final int maxRows) {
		final FlattenedColumnValues out = new FlattenedColumnValues(data.clazz, maxRows);
		if (data.nullFlags != null) {
			System.arraycopy(data.nullFlags, 0, out.nullFlags, 0, maxRows);
		}
		System.arraycopy(data.arrayOfValues, 0, out.arrayOfValues, 0, maxRows);
		return out;
	}
}
