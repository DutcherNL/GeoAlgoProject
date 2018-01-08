package GUI.DrawSpace;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JPanel;

public class JPanel_DrawSpace extends JPanel implements MouseListener{

	protected Dimension size = new Dimension(400,400);
	protected int pointWidth = 10;
	protected double start_x = 0;
	protected double start_y = 0;
	protected double zoomFactor_x = 1;
	protected double zoomFactor_y = 1;
	protected double edgeCorrection = 20;
	
	
	public JPanel_DrawSpace() {
		this.addMouseListener(this);
	}
	
	/**
	 * Draw points on the screen for a given list of points
	 * @param g The graphics object
	 * @param points The to be drawn points
	 * @param color The color of the points
	 */
	protected void drawPoints(Graphics g, List<Point> points, Color color) {
		if (points == null)
			return;
		
		
		g.setColor(color);
		for(int i=0; i<points.size();i++) {
			g.fillOval(
					(int)((points.get(i).x - start_x) * zoomFactor_x + edgeCorrection - pointWidth / 2),
					(int)((points.get(i).y - start_y) * zoomFactor_y + edgeCorrection - pointWidth / 2),
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
	protected void drawLines(Graphics g, List<Point> points, Color color) {
		drawLines(g, points, color, color);
	}
	/**
	 * Advanced version of line drawer where the last line (looping line) is given a different color
	 * @param g The graphics object
	 * @param points The points connecting the lines
	 * @param color The color of the lines
	 * @param connectColor The color of the last line
	 */
	protected void drawLines(Graphics g, List<Point> points, Color color, Color connectColor) {
		g.setColor(color);

		for(int i=0; i + 1<points.size();i++) {
			g.drawLine(
					(int)((points.get(i).x - start_x) * zoomFactor_x + edgeCorrection),
					(int)((points.get(i).y - start_y) * zoomFactor_y + edgeCorrection),
					(int)((points.get(i+1).x - start_x) * zoomFactor_x + edgeCorrection),
					(int)((points.get(i+1).y - start_y) * zoomFactor_y + edgeCorrection)
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
	protected void drawPolygon(Graphics g, List<Point> points, Color color) {
		g.setColor(color);

		int[] xPoints = points.stream().map(p -> ((p.x - start_x) * zoomFactor_x + edgeCorrection)).mapToInt(Double::intValue).toArray();
		int[] yPoints = points.stream().map(p -> ((p.y - start_y) * zoomFactor_y + edgeCorrection)).mapToInt(Double::intValue).toArray();

		g.fillPolygon(xPoints, yPoints, points.size());
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
	public Dimension getPreferredSize() {
	      return size;
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

}
