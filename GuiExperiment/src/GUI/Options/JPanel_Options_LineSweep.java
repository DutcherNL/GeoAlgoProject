package GUI.Options;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import Space.Room;
import Space.UpdateEvent;
import Space.PhaseControl.PhaseControl_LineSweep;

public class JPanel_Options_LineSweep  extends JPanel_Options{

	public JPanel_Options_LineSweep(PhaseControl_LineSweep Sweeper) {
		super(Sweeper.room);

		LayoutManager experimentLayout = new GridLayout(6,1);
		this.setLayout(experimentLayout);
		
		// Startpoitns button
		JButton button_FullSweep= new JButton("Run full sweep");
		this.add(button_FullSweep);
		button_FullSweep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					Sweeper.runFullSweep();
			    };
		});

		
		// Sweepline button
		JButton button_SingleSweep= new JButton("NextSweepLine");
		this.add(button_SingleSweep);
		button_SingleSweep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sweeper.runSingleSweep();
			    }          
		});
		
		// Print button
		JButton button_Print= new JButton("Print");
		this.add(button_Print);
		button_Print.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sweeper.PrintTree();
			    }          
		});
	
		// Print button
		JButton button_FinalCompute= new JButton("Compute Final Result");
		this.add(button_FinalCompute);
		button_FinalCompute.setEnabled(false);
		button_FinalCompute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					Sweeper.visualizeShape = !Sweeper.visualizeShape;
					if (Sweeper.Shape == null) {
						Sweeper.CompleteShape();
					}
					Sweeper.onUpdate();
			    }          
		});
		
		Sweeper.addListener(new UpdateEvent() {
			public void onUpdate() {
				button_FinalCompute.setEnabled(Sweeper.shapeComplete);
				button_FullSweep.setEnabled(!Sweeper.shapeComplete);
				button_SingleSweep.setEnabled(!Sweeper.shapeComplete);
			}
		});
		
			
	
	
	}
	
	

	
}
