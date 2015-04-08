package com.bzsoft.wjdbc.adapter;

public final class ThreadLocalSQLConverter implements SQLConverter {

	private static final ThreadLocal<SQLConverter> THL = new InheritableThreadLocal<SQLConverter>();

	protected ThreadLocalSQLConverter(){
		//empty
	}

	public static void setConverter(final SQLConverter sc){
		THL.set(sc);
	}

	@Override
	public String convert(final String sql) {
		final SQLConverter conv = THL.get();
		if (conv != null){
			return conv.convert(sql);
		}
		return sql;
	}

}
