package GUI.DrawSpace;

import Space.Lights;
import Space.PhaseControl.PhaseControl_Builder;
import Space.RoomFragment;
import Space.Vertex;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.List;

public class JPanel_DrawSpace_Builder extends JPanel_DrawSpace implements MouseMotionListener {

	public final static Color POINT_COLOR = new Color(53, 53, 53);
	public final static Color EDGE_COLOR = new Color(53, 53, 53);
	public final static Color WORKEDGE_COLOR = new Color(5, 53, 151);
	public final static Color BACKGROUND_COLOR = new Color(151, 151, 151);
	public final static Color EDGE_ERROR_COLOR = Color.RED;
	
	private PhaseControl_Builder roomBuilder;
	private Lights lights;
	
	public JPanel_DrawSpace_Builder(PhaseControl_Builder RoomBuilder, Lights lights) {
		this.roomBuilder = RoomBuilder;
		this.lights = lights;

		//zoomScope();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		this.roomBuilder.addListener(this::repaint);
		this.lights.addListener(this::repaint);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		List<Vertex> roomPoints = roomBuilder.getVertices();

		for (List<Vertex> region : lights.getVisibilityRegions()) {
			this.drawPolygon(g, region, Lights.REGION_COLOR);
		}

		// Draw the lines, draw last line different if it can't close
		if (this.roomBuilder.canClose()) {
			this.drawLines(g, roomPoints, WORKEDGE_COLOR);
		}
		else {
			this.drawLines(g, roomPoints, WORKEDGE_COLOR, EDGE_ERROR_COLOR);
		}

		// Draw all points of the current figure
		this.drawVertices(g, roomPoints, POINT_COLOR);

		this.drawPoints(g, lights.getTemp(), Color.MAGENTA);

		// Draw all other fragment areas
		for(RoomFragment fragment : roomBuilder.room.getFragments()) {
			this.drawLines(g, fragment.getVertices(), EDGE_COLOR);
		}

		this.drawPoints(g, lights.getLights(), Lights.POINT_COLOR);
		
		g.setColor(Color.CYAN);		
		this.drawPoint(g, this.roomBuilder.Intersection);
	}
	
	private boolean dragging = false;

	private void drag(MouseEvent me) {
		int x = (int) ((me.getX() - edgeCorrection) / zoomFactor_x + start_x);
		int y = (int) ((me.getY() - edgeCorrection) / zoomFactor_y + start_y);

		lights.clear();
		lights.addLight(new Point(x, y));
		lights.calculateVisibilityRegions();
	}

	@Override
	public void mousePressed(MouseEvent me) {
		int x = (int)((me.getX() - edgeCorrection) / zoomFactor_x + start_x);
		int y = (int)((me.getY() - edgeCorrection) / zoomFactor_y + start_y);

		if (me.getButton() == MouseEvent.BUTTON1) {
			this.roomBuilder.addVertex(x, y);
		} else if (me.getButton() == MouseEvent.BUTTON2) {
			dragging = true;
			drag(me);
		} else if (me.getButton() == MouseEvent.BUTTON3) {
			lights.addLight(new Point(x, y));
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		if (me.getButton() == MouseEvent.BUTTON2) {
			dragging = false;
			drag(me);
		}
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		if (dragging) {
			drag(me);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {}
}
