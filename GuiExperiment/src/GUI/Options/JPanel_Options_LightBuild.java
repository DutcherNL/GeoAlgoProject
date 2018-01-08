package GUI.Options;

import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JPanel;

import Space.Lights;
import Space.Room;

public class JPanel_Options_LightBuild extends JPanel{

	private Room room;
	private Lights lights;
	private int width = 100;
	
	
	public JPanel_Options_LightBuild(Room room, Lights lights) {
		this.room = room;
		this.lights = lights;

		LayoutManager experimentLayout = new GridLayout(10,1);
		this.setLayout(experimentLayout);
		
		// Compute visibilityRegion button
		JButton button_visibility = new JButton("Finish Room");
		this.add(button_visibility);
		button_visibility.addActionListener(e -> lights.calculateVisibilityRegions());
	}
}
