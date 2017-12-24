package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JPanel;

import Space.Room;
import Space.UpdateEvent;

public class Gui_DrawSpace  extends JPanel implements MouseListener{
	
	Dimension size = new Dimension(400,400);
	Room room;
	int pointwidth = 10;
	double start_x = 0;
	double start_y = 0;
	double zoomFactor_x = 1;
	double zoomFactor_y = 1;
	double edgeCorrecion = 20;
	
	public Gui_DrawSpace(Room Room) {
		room = Room;
		//ZoomScope();
		this.addMouseListener(this);
		room.addListener(new UpdateEvent() {
			public void onUpdate() {
				repaint();
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{		super.paintComponent(g);
	
		List<Point> Points = room.getPoints();
	
		g.setColor(Color.cyan);
		g.fillRect(0, 0, size.width, size.height);
		
		this.drawLines(g, Points);
		this.drawPoints(g, Points);
		
		
	
	}
	
	private void drawPoints(Graphics g, List<Point> points) {
		g.setColor(Color.black);
		for(int i=0; i<points.size();i++)
		{
			g.fillOval(
					(int)((points.get(i).x - start_x) * zoomFactor_x + edgeCorrecion - pointwidth / 2),
					(int)((points.get(i).y - start_y) * zoomFactor_y + edgeCorrecion - pointwidth / 2),
					pointwidth,
					pointwidth);
		}
	}
	
	private void drawLines(Graphics g, List<Point> points) {
		g.setColor(Color.DARK_GRAY);
		for(int i=0; i + 1<points.size();i++)
		{
			g.drawLine(
					(int)((points.get(i).x - start_x) * zoomFactor_x + edgeCorrecion),
					(int)((points.get(i).y - start_y) * zoomFactor_y + edgeCorrecion),
					(int)((points.get(i+1).x - start_x) * zoomFactor_x + edgeCorrecion),
					(int)((points.get(i+1).y - start_y) * zoomFactor_y + edgeCorrecion)
					);
		}
		
		g.setColor(Color.LIGHT_GRAY);
		if (!room.canClose())
		{
			g.setColor(Color.red);
		}
		
		if (points.size() > 1) {
			g.drawLine(
					(int) ((points.get(0).x - start_x) * zoomFactor_x + edgeCorrecion),
					(int) ((points.get(0).y - start_y) * zoomFactor_y + edgeCorrecion),
					(int) ((points.get(points.size() - 1).x - start_x) * zoomFactor_x + edgeCorrecion),
					(int) ((points.get(points.size() - 1).y - start_y) * zoomFactor_y + edgeCorrecion));
		}
	}
	
	public void ZoomScope()
	{
		Rectangle BoundaryBox = room.getBoundary();
		start_x = BoundaryBox.getMinX();
		start_y = BoundaryBox.getMinY();
		
		zoomFactor_x = (size.getWidth() - 2 * edgeCorrecion)  / BoundaryBox.getWidth();
		zoomFactor_y = (size.getHeight() - 2 * edgeCorrecion) / BoundaryBox.getHeight();
		
		System.out.println(zoomFactor_x + " " + zoomFactor_y);
	}
	
	
	@Override
	public Dimension getPreferredSize() {
	      return size;
	   }
	

	@Override
	public void mouseClicked(MouseEvent me) {
		room.AddPoint(
				(int)((me.getX() - edgeCorrecion) / zoomFactor_x + start_x),
				(int)((me.getY() - edgeCorrecion) / zoomFactor_y + start_y)
		);
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
