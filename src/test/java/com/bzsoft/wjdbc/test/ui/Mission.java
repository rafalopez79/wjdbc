package com.bzsoft.wjdbc.test.ui;

import java.util.Observable;

public class Mission extends Observable {

	public static final int	UNEXECUTED	= -1;

	private int					status;

	public Mission(final int c) {
		status = c;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(final int status) {
		this.status = status;
		setChanged();
		notifyObservers();
	}
}
