package com.bzsoft.wjdbc.test.ui;

import javax.swing.SwingWorker;

public class MyWorker extends SwingWorker<Integer, String> {

	private final Mission	m;

	// <dec>
	Block							block1	= new Block();
	Block							block2	= new Block();
	Block							block3	= new Block();
	Block							block4	= new Block();

	public MyWorker(final Mission mission) {
		this.m = mission;
	}

	@Override
	protected Integer doInBackground() throws Exception {
		// <exe>
		block1.addObserver(block2);
		block2.addObserver(block3);
		block3.addObserver(block4);
		block4.addObserver(block2);
		block1.update(null, m);

		return 4;
	}

}