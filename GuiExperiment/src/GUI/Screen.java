package GUI;

import Space.Lights;
import Space.Room;

import javax.swing.*;
import java.awt.*;

public class Screen {

	private JFrame guiFrame;
	private Room room;
	private Lights lights;

	public Screen() {

		room = new Room();
		lights = new Lights(room);

		guiFrame = new JFrame();
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setTitle("Space");
		guiFrame.setLocationRelativeTo(null);
		
		DrawSpace drawSpace = new DrawSpace(room, lights);

		guiFrame.add(drawSpace, BorderLayout.WEST);
		guiFrame.add(
				new Options(room, lights),
				BorderLayout.EAST);
		drawSpace.repaint();
		
		
		// Display the JFrame	
		guiFrame.pack();		
		guiFrame.setVisible(true);
	}
	
	
}
