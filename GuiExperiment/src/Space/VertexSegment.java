package Space;

import java.awt.geom.Point2D;

public class VertexSegment extends LineSegment{
	public Vertex startPoint;
	public Vertex endPoint;
	
	public VertexSegment(Vertex start, Vertex end) {
		super (start, end);
		this.startPoint = start;
		this.endPoint = end;
	}
	
	public Vertex getHighestValue() {
		if (Utilities.isBelow(startPoint, endPoint))
			return endPoint;
		return startPoint;
	}
	public Vertex getLowestValue() {
		if (Utilities.isBelow(startPoint, endPoint))
			return startPoint;
		return endPoint;
	}
	
	public String toString() {
		return "From {" + startPoint.x + "," + startPoint.y + "} to {" + endPoint.x + "," + endPoint.y + "}";
	}
	
	/**
	 * Computes the location of this segment with horizontal line at heigh y
	 * @param y The height of the line
	 * @return The intersection point
	 */
	public Point2D getHorizontalIntersection(double y) {
		Vertex vertexHigh = this.getHighestValue();
		Vertex vertexLow = this.getLowestValue();
		
			// return nothing if request is out of range
		if (!(vertexHigh.y >= y && y >= vertexLow.y))
		{
			System.out.println("Y out of scope "+vertexHigh.y + " - " + vertexLow.y + " on: "+y);
			return null;
		}
		
		// If the segment is itself horizontal,
		if (vertexHigh.y == vertexLow.y) 
			return vertexLow;
		
		double relativeY = (y - vertexLow.y) / (vertexHigh.y - vertexLow.y);
		double x = vertexLow.x + relativeY * (vertexHigh.x - vertexLow.x);
		return new PointDouble(x, y);
	}
	
	public Point2D getIntersectionPoint(VertexSegment lineSegment) {
		return LineIntersect.getIntersectionPoint(this, lineSegment);
		
		
	}
	

	
}
