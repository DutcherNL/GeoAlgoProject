package Tree_Sweep;

import Space.LineIntersect;
import Space.Vertex;
import Space.VertexSegment;

public class TreeNode_SweepLeft extends TreeNode_Sweep{

	public TreeNode_SweepLeft(VertexSegment segment, boolean isMain, TreeNode_Sweep parent) {
		super(segment, isMain, true, parent);
	}
	
	@Override
	protected void update() {
		if (this.segment.endPoint == this.opposite.segment.startPoint) {
				// End of the domain is reached
				this.checkIntersectLeft(this.segment.endPoint.y);
				this.opposite.checkIntersectLeft(this.segment.endPoint.y);
				this.opposite.remove();
				this.remove();
				this.parentNode.calcNodeVariables();
				this.opposite.parentNode.calcNodeVariables();
			} else {
				this.checkIntersectLeft(this.segment.endPoint.y);
				this.segment = new VertexSegment(this.segment.endPoint, this.segment.endPoint.getNext());
				this.calcNodeVariables();
		}
	}
	
	@Override
	protected void computeIntersection(TreeNode_Sweep interSegment) {
		Vertex intersection =  new Vertex(
				LineIntersect.getIntersectionPoint(
						this.segment,
						interSegment.segment));
		
		
		
		
		if (interSegment.isLeft) {
			if (this.isConflicted) {
				// intersegment passes from left to right
				intersection.setPrevious(interSegment.segment.startPoint);
				intersection.setNext(this.segment.endPoint);					
			} else {
				// intersegment passes from right to left
				intersection.setPrevious(this.segment.startPoint);
				intersection.setNext(interSegment.segment.endPoint);
			}
			
			intersection.getPrevious().setNext(intersection);
			intersection.getNext().setPrevious(intersection);
			
			this.segment.startPoint = intersection;
			interSegment.segment.startPoint = intersection;
			
			interSegment.changeConflictedState();
			this.changeConflictedState();
			
		} else {
			if (this.isConflicted) {
				// intersegment passes from left to right
				intersection.setPrevious(interSegment.segment.startPoint);
				intersection.setNext(this.segment.endPoint);
			} else {
				// intersegment passes from right to left
				intersection.setPrevious(this.segment.startPoint);
				intersection.setNext(interSegment.segment.endPoint);
			}
			
			intersection.getPrevious().setNext(intersection);
			intersection.getNext().setPrevious(intersection);
			
			this.segment.startPoint = intersection;
			interSegment.segment.endPoint = intersection;
			
			interSegment.changeConflictedState();
			this.changeConflictedState();
		}

		this.broadcastIntersection(intersection);
	}

}
