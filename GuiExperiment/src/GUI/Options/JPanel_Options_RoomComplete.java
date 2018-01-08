package GUI.Options;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import Space.Room;
import Space.UpdateEvent;

public class JPanel_Options_RoomComplete extends JPanel_Options{
	private int width = 100;
	
	
	public JPanel_Options_RoomComplete(Room room) {
		super(room);

		LayoutManager experimentLayout = new GridLayout(10,1);
		this.setLayout(experimentLayout);
		
		// clear button
		JButton button_Clear = new JButton("Remove Room");
		button_Clear.setSize(this.width, 50);
		this.add(button_Clear);
		button_Clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	            room.clear();
	         }          
		});
		
		// Save button
		JButton button_Save = new JButton("Save points");
		this.add(button_Save);
		button_Save.setEnabled(false);
		
		
		// Compute shape button
		JButton button_Next = new JButton("Next");
		this.add(button_Next);
		

	}
}
