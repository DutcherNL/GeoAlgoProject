package GUI.Options;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import Space.Room;
import Space.UpdateEvent;
import Space.PhaseControl.PhaseControl_Triangulate;

public class JPanel_Options_RoomTriangulate extends JPanel_Options{
	private int width = 100;
	
	PhaseControl_Triangulate phaseController;
	
	public JPanel_Options_RoomTriangulate(PhaseControl_Triangulate PhaseController) {
		super(PhaseController.room);
		this.phaseController = PhaseController;

		LayoutManager experimentLayout = new GridLayout(10,1);
		this.setLayout(experimentLayout);
		
		// Save button
		JButton button_Save = new JButton("Save Room Design");
		this.add(button_Save);
		button_Save.setEnabled(false);
		
		// Compute button
		JButton button_Compute_Points = new JButton("Compute Points");
		button_Compute_Points.setSize(this.width, 50);
		this.add(button_Compute_Points);
		button_Compute_Points.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				phaseController.ComputePointTypes();
	         }          
		});
		

		
		
		// Compute shape button
		JButton button_Next = new JButton("Next");
		this.add(button_Next);
		

	}
}
