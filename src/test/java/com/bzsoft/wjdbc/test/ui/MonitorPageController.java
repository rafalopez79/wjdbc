package com.bzsoft.wjdbc.test.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class MonitorPageController implements Observer {

	private final List<Mission>	missions;

	public MonitorPageController() {
		missions = new ArrayList<Mission>();
		for (int i = 0; i < 2; i++) {
			final Mission m = new Mission(i);
			m.addObserver(this);
			missions.add(m);
		}
	}

	private final JLabel	jl	= new JLabel("Hello");

	class StartButtonListener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			for (int i = 0; i < missions.size(); i++) {
				final MyWorker worker = new MyWorker(missions.get(i));
				worker.execute();
			}
		}
	}

	@Override
	public void update(final Observable o, final Object arg) {
		if (o instanceof Mission) {
			final Mission m = (Mission) o;
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					System.out.println("!");
					jl.setText(Integer.toString(m.getStatus()));
				}
			});

		}
	}

	public static void main(final String[] args) {
		final MonitorPageController a = new MonitorPageController();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final JFrame jf = new JFrame();
				jf.setSize(800, 600);
				final JButton jb = new JButton("Start");
				jb.addActionListener(a.new StartButtonListener());
				jf.add(jb, BorderLayout.NORTH);
				jf.add(a.jl, BorderLayout.CENTER);
				jf.setVisible(true);
			}
		});
	}

}
