package Space;

import java.awt.geom.Point2D;

public class Segment {
	public Vertex startVertex;
	public Vertex endVertex;
	
	public Segment(Vertex start, Vertex end) {
		this.startVertex = start;
		this.endVertex = end;
	}

	public Vertex getHighestValue() {
		if (Utilities.isBelow(startVertex, endVertex))
			return endVertex;
		return startVertex;
	}
	public Vertex getLowestValue() {
		if (Utilities.isBelow(startVertex, endVertex))
			return startVertex;
		return endVertex;
	}
	
	public String toString() {
		return "From {" + startVertex.x + "," + startVertex.y + "} to {" + endVertex.x + "," + endVertex.y + "}";
	}
	
	public Point2D getIntersectionPoint(Segment lineSegment) {
		//TODO: implementation
		return null;
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
			return vertexHigh;
		
		double relativeY = (y - vertexLow.y) / (vertexHigh.y - vertexLow.y);
		double x = vertexLow.x + relativeY * (vertexHigh.x - vertexLow.x);
		return new PointDouble(x, y);
	}
	
	public boolean isVertical() {
			return startVertex.x == endVertex.x;
	}

	public Vertex getStartVertex() {
		return startVertex;
	}

	public Vertex getEndVertex() {
		return endVertex;
	}
}
