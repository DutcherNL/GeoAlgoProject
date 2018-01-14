package Space;

import java.awt.Color;
import java.awt.geom.Point2D;

public class SweepDomain {
	public Segment leftSegment;
	public Segment rightSegment;
	
	public Color displaycolor;

	public SweepDomain(Segment LeftSegment, Segment RightSegment) {
		this.leftSegment = LeftSegment;
		this.rightSegment = RightSegment;
		
		java.util.Random R = new java.util.Random();
		this.displaycolor = Color.getHSBColor(R.nextFloat(), 0.5f + 0.2f*R.nextFloat(), 0.5f);
	}
	
	public SweepDomain(Vertex centerVertex) {
		this.leftSegment = new Segment(centerVertex, centerVertex.getPrevious());
		this.rightSegment = new Segment(centerVertex, centerVertex.getNext());
		
		java.util.Random R = new java.util.Random();
		this.displaycolor = Color.getHSBColor(R.nextFloat(), 0.5f + 0.2f*R.nextFloat(), 0.5f);
	}
	
	public Vertex getHighestEndPoint() {
		if (Utilities.isBelow(
				this.leftSegment.getLowestValue(),
				this.rightSegment.getLowestValue()))
			return this.rightSegment.getLowestValue();
		return this.leftSegment.getLowestValue();
	}
	
	
	public String toString() {
		return "Left side: " +leftSegment.toString() + " | " + rightSegment.toString();
	}
	
	public double leftXAtY(double y) {
		return this.leftSegment.getHorizontalIntersection(y).getX();
	}
	public double rightXAtY(double y) {
		return this.rightSegment.getHorizontalIntersection(y).getX();
	}
	
	public boolean isWithin(Point2D Point) {
		Point2D leftPoint = this.leftSegment.getHorizontalIntersection(Point.getY());
		Point2D rightPoint = this.rightSegment.getHorizontalIntersection(Point.getY());
		return (leftPoint.getX() < Point.getX() && Point.getX() < rightPoint.getX());
	}
}
