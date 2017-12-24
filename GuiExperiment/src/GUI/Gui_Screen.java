package GUI;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Space.Room;

public class Gui_Screen {

	JFrame guiFrame;
	Room room;
	
	public Gui_Screen() {
		
		room = new Room();
		
		guiFrame = new JFrame();
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setTitle("Space");
		guiFrame.setLocationRelativeTo(null);
		
		Gui_DrawSpace drawSpace = new Gui_DrawSpace(room);
		guiFrame.add(drawSpace, BorderLayout.WEST);
		guiFrame.add(
				new Gui_Options(room),
				BorderLayout.EAST);
		drawSpace.repaint();
		
		
		// Display the JFrame	
		guiFrame.pack();		
		guiFrame.setVisible(true);
	}
	
	
}
