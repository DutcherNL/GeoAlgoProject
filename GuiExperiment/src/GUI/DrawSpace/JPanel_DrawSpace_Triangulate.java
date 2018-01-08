package GUI.DrawSpace;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;

import Space.RoomFragment;
import Space.PhaseControl.PhaseControl_Builder;
import Space.PhaseControl.PhaseControl_Triangulate;

public class JPanel_DrawSpace_Triangulate extends JPanel_DrawSpace {

	public final static Color VERTEX_COLOR_START = new Color(50, 120, 50);
	public final static Color VERTEX_COLOR_END = new Color(120, 50, 50);
	public final static Color VERTEX_COLOR_SPLIT = new Color(120, 50, 120);
	public final static Color VERTEX_COLOR_MERGE = new Color(50, 120, 120);
	
	public final static Color EDGE_COLOR = new Color(53, 53, 53);
	public final static Color WORKEDGE_COLOR = new Color(5, 53, 151);
	public final static Color BACKGROUND_COLOR = new Color(151, 151, 151);
	
	private PhaseControl_Triangulate roomTriangulator;
	
	public JPanel_DrawSpace_Triangulate(PhaseControl_Triangulate RoomTriangulator) {
		this.roomTriangulator = RoomTriangulator;

		

		this.roomTriangulator.addListener(this::repaint);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, size.width, size.height);
		
		// Draw all other fragment areas
		for(RoomFragment fragment : roomTriangulator.room.getFragments())
		{
			this.drawLines(g, fragment.getPoints(), EDGE_COLOR);
		}
		
		this.drawPoints(
				g,
				this.roomTriangulator.getPointType("START"),
				VERTEX_COLOR_START);
		this.drawPoints(
				g,
				this.roomTriangulator.getPointType("END"),
				VERTEX_COLOR_END);
		this.drawPoints(
				g,
				this.roomTriangulator.getPointType("MERGE"),
				VERTEX_COLOR_MERGE);
		this.drawPoints(
				g,
				this.roomTriangulator.getPointType("SPLIT"),
				VERTEX_COLOR_SPLIT);
	}
	
}
