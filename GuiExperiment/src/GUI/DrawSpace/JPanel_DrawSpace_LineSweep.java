package GUI.DrawSpace;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import Space.Lights;
import Space.PointDouble;
import Space.RoomFragment;
import Tree_Sweep.SweepDomain;
import Space.Vertex;
import Space.PhaseControl.PhaseControl_LineSweep;

public class JPanel_DrawSpace_LineSweep extends JPanel_DrawSpace{
	
	public final static Color POINT_COLOR_START = new Color(120, 53, 53);
	public final static Color POINT_COLOR_START_ALT = new Color(120, 53, 53, 100);
	public final static Color POINT_COLOR_INTERSECT = new Color(0, 200, 100);
	public final static Color POINT_COLOR_SPLIT = new Color(120, 53, 120);
	public final static Color POINT_COLOR_SPLIT_ALT = new Color(120,53,120, 100);
	public final static Color POINT_COLOR_DOMAIN = new Color(53, 120, 53);
	public final static Color COLOR_RESULT = new Color(0, 200, 200);
	public final static Color EDGE_COLOR_ROOM = new Color(53, 53, 53);
	public final static Color EDGE_COLOR_LIGHTS = new Color(90, 90, 47);
	public final static Color YLINE_COLOR = new Color(53, 53, 53);
	public final static Color EDGE_ERROR_COLOR = Color.RED;
	
	private Point2D mouseCoordinate;
	private boolean zoomIn;
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
		
		if (this.roomSweeper.visualizeShape) {
			this.displayResult(g);
		} else {
			this.displayProgress(g);
		}
		
	}
	
	private void displayResult(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(8));
		this.drawLines(g, COLOR_RESULT, this.roomSweeper.Shape);
	}

	private void displayProgress(Graphics g) {
		if (this.roomSweeper.status != null) {
			Vertex nextVertexFromDomain = this.roomSweeper.status.getHighestVertex();
			
			
			List<SweepDomain> Domains = this.roomSweeper.status.exportToList();
			
			SweepDomain workDomain;
			Polygon P;
			
			for (int i = 0; i < Domains.size(); i++) {
				workDomain = Domains.get(i);
				
				g.setColor(workDomain.color);
				
				P = new Polygon();
				P.addPoint(
						(int)((workDomain.leftSegment.startPoint.x - start_x) * zoomFactor_x + edgeCorrection),
						(int)((workDomain.leftSegment.startPoint.y - start_y) * zoomFactor_y + edgeCorrection));
				P.addPoint(
						(int)((workDomain.leftSegment.endPoint.x - start_x) * zoomFactor_x + edgeCorrection),
						(int)((workDomain.leftSegment.endPoint.y - start_y) * zoomFactor_y + edgeCorrection));
				P.addPoint(
						(int)((workDomain.rightSegment.startPoint.x - start_x) * zoomFactor_x + edgeCorrection),
						(int)((workDomain.rightSegment.startPoint.y - start_y) * zoomFactor_y + edgeCorrection));
				P.addPoint(
						(int)((workDomain.rightSegment.endPoint.x - start_x) * zoomFactor_x + edgeCorrection),
						(int)((workDomain.rightSegment.endPoint.y - start_y) * zoomFactor_y + edgeCorrection));
				g.fillPolygon(P);
				
				g.setColor(Color.black);
				
				if (nextVertexFromDomain != null) {
					g.setColor(POINT_COLOR_DOMAIN);
					this.drawVertix(g, nextVertexFromDomain);
				}
			}			
		}
		
		if (roomSweeper.useRoomFragments || roomSweeper.mainForm == null) {
			// Draw all other fragment areas
			for(RoomFragment fragment : roomSweeper.room.getFragments()) {
				this.drawLines(g, fragment.getVertices(), EDGE_COLOR_ROOM);
			}
		}
		if (!roomSweeper.useRoomFragments || roomSweeper.mainForm == null) {
			for(List<Vertex> visibleRegion : roomSweeper.lights.getVisibilityRegions()) {
				this.drawLines(g, visibleRegion, EDGE_COLOR_ROOM);
			}
		}
		
		g.setColor(YLINE_COLOR);
		g.drawLine(
				(int)(edgeCorrection),
				(int)((roomSweeper.yLine - start_y) * zoomFactor_y + edgeCorrection),
				(int)(size.getWidth() -  edgeCorrection),
				(int)((roomSweeper.yLine - start_y) * zoomFactor_y + edgeCorrection)
				);
		
		if (this.roomSweeper.sideForm != null) {
			this.drawVertices(g, this.roomSweeper.sideForm.getStartVertices(), POINT_COLOR_START_ALT);
			this.drawVertices(g, this.roomSweeper.sideForm.getSplitVertices(), POINT_COLOR_SPLIT_ALT);
		}
		
		g.setColor(POINT_COLOR_INTERSECT);
		if (this.roomSweeper.intersections != null) {
			for(Vertex v : this.roomSweeper.intersections) {
				g.drawLine(
						(int)((v.getPrevious().getX() - start_x) * zoomFactor_x + edgeCorrection),
						(int)((v.getPrevious().getY() - start_y) * zoomFactor_y + edgeCorrection),
						(int)((v.getX() - start_x) * zoomFactor_x + edgeCorrection),
						(int)((v.getY() - start_y) * zoomFactor_y + edgeCorrection)
						);
				this.drawVertix(g, v);
				g.drawLine(
						(int)((v.getNext().getX() - start_x) * zoomFactor_x + edgeCorrection),
						(int)((v.getNext().getY() - start_y) * zoomFactor_y + edgeCorrection),
						(int)((v.getX() - start_x) * zoomFactor_x + edgeCorrection),
						(int)((v.getY() - start_y) * zoomFactor_y + edgeCorrection)
						);
			}
		}

		if (this.roomSweeper.mainForm != null) {
			this.drawVertices(g, this.roomSweeper.mainForm.getStartVertices(), POINT_COLOR_START);
			this.drawVertices(g, this.roomSweeper.mainForm.getSplitVertices(), POINT_COLOR_SPLIT);
		}
	}

	@Override
	public void mousePressed(MouseEvent me) {
		int x = (int)((me.getX() - edgeCorrection) / zoomFactor_x + start_x);
		int y = (int)((me.getY() - edgeCorrection) / zoomFactor_y + start_y);

		switch(me.getButton()) {
			case MouseEvent.BUTTON1:
				
				this.mouseCoordinate = new PointDouble(x, y);
				this.zoomIn = true;
				break;
			case MouseEvent.BUTTON2:
				this.resetZoom();
				break;
			case MouseEvent.BUTTON3:
				this.mouseCoordinate = new PointDouble(x, y);
				this.zoomIn = false;
				break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		if (this.mouseCoordinate != null) {
		
			if (this.zoomIn) {
				zoomFactor_x = this.zoomFactor_x * 2;
				zoomFactor_y = this.zoomFactor_y * 2;
			} else {
				zoomFactor_x = this.zoomFactor_x / 2;
				zoomFactor_y = this.zoomFactor_y / 2;
			}
			
			this.start_x = this.mouseCoordinate.getX() - (size.getWidth() - 2 * edgeCorrection) / (2 * this.zoomFactor_x);
			this.start_y = this.mouseCoordinate.getY() - (size.getHeight() - 2 * edgeCorrection) / (2 * this.zoomFactor_y);
			
			this.mouseCoordinate = null;
			
			this.repaint();
		}
	}

}
