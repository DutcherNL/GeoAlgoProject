package GUI.Options;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import Space.Room;
import Space.PhaseControl.PhaseControl_LineSweep;

public class JPanel_Options_LineSweep  extends JPanel_Options{

	public JPanel_Options_LineSweep(PhaseControl_LineSweep Sweeper) {
		super(Sweeper.room);

		LayoutManager experimentLayout = new GridLayout(6,1);
		this.setLayout(experimentLayout);
		
		// Startpoitns button
		JButton button_StartPoints= new JButton("Compute StartPoints");
		this.add(button_StartPoints);
		button_StartPoints.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sweeper.ComputeStartPoints();
				Sweeper.StartSweepStepWise();
			    }          
		});
		
		// Startpoitns button
		JButton button_SweepPrep= new JButton("Prepare sweep");
		this.add(button_SweepPrep);
		button_SweepPrep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sweeper.StartSweepStepWise();
			    }          
		});
		
		// Startpoitns button
		JButton button_SweepNext= new JButton("NextSweepLine");
		this.add(button_SweepNext);
		button_SweepNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sweeper.sweepNextPoint();
			    }          
		});
	}
	
	

	
}
