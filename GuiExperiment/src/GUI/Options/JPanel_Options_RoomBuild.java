package GUI.Options;

import Space.Lights;
import Space.PhaseControl.PhaseControl_Builder;
import Space.Room;
import Space.UpdateEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Contains the option buttons for the program
 * @author i_wou_000
 *
 */
public class JPanel_Options_RoomBuild extends JPanel_Options{
	
	
	public JPanel_Options_RoomBuild(PhaseControl_Builder Builder) { super(Builder.room);

		LayoutManager experimentLayout = new GridLayout(6,1);
		this.setLayout(experimentLayout);
		
		// clear Fragments button
		JButton button_ClearFragment = new JButton("Clear Sections");
		this.add(button_ClearFragment);
		button_ClearFragment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Builder.clearFragments();
	         }          
		});

		
		// clear button
		JButton button_ClearPoints = new JButton("Clear Points");
		this.add(button_ClearPoints);
		button_ClearPoints.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Builder.clearPoints();
	         }          
		});

		// removeLastPoint button
		JButton button_RemoveLastPoint = new JButton("Remove Last Point");
		this.add(button_RemoveLastPoint);
		button_RemoveLastPoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Builder.removeLastPoint();
	         }          
	    });
		
		// Load button
		JButton button_Load = new JButton("Load external");
		this.add(button_Load);
		button_Load.setEnabled(false);
		
		// Compute shape button
		JButton button_Shape = new JButton("Finish shape");
		this.add(button_Shape);
		button_Shape.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Builder.ExportToRoom();
	         }          
	    });
		
		room.addListener(new UpdateEvent() {
			public void onUpdate() {
				button_Shape.setEnabled(Builder.canClose());;
			}
		});	
	}
}
