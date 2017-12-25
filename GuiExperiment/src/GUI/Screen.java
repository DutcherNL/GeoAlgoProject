package GUI;

import Space.Room;

import javax.swing.*;
import java.awt.*;

public class Screen {

	private JFrame guiFrame;
	private Room room;

	public Screen() {

		room = new Room();
		
		guiFrame = new JFrame();
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setTitle("Space");
		guiFrame.setLocationRelativeTo(null);
		
		DrawSpace drawSpace = new DrawSpace(room);
		guiFrame.add(drawSpace, BorderLayout.WEST);
		guiFrame.add(
				new Options(room),
				BorderLayout.EAST);
		drawSpace.repaint();
		
		
		// Display the JFrame	
		guiFrame.pack();		
		guiFrame.setVisible(true);
	}
	
	
}
