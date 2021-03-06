package GUI;

import GUI.DrawSpace.JPanel_DrawSpace;
import GUI.DrawSpace.JPanel_DrawSpace_Builder;
import GUI.DrawSpace.JPanel_DrawSpace_LineSweep;
import GUI.Options.JPanel_Options;
import GUI.Options.JPanel_Options_LineSweep;
import GUI.Options.JPanel_Options_RoomBuild;
import Space.Lights;
import Space.PhaseControl.PhaseControl_Builder;
import Space.PhaseControl.PhaseControl_LineSweep;
import Space.Room;
import Space.UpdateEvent;
import Space.Vertex;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;


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
	 * Phase 1 = Room acceptance (triangulation)
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
		guiFrame.setLocation(100, 50);
		
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
		guiFrame.setResizable(false);
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
		drawSpace = new JPanel_DrawSpace_Builder((PhaseControl_Builder)this.phaseController, lights);
		SVG svg = new SVG(drawSpace);
		this.options = new JPanel_Options_RoomBuild(this, (PhaseControl_Builder)this.phaseController, lights, svg);

		BuildGeneral();
	}
	
	/**
	 * Creates the necessary material for phase 1
	 */
	private void BuildPhase1() {
		RemoveGeneral();


		phaseController = new PhaseControl_LineSweep(room, lights);
		this.phaseName.setText("Prepare Room");
		this.drawSpace = new JPanel_DrawSpace_LineSweep((PhaseControl_LineSweep)phaseController);
		SVG svg = new SVG(drawSpace);
		this.options = new JPanel_Options_LineSweep((PhaseControl_LineSweep)phaseController, svg);

		BuildGeneral();
		
	}

	public void saveState() throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("./state.dat"));
		out.writeObject(lights.getLights());
		out.writeObject(room.getVertices());
		out.close();
	}

	public void loadState() throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("./state.dat"));
		java.util.List<Point2D> lightsList = (java.util.List<Point2D>) in.readObject();
		lights.setLights(lightsList);
		room.setVertices((java.util.List<Vertex>) in.readObject());
		this.drawSpace.repaint();
		in.close();
	}

    public void removeLights() {
		lights.clear();
		this.drawSpace.repaint();
    }
}
