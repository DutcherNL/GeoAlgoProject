package GUI.DrawSpace;

import Space.Vertex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.List;

public class JPanel_DrawSpace extends JPanel implements MouseListener{

	protected Dimension size = new Dimension(800,800);
	protected int pointWidth = 10;
	protected double zoomFactor_x = 1;
	protected double zoomFactor_y = -1;
	protected double edgeCorrection = 20;
	protected double start_x = 0;
	protected double start_y = size.height - edgeCorrection;

	/**
	 * Draw points on the screen for a given list of points
	 * @param g The graphics object
	 * @param points The to be drawn points
	 * @param color The color of the points
	 */
	protected void drawVertices(Graphics g, List<Vertex> points, Color color) {
		if (points == null) return;
		
		g.setColor(color);
		for(int i=0; i<points.size();i++) {		
			this.drawVertix(g, points.get(i));
		}
	}
	
	protected void drawVertix(Graphics g, Vertex vertex) {
		if (vertex == null)
			return;
		
		g.fillOval(
				(int)((vertex.x - start_x) * zoomFactor_x + edgeCorrection - pointWidth / 2),
				(int)((vertex.y - start_y) * zoomFactor_y + edgeCorrection - pointWidth / 2),
				pointWidth,
				pointWidth);
	}

	/**
	 * Draw points on the screen for a given list of points
	 * @param g The graphics object
	 * @param points The to be drawn points
	 * @param color The color of the points
	 */
	protected void drawPoints(Graphics g, List<Point2D> points, Color color) {
		if (points == null) return;
		
		g.setColor(color);
		for(int i=0; i<points.size();i++) {
			
			g.fillOval(
					(int)((points.get(i).getX() - start_x) * zoomFactor_x + edgeCorrection - pointWidth / 2),
					(int)((points.get(i).getY() - start_y) * zoomFactor_y + edgeCorrection - pointWidth / 2),
					pointWidth,
					pointWidth);
		}
	}
	
	/**
	 * Simplified version of the line draw program
	 * @param g The graphics object
	 * @param points the lines connecting all the points
	 * @param color The color of the lines
	 */
	protected void drawLines(Graphics g, List<Vertex> points, Color color) {
		drawLines(g, points, color, color);
	}
	/**
	 * Advanced version of line drawer where the last line (looping line) is given a different color
	 * @param g The graphics object
	 * @param points The points connecting the lines
	 * @param color The color of the lines
	 * @param connectColor The color of the last line
	 */
	protected void drawLines(Graphics g, List<Vertex> points, Color color, Color connectColor) {
		g.setColor(color);

		if (points.size() > 0) {
			g.drawString(0 + "(" + ((int) points.get(0).x) + "," + ((int) points.get(0).y) + ")",
					(int)((points.get(0).x - start_x) * zoomFactor_x + edgeCorrection),
					(int)((points.get(0).y - start_y) * zoomFactor_y + edgeCorrection) - 5
			);
		}

		for(int i=0; i + 1 < points.size();i++) {
			g.drawLine(
					(int)((points.get(i).x - start_x) * zoomFactor_x + edgeCorrection),
					(int)((points.get(i).y - start_y) * zoomFactor_y + edgeCorrection),
					(int)((points.get(i+1).x - start_x) * zoomFactor_x + edgeCorrection),
					(int)((points.get(i+1).y - start_y) * zoomFactor_y + edgeCorrection)
					);

			g.drawString(i+1 + "(" + ((int) points.get(i+1).x) + "," + ((int) points.get(i+1).y) + ")",
					(int)((points.get(i+1).x - start_x) * zoomFactor_x + edgeCorrection),
					(int)((points.get(i+1).y - start_y) * zoomFactor_y + edgeCorrection) - 5
			);
		}
		g.setColor(connectColor);
		
		if (points.size() > 1) {
			g.drawLine(
					(int) ((points.get(0).x - start_x) * zoomFactor_x + edgeCorrection),
					(int) ((points.get(0).y - start_y) * zoomFactor_y + edgeCorrection),
					(int) ((points.get(points.size() - 1).x - start_x) * zoomFactor_x + edgeCorrection),
					(int) ((points.get(points.size() - 1).y - start_y) * zoomFactor_y + edgeCorrection));
		}
	}
	
	/**
	 * Draws a polygon from the given points
	 * @param g The graphics object
	 * @param points The edge points
	 * @param color The color of the polygon
	 */
	protected void drawPolygon(Graphics g, List<Vertex> points, Color color) {
		g.setColor(color);

		int[] xVertices = points.stream().map(p -> ((p.x - start_x) * zoomFactor_x + edgeCorrection)).mapToInt(Double::intValue).toArray();
		int[] yVertices = points.stream().map(p -> ((p.y - start_y) * zoomFactor_y + edgeCorrection)).mapToInt(Double::intValue).toArray();

		g.fillPolygon(xVertices, yVertices, points.size());
	}
	
	/**
	 * Focuses the window to include the entire room figure
	 */
	public void zoomScope(Rectangle BoundaryBox)
	{
		start_x = BoundaryBox.getMinX();
		start_y = BoundaryBox.getMinY();
		
		zoomFactor_x = (size.getWidth() - 2 * edgeCorrection)  / BoundaryBox.getWidth();
		zoomFactor_y = (size.getHeight() - 2 * edgeCorrection) / BoundaryBox.getHeight();
		
		this.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public Dimension getPreferredSize() {
	      return size;
}

}
