package GUI.Options;

import GUI.SVG;
import Space.PhaseControl.PhaseControl_LineSweep;
import Space.RoomFragment;
import Space.UpdateEvent;
import Space.Vertex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JPanel_Options_LineSweep  extends JPanel_Options{

	public JPanel_Options_LineSweep(PhaseControl_LineSweep Sweeper, SVG svg) {
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
				button_FinalCompute.setText("Flip visuals");
			
				Sweeper.visualizeShape = !Sweeper.visualizeShape;
				if (Sweeper.Shape == null) {
					Sweeper.CompleteShape();
				}
				Sweeper.onUpdate();
		    }          
		});
		
		// Print button
		JButton button_StoreShell= new JButton("Store Shell in room");
		this.add(button_StoreShell);
		button_StoreShell.setEnabled(false);
		button_StoreShell.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					Sweeper.room.clear();
					
					java.util.ArrayList<Vertex> list = new java.util.ArrayList<Vertex>();
					Vertex root = Sweeper.Shape.get(0).startPoint;
					Vertex v = root;;
					do {
						list.add(v);
						v = v.getNext();
					} while (v != root);
					
					
					Sweeper.room.addFragment(new RoomFragment(list));
					
			    }          
		});

		// Compute visibility regions button
		JButton button_Export = new JButton("Export");
		this.add(button_Export);
		button_Export.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				svg.export();
			}
		});
		
		
		Sweeper.addListener(new UpdateEvent() {
			public void onUpdate() {
				button_FinalCompute.setEnabled(Sweeper.shapeComplete);
				button_FullSweep.setEnabled(!Sweeper.shapeComplete);
				button_SingleSweep.setEnabled(!Sweeper.shapeComplete);
				button_StoreShell.setEnabled(Sweeper.Shape != null);
			}
		});
		
			
	
	
	}
	
	

	
}
