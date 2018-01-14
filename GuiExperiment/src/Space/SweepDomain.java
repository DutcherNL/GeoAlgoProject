package Space;

public class SweepDomain {
	public Segment leftSegment;
	public Segment rightSegment;

	public SweepDomain(Segment LeftSegment, Segment RightSegment) {
		this.leftSegment = LeftSegment;
		this.rightSegment = RightSegment;
	}
	
	public SweepDomain(Vertex centerVertex) {
		this.leftSegment = new Segment(centerVertex, centerVertex.getPrevious());
		this.rightSegment = new Segment(centerVertex, centerVertex.getNext());
	}
	
	public Vertex getHighestPoint() {
		if (Utilities.isBelow(
				this.leftSegment.getHighestValue(),
				this.rightSegment.getHighestValue()))
			return this.rightSegment.getHighestValue();
		return this.leftSegment.getHighestValue();
	}
	
	
	public String toString() {
		return "Left side: " +leftSegment.toString() + " | " + rightSegment.toString();
	}
}
