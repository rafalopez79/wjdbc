package com.bzsoft.wjdbc.command;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * This class holds an array of class arrays which can be efficiently used for
 * Reflection on the serverside by just transporting the array identifier
 * instead of big objects.
 */
public final class ParameterTypeCombinations {

	private ParameterTypeCombinations() {
		// empty
	}

	public static final Class<?>[][]	TYPE_COMBINATIONS		= new Class[][] { new Class[0], // 0
		new Class[] { boolean.class }, // 1
		new Class[] { int.class }, // 2
		new Class[] { String.class }, // 3
		new Class[] { Map.class }, // 4
		// Pairs
		new Class[] { int.class, int.class }, // 5
		new Class[] { int.class, Calendar.class }, // 6
		new Class[] { String.class, boolean.class }, // 7
		new Class[] { String.class, byte.class }, // 8
		new Class[] { String.class, short.class }, // 9
		new Class[] { String.class, int.class }, // 10
		new Class[] { String.class, long.class }, // 11
		new Class[] { String.class, float.class }, // 12
		new Class[] { String.class, double.class }, // 13
		new Class[] { String.class, String.class }, // 14
		new Class[] { String.class, Date.class }, // 15
		new Class[] { String.class, Time.class }, // 16
		new Class[] { String.class, Timestamp.class }, // 17
		new Class[] { String.class, Calendar.class }, // 18
		new Class[] { String.class, URL.class }, // 19
		new Class[] { String.class, BigDecimal.class }, // 20
		new Class[] { String.class, byte[].class }, // 21
		new Class[] { String.class, int[].class }, // 22
		new Class[] { String.class, String[].class }, // 23
		// Triples
		new Class[] { int.class, int.class, int.class }, // 24
		new Class[] { String.class, int.class, int.class }, // 25
		new Class[] { int.class, int.class, String.class }, // 26
		new Class[] { String.class, int.class, String.class }, // 27
		new Class[] { String.class, String.class, String.class }, // 28
		new Class[] { String.class, Date.class, Calendar.class }, // 29
		new Class[] { String.class, Time.class, Calendar.class }, // 30
		new Class[] { String.class, Timestamp.class, Calendar.class }, // 31
		// Quad
		new Class[] { String.class, int.class, int.class, int.class }, // 32
		new Class[] { String.class, String.class, String.class, String.class }, // 33
		new Class[] { String.class, String.class, String.class, String[].class }, // 34
		new Class[] { String.class, String.class, String.class, int[].class }, // 35
		// Five
		new Class[] { String.class, String.class, String.class, int.class, boolean.class }, // 36
		new Class[] { String.class, String.class, String.class, boolean.class, boolean.class }, // 37
		// Six
		new Class[] { String.class, String.class, String.class, String.class, String.class, String.class } // 38
	};

	// Single
	public static final int				VOID						= 0;
	public static final int				BOL						= 1;
	public static final int				INT						= 2;
	public static final int				STR						= 3;
	public static final int				MAP						= 4;
	// Pairs
	public static final int				INTINT					= 5;
	public static final int				INTCAL					= 6;
	public static final int				STRBOL					= 7;
	public static final int				STRBYT					= 8;
	public static final int				STRSHT					= 9;
	public static final int				STRINT					= 10;
	public static final int				STRLNG					= 11;
	public static final int				STRFLT					= 12;
	public static final int				STRDBL					= 13;
	public static final int				STRSTR					= 14;
	public static final int				STRDAT					= 15;
	public static final int				STRTIM					= 16;
	public static final int				STRTMS					= 17;
	public static final int				STRCAL					= 18;
	public static final int				STRURL					= 19;
	public static final int				STRBID					= 20;
	public static final int				STRBYTA					= 21;
	public static final int				STRINTA					= 22;
	public static final int				STRSTRA					= 23;
	// Triples
	public static final int				INTINTINT				= 24;
	public static final int				STRINTINT				= 25;
	public static final int				INTINTSTR				= 26;
	public static final int				STRINTSTR				= 27;
	public static final int				STRSTRSTR				= 28;
	public static final int				STRDATCAL				= 29;
	public static final int				STRTIMCAL				= 30;
	public static final int				STRTMSCAL				= 31;
	// Quad
	public static final int				STRINTINTINT			= 32;
	public static final int				STRSTRSTRSTR			= 33;
	public static final int				STRSTRSTRSTRA			= 34;
	public static final int				STRSTRSTRINTA			= 35;
	// Five
	public static final int				STRSTRSTRINTBOL		= 36;
	public static final int				STRSTRSTRBOLBOL		= 37;
	// Six
	public static final int				STRSTRSTRSTRSTRSTR	= 38;
}
