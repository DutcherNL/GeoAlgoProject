package Space;

import java.awt.Rectangle;
import java.awt.geom.Point2D;

public class LineSegment {
	public Point2D startPoint;
	public Point2D endPoint;
	
	public LineSegment(Point2D a, Point2D b) {
		this.startPoint = a;
		this.endPoint  = b;
	}
	
	public Rectangle getBoundingBox() {
		return new Rectangle(
				(int)Math.min(startPoint.getX(), endPoint.getX()),
				(int)Math.min(startPoint.getY(), endPoint.getY()),
				(int)Math.max(startPoint.getX(), endPoint.getX()),
				(int)Math.max(startPoint.getY(), endPoint.getY()));
	}
}
