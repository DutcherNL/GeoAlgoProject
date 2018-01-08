package GUI;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Small section allowing cycling through the phases
 * @author i_wou_000
 *
 */
public class JPanel_PhaseScreen extends JPanel{

	private Screen screen;
	
	/**
	 * CONSTRUCTOR
	 * @param Screen The Screen controlling the program
	 */
	public JPanel_PhaseScreen(Screen Screen) {
		this.screen = Screen;
		
		// Set the layout
		LayoutManager experimentLayout = new GridLayout(2,1);
		this.setLayout(experimentLayout);
		
		// previous button
		JButton button_Previuos = new JButton("Previous phase");
		this.add(button_Previuos);
		button_Previuos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Previous pressed");
				
	            if (screen.canEnterPreviousPhase()) {
	            	screen.setPhase(false);
	            }}          
		});
		
		// Next button
		JButton button_Next = new JButton("Next phase");
		this.add(button_Next);
		button_Next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Next pressed");
				
				if (screen.canEnterNextPhase()) {
	            	screen.setPhase(true);
	            }}
		});
	}
	
	
}
