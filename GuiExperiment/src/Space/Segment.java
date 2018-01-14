package Space;

public class Segment {
	public Vertex startVertex;
	public Vertex endVertex;
	
	public Segment(Vertex start, Vertex end) {
		this.startVertex = start;
		this.endVertex = end;
	}
	
	public Vertex getHighestValue() {
		if (Utilities.isBelow(startVertex, endVertex))
			return startVertex;
		return endVertex;
	}
	
	public String toString() {
		return "From {" + startVertex.x + "," + startVertex.y + "} to {" + endVertex.x + "," + endVertex.y + "}";
	}
}
