package GUI.DrawSpace;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import Space.Lights;
import Space.RoomFragment;
import Space.SweepDomain;
import Space.Vertex;
import Space.PhaseControl.PhaseControl_LineSweep;

public class JPanel_DrawSpace_LineSweep extends JPanel_DrawSpace{
	
	public final static Color POINT_COLOR_START = new Color(120, 53, 53);
	public final static Color POINT_COLOR_DOMAIN = new Color(53, 120, 53);
	public final static Color EDGE_COLOR = new Color(53, 53, 53);
	public final static Color YLINE_COLOR = new Color(53, 53, 53);
	public final static Color BACKGROUND_COLOR = new Color(151, 151, 151);
	public final static Color EDGE_ERROR_COLOR = Color.RED;
	
	private PhaseControl_LineSweep roomSweeper;
	
	public JPanel_DrawSpace_LineSweep(PhaseControl_LineSweep RoomSweeper) {
		this.roomSweeper = RoomSweeper;

		//zoomScope();
		this.addMouseListener(this);

		this.roomSweeper.addListener(this::repaint);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, size.width, size.height);
		
		// Draw all other fragment areas
		for(RoomFragment fragment : roomSweeper.room.getFragments()) {
			this.drawLines(g, fragment.getVertices(), EDGE_COLOR);
		}
		
		g.setColor(YLINE_COLOR);
		g.drawLine(
				(int)(edgeCorrection),
				(int)((roomSweeper.yLine - start_y) * zoomFactor_y + edgeCorrection),
				(int)(size.getWidth() -  edgeCorrection),
				(int)((roomSweeper.yLine - start_y) * zoomFactor_y + edgeCorrection)
				);
		
		if (this.roomSweeper.startVertices != null)
			this.drawVertices(g, this.roomSweeper.startVertices, POINT_COLOR_START);
		
		if (this.roomSweeper.status != null) {
			Vertex nextVertexFromDomain = this.roomSweeper.status.getTopVertex();
			if (nextVertexFromDomain != null) {
				g.setColor(POINT_COLOR_DOMAIN);
				this.drawVertice(g, nextVertexFromDomain);
			}
			
			List<SweepDomain> Domains = new ArrayList<SweepDomain>();
			this.roomSweeper.status.exportToList(Domains);
			
			for (int i = 0; i < Domains.size(); i++) {
				g.setColor(new Color(40*i, 40, 40));
				g.drawLine(
						(int)((Domains.get(i).leftSegment.endVertex.x - start_x) * zoomFactor_x + edgeCorrection),
						(int)((Domains.get(i).leftSegment.endVertex.y - start_y) * zoomFactor_y + edgeCorrection),
						(int)((Domains.get(i).rightSegment.endVertex.x - start_x) * zoomFactor_x + edgeCorrection),
						(int)((Domains.get(i).rightSegment.endVertex.y - start_y) * zoomFactor_y + edgeCorrection)
						);
			}
		}
	}

}
