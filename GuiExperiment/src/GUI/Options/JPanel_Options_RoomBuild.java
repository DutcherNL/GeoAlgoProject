package GUI.Options;

import GUI.Screen;
import Space.Lights;
import Space.PhaseControl.PhaseControl_Builder;
import Space.UpdateEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Contains the option buttons for the program
 * @author i_wou_000
 *
 */
public class JPanel_Options_RoomBuild extends JPanel_Options{
	
	
	public JPanel_Options_RoomBuild(Screen screen, PhaseControl_Builder Builder, Lights lights) {
		super(Builder.room);

		LayoutManager experimentLayout = new GridLayout(15,1);
		this.setLayout(experimentLayout);
		
		this.add(new JLabel(""));
		this.add(new JLabel("Room creation options"));
		
		// removeLastPoint button
		JButton button_RemoveLastPoint = new JButton("Remove Last Point");
		this.add(button_RemoveLastPoint);
		button_RemoveLastPoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Builder.removeLastVertex();
	         }          
	    });
		
		// clear button
		JButton button_ClearPoints = new JButton("Clear Points");
		this.add(button_ClearPoints);
		button_ClearPoints.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Builder.clearVertices();
	         }          
		});

		// removeLights button
		JButton button_RemoveLights = new JButton("Remove lights");
		this.add(button_RemoveLights);
		button_RemoveLights.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				screen.removeLights();
	         }
	    });
		
		// Compute shape button
		JButton button_Shape = new JButton("Define shape as room");
		this.add(button_Shape);
		button_Shape.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Builder.ExportToRoom();
	         }          
	    });
		
		this.add(new JLabel(""));
		this.add(new JLabel("Room alteration options"));

		
		// clear Fragments button
		JButton button_ClearFragment = new JButton("Clear Sections");
		this.add(button_ClearFragment);
		button_ClearFragment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Builder.clearFragments();
	         }          
		});
		
		// Compute visibility regions button
		JButton button_Save = new JButton("S4V3");
		this.add(button_Save);
		button_Save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Space.Space_ImportExport.Export(room);
					//screen.saveState();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
	    });

		// Compute visibility regions button
		JButton button_Load = new JButton("L04D");
		this.add(button_Load);
		button_Load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//screen.loadState();
				Space.Space_ImportExport.Import(room);
				Builder.onUpdate();
			}
	    });
		

		
		this.add(new JLabel(""));
		this.add(new JLabel("Light computation"));

		// Compute visibility regions button
		JButton button_Visibility = new JButton("Compute visibility regions");
		this.add(button_Visibility);
		button_Visibility.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lights.calculateVisibilityRegions();
			}
	    });


		
		room.addListener(new UpdateEvent() {
			public void onUpdate() {
				button_Shape.setEnabled(Builder.canClose());;
			}
		});	
	}
}
