package GUI;

import Space.*;
import Space.PhaseControl.*;

import javax.swing.*;

import GUI.*;
import GUI.DrawSpace.*;
import GUI.Options.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Launches the visual implementation of the program as well as the data storage objects
 * @author i_wou_000
 *
 */
public class Screen {

	private JFrame guiFrame;
	private Room room;
	private Lights lights;
	
	private JPanel_Options options;
	private JPanel_DrawSpace drawSpace;
	private JPanel_PhaseScreen phaseScreen;
	private GridBagConstraints GBC;
	private Space.PhaseControl.PhaseControl phaseController;
	
	private Label phaseName;
	private int phase = 0;
	/**
	 * Phase 0 = room build
	 * Phase 1 = Room acceptance (triangulisation)
	 * Phase 2 = Build Lights
	 */

	/**
	 * CONSTRUCTOR
	 */
	public Screen() {

		room = new Room();
		lights = new Lights(room);
		
		// Create the GUI frame
		guiFrame = new JFrame();
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setTitle("GEO algorithm");
		guiFrame.setLocationRelativeTo(null);
		
		// Set up the layout
		guiFrame.setLayout(new GridBagLayout());
		GBC = new GridBagConstraints();
		GBC.fill = GridBagConstraints.HORIZONTAL;
		
		// Set up the Phase Screen
		GBC.gridwidth = 1;
		GBC.gridheight = 1;
		GBC.gridx = 1;
		GBC.gridy = 2;
		GBC.anchor = GridBagConstraints.SOUTH;
		this.phaseScreen = new JPanel_PhaseScreen(this);
		guiFrame.add(this.phaseScreen, GBC);
		
		// Set up the Label
		GBC.anchor = GridBagConstraints.NORTH;
		phaseName = new Label();
		phaseName.setAlignment(1);
		GBC.gridx = 1;
		GBC.gridy = 0;
		guiFrame.add(phaseName, GBC);
		
		
		// Correct anchor
		GBC.anchor = GridBagConstraints.CENTER;
		
		// Set the correct phase
		setPhase(0);		
		// Display the JFrame	
		guiFrame.pack();		
		guiFrame.setVisible(true);
		
		
	}
	
	/**
	 * Determine whether the next phase can be entered
	 * @return Whether the next phase can be entered
	 */
	public boolean canEnterNextPhase() {	
		return this.phaseController.canGoEnterNextPhase();
	}
	
	/**
	 * Determine whether the previous phase can be entered
	 * @return
	 */
	public boolean canEnterPreviousPhase() {
		return phase>0;
	}
	
	/**
	 * Sets the phase to a new phase and alters the workspace
	 * @param Phase
	 */
	public void setPhase(int Phase) {
		this.phase = Phase;
		
		switch(Phase) {
			case 0:
				BuildPhase0();
				break;
			case 1:
				BuildPhase1();
				break;
			case 2:
				//
				break;
			default:
				this.phaseName.setText("ERROR phase out of scope");
		}
		
		guiFrame.revalidate();
		
		System.out.println("Phase is now "+this.phase);
	}
	
	/**
	 * Adjust the phase by 1
	 * @param increment Increments (true) or decrements (false) the current phase by 1
	 */
	public void setPhase(boolean increment) {
		if (increment)
			this.setPhase(this.phase + 1);
		else
			this.setPhase(this.phase - 1);
	}
	
	/**
	 * Resets the workspace to its basic form
	 */
	private void RemoveGeneral() {
		try {
			guiFrame.remove(this.drawSpace);
			guiFrame.remove(this.options);
		}catch(Exception e){
			// don't do nothing
		}
	}
	
	/**
	 * Rebuild the workspace
	 */
	private void BuildGeneral() {
		// Add the draw Space
		GBC.gridx = 0;
		GBC.gridy = 0;
		GBC.gridwidth = 1;
		GBC.gridheight = 3;
		guiFrame.add(drawSpace, GBC);
		drawSpace.repaint();
		
		// add the options screen
		GBC.gridwidth = 1;
		GBC.gridheight = 1;
		GBC.gridx = 1;
		GBC.gridy = 1;
		guiFrame.add(this.options, GBC);
		
		// Link the phase buttons to the phasecontrol
		phaseController.addListener(new UpdateEvent() {
			public void onUpdate() {
				phaseScreen.UpdateButtonStatus();
			}          
		});
		
		phaseScreen.UpdateButtonStatus();
	}
	
	/**
	 * Creates the necessary material for phase 0
	 */
	private void BuildPhase0() {
		RemoveGeneral();
		
		phaseController = new PhaseControl_Builder(room);
		this.phaseName.setText("Draw Room");
		this.options = new JPanel_Options_RoomBuild((PhaseControl_Builder)this.phaseController);
		drawSpace = new JPanel_DrawSpace_Builder((PhaseControl_Builder)this.phaseController);
		
		BuildGeneral();
	}
	
	/**
	 * Creates the necessary material for phase 1
	 */
	private void BuildPhase1() {
		RemoveGeneral();
		
		phaseController = new PhaseControl_Builder(room);
		this.phaseName.setText("Prepare Room");
		this.options = new JPanel_Options_RoomComplete(room);
		this.drawSpace = new JPanel_DrawSpace_Builder((PhaseControl_Builder)phaseController);
		
		BuildGeneral();
		
	}
	
}
