//VJDBC - Virtual JDBC
//Written by Michael Link
//Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.serial;

interface ArrayAccess {

	Object getValue(Object array, int index, boolean[] nullFlags);

}
