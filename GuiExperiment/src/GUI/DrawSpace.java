package GUI;

import Space.Lights;
import Space.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class DrawSpace extends JPanel implements MouseListener{

	private Room room;
	private Lights lights;

	private Dimension size = new Dimension(400,400);
	private int pointWidth = 10;
	private double start_x = 0;
	private double start_y = 0;
	private double zoomFactor_x = 1;
	private double zoomFactor_y = 1;
	private double edgeCorrection = 20;
	
	public DrawSpace(Room room, Lights lights) {
		this.room = room;
		this.lights = lights;

		//zoomScope();
		this.addMouseListener(this);

		this.room.addListener(this::repaint);
		this.lights.addListener(this::repaint);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	
		List<Point> roomPoints = room.getPoints();

		g.setColor(Room.BACKGROUND_COLOR);
		g.fillRect(0, 0, size.width, size.height);

		for (List<Point> region : lights.getVisibilityRegions()) {
			this.drawPolygon(g, region, Lights.REGION_COLOR);
		}

		this.drawLines(g, roomPoints, Room.EDGE_COLOR, Room.EDGE_ERROR_COLOR);
		this.drawPoints(g, roomPoints, Room.POINT_COLOR);

		this.drawPoints(g, lights.getLights(), Lights.POINT_COLOR);
	}

	private void drawPoints(Graphics g, List<Point> points, Color color) {
		g.setColor(color);
		for(int i=0; i<points.size();i++) {
			g.fillOval(
					(int)((points.get(i).x - start_x) * zoomFactor_x + edgeCorrection - pointWidth / 2),
					(int)((points.get(i).y - start_y) * zoomFactor_y + edgeCorrection - pointWidth / 2),
					pointWidth,
					pointWidth);
		}
	}

	private void drawLines(Graphics g, List<Point> points, Color color, Color errorColor) {
		g.setColor(color);

		for(int i=0; i + 1<points.size();i++) {
			g.drawLine(
					(int)((points.get(i).x - start_x) * zoomFactor_x + edgeCorrection),
					(int)((points.get(i).y - start_y) * zoomFactor_y + edgeCorrection),
					(int)((points.get(i+1).x - start_x) * zoomFactor_x + edgeCorrection),
					(int)((points.get(i+1).y - start_y) * zoomFactor_y + edgeCorrection)
					);
		}

		if (!room.canClose()) {
			g.setColor(errorColor);
		}

		if (points.size() > 1) {
			g.drawLine(
					(int) ((points.get(0).x - start_x) * zoomFactor_x + edgeCorrection),
					(int) ((points.get(0).y - start_y) * zoomFactor_y + edgeCorrection),
					(int) ((points.get(points.size() - 1).x - start_x) * zoomFactor_x + edgeCorrection),
					(int) ((points.get(points.size() - 1).y - start_y) * zoomFactor_y + edgeCorrection));
		}
	}

	private void drawPolygon(Graphics g, List<Point> points, Color color) {
		g.setColor(color);

		int[] xPoints = points.stream().map(p -> ((p.x - start_x) * zoomFactor_x + edgeCorrection)).mapToInt(Double::intValue).toArray();
		int[] yPoints = points.stream().map(p -> ((p.y - start_y) * zoomFactor_y + edgeCorrection)).mapToInt(Double::intValue).toArray();

		g.fillPolygon(xPoints, yPoints, points.size());
	}

	public void zoomScope()
	{
		Rectangle BoundaryBox = room.getBoundary();
		start_x = BoundaryBox.getMinX();
		start_y = BoundaryBox.getMinY();
		
		zoomFactor_x = (size.getWidth() - 2 * edgeCorrection)  / BoundaryBox.getWidth();
		zoomFactor_y = (size.getHeight() - 2 * edgeCorrection) / BoundaryBox.getHeight();
		
		System.out.println(zoomFactor_x + " " + zoomFactor_y);
	}
	
	
	@Override
	public Dimension getPreferredSize() {
	      return size;
	   }
	

	@Override
	public void mouseClicked(MouseEvent me) {
		int x = (int)((me.getX() - edgeCorrection) / zoomFactor_x + start_x);
		int y = (int)((me.getY() - edgeCorrection) / zoomFactor_y + start_y);

		if (me.getButton() == MouseEvent.BUTTON1) {
			room.addPoint(x, y);
		} else if (me.getButton() == MouseEvent.BUTTON3) {
			lights.addLight(new Point(x, y));
		}
	}

	@Override
	public void mouseEntered(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}
}
