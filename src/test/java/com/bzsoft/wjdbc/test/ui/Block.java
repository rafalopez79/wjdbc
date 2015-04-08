package com.bzsoft.wjdbc.test.ui;

import java.util.Observable;
import java.util.Observer;

public class Block extends Observable implements Observer {

	public Mission run(final Mission m) {
		m.setStatus(Mission.UNEXECUTED);
		return m;
	}

	@Override
	public void update(final Observable o, final Object arg) {
		final Mission m = this.run((Mission) arg);
		setChanged();
		notifyObservers(m);
	}
}