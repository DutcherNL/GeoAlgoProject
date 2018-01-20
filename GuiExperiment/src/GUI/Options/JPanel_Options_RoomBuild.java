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

		LayoutManager experimentLayout = new GridLayout(6,1);
		this.setLayout(experimentLayout);
		
		// clear Fragments button
		JButton button_ClearFragment = new JButton("Clear Sections");
		this.add(button_ClearFragment);
		button_ClearFragment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Builder.clearFragments();
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

		// removeLastPoint button
		JButton button_RemoveLastPoint = new JButton("Remove Last Point");
		this.add(button_RemoveLastPoint);
		button_RemoveLastPoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Builder.removeLastVertex();
	         }          
	    });
		
//		// Load button
//		JButton button_Load = new JButton("Load external");
//		this.add(button_Load);
//		button_Load.setEnabled(false);
		
		// Compute shape button
		JButton button_Shape = new JButton("Finish shape");
		this.add(button_Shape);
		button_Shape.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Builder.ExportToRoom();
	         }          
	    });

		// Compute visibility regions button
		JButton button_Visibility = new JButton("Compute visibility regions");
		this.add(button_Visibility);
		button_Visibility.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lights.calculateVisibilityRegions();
			}
	    });

		// Compute visibility regions button
		JButton button_Save = new JButton("S4V3");
		this.add(button_Save);
		button_Save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					screen.saveState();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
	    });

		// Compute visibility regions button
		JButton button_Load = new JButton("L04D");
		this.add(button_Load);
		button_Load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					screen.loadState();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
	    });
		
		room.addListener(new UpdateEvent() {
			public void onUpdate() {
				button_Shape.setEnabled(Builder.canClose());;
			}
		});	
	}
}
