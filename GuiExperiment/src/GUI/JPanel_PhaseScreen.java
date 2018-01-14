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
	private JButton button_Previous;
	private JButton button_Next;
	
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
		this.button_Previous = new JButton("Previous phase");
		this.add(button_Previous);
		button_Previous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Previous pressed");
				
	            if (screen.canEnterPreviousPhase()) {
	            	screen.setPhase(false);
	            }}          
		});
		
		// Next button
		this.button_Next = new JButton("Next phase");
		this.add(button_Next);
		button_Next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Next pressed");
				
				if (screen.canEnterNextPhase()) {
	            	screen.setPhase(true);
	            }}
		});
	}
	
	public void UpdateButtonStatus() {
		this.button_Next.setEnabled(this.screen.canEnterNextPhase());
		this.button_Previous.setEnabled(this.screen.canEnterPreviousPhase());
	}
	
	
}
