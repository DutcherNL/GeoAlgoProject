package Tree_Sweep;

import Space.LineIntersect;
import Space.Utilities;
import Space.Vertex;
import Space.VertexSegment;

public class TreeNode_SweepRight extends TreeNode_Sweep{

	public TreeNode_SweepRight(VertexSegment segment, boolean isMain, TreeNode_Sweep parent) {
		super(segment, isMain, false, parent);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	protected void update() {
		
		if (Utilities.isBelow(this.segment.startPoint, this.segment.startPoint.getPrevious())) {
			// A merge occurs
			TreeNode_Sweep mergeNode = this.getNextNode(this.type);
			
			this.checkIntersectLeft(this.segment.startPoint.y);
			mergeNode.checkIntersectLeft(this.segment.startPoint.y);
			
			this.opposite.opposite = mergeNode.opposite;
			this.opposite.opposite.opposite = this.opposite; // yes, this could be better, but this is just cool.
			this.opposite.opposite.data = this.opposite.data;
			
			mergeNode.remove();
			this.remove();
			this.parentNode.calcNodeVariables();
			mergeNode.parentNode.calcNodeVariables();
			
		} else {
			this.checkIntersectLeft(this.segment.startPoint.y);
			this.segment = new VertexSegment(this.segment.startPoint.getPrevious(), this.segment.startPoint);
			this.calcNodeVariables();
	}
	}

	
	@Override
	protected void computeIntersection(TreeNode_Sweep interSegment) {
		Vertex intersection =  new Vertex(
				LineIntersect.getIntersectionPoint(
						this.segment,
						interSegment.segment));
		
		// Shortcut for a cleaner solution in case there is a horizontal line intersection
		if (intersection == this.segment.endPoint) {
			if (this.isConflicted) {
				// Go the normal route
			} else {
				interSegment.segment.startPoint.setNext(this.segment.endPoint);
				interSegment.changeConflictedState();
				this.changeConflictedState();
				this.broadcastIntersection(intersection);
				return;
			}			
		}
		
		if (interSegment.isLeft) {
			if (this.isConflicted) {
				// intersegment passes from left to right
				intersection.setPrevious(this.segment.startPoint);
				intersection.setNext(interSegment.segment.endPoint);				
			} else {
				// intersegment passes from right to left
				intersection.setPrevious(interSegment.segment.startPoint);
				intersection.setNext(this.segment.endPoint);
			}
			
			intersection.getPrevious().setNext(intersection);
			intersection.getNext().setPrevious(intersection);
			
			this.segment.endPoint = intersection;
			interSegment.segment.startPoint = intersection;
			
			interSegment.changeConflictedState();
			this.changeConflictedState();
			
		} else {
			if (this.isConflicted) {
				// intersegment passes from right to left
				intersection.setPrevious(this.segment.startPoint);
				intersection.setNext(interSegment.segment.endPoint);
			} else {
				// intersegment passes from left to right
				intersection.setPrevious(interSegment.segment.startPoint);
				intersection.setNext(this.segment.endPoint);
			}
			intersection.getPrevious().setNext(intersection);
			intersection.getNext().setPrevious(intersection);
			
			this.segment.endPoint = intersection;
			interSegment.segment.endPoint = intersection;
			
			interSegment.changeConflictedState();
			this.changeConflictedState();
		}

		this.broadcastIntersection(intersection);
		
	}
}
