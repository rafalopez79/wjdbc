package com.bzsoft.wjdbc.serial;

interface ArrayAccess {

	Object getValue(Object array, int index, boolean[] nullFlags);

}
