package GUI;

import Space.Lights;
import Space.Room;
import Space.UpdateEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Options extends JPanel{
	
	private Room room;
	private Lights lights;
	private int width = 100;
	
	
	public Options(Room room, Lights lights) {
		this.room = room;
		this.lights = lights;

		LayoutManager experimentLayout = new GridLayout(10,1);
		this.setLayout(experimentLayout);
		
		// clear button
		JButton button_Clear = new JButton("Clear Points");
		button_Clear.setSize(this.width, 50);
		this.add(button_Clear);
		button_Clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	            room.clear();
	            lights.clear();
	         }          
		});

		// removeLastPoint button
		JButton button_RemoveLastPoint = new JButton("Remove Last Point");
		button_RemoveLastPoint.setSize(this.width, 50);
		this.add(button_RemoveLastPoint);
		button_RemoveLastPoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	            room.removeLastPoint();
	         }          
	    });
		
		// Load button
		JButton button_Load = new JButton("Load external");
		this.add(button_Load);
		button_Load.setEnabled(false);
		
		// Save button
		JButton button_Save = new JButton("Save points");
		this.add(button_Save);
		button_Save.setEnabled(false);
		
		// Compute shape button
		JButton button_Shape = new JButton("Compute shape");
		this.add(button_Shape);
		
		room.addListener(new UpdateEvent() {
			public void onUpdate() {
				button_Shape.setEnabled(room.canClose());;
			}
		});

		// Compute visibilityRegion button
		JButton button_visibility = new JButton("Compute Visibility Region");
		this.add(button_visibility);
		button_visibility.addActionListener(e -> lights.calculateVisibilityRegions());
	}
}
