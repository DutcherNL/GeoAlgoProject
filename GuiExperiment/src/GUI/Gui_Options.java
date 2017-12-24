package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import Space.Room;
import Space.UpdateEvent;

public class Gui_Options extends JPanel{
	
	private Room room;
	int width = 100;
	
	
	public Gui_Options(Room Room) {
		this.room = Room;
		
		LayoutManager experimentLayout = new GridLayout(10,1);
		this.setLayout(experimentLayout);
		
		// Clear button
		JButton button_Clear = new JButton("Clear Points");
		button_Clear.setSize(this.width, 50);
		this.add(button_Clear);
		button_Clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	            room.Clear();
	         }          
	      });
		// RemoveLastPoint button
		JButton button_RemoveLastPoint = new JButton("Remove Last Point");
		button_RemoveLastPoint.setSize(this.width, 50);
		this.add(button_RemoveLastPoint);
		button_RemoveLastPoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	            room.RemoveLastPoint();
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
		
		
		
		
	}
	

}
