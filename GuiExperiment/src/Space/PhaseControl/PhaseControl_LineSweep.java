package Space.PhaseControl;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import Space.PointType;
import Space.Room;
import Space.Segment;
import Space.SweepDomain;
import Space.Utilities;
import Space.Vertex;

public class PhaseControl_LineSweep  extends PhaseControl{

	public Room room;
	public double yLine = 0;
	
	public TreeNode_SweepDomain status;
	
	
	public Stack<Vertex> startVertices;
	public Stack<Vertex> splitVertices;
	
	
	public PhaseControl_LineSweep(Room Room) {
		this.room = Room;
		
	}
	
	@Override
	public boolean canGoEnterNextPhase() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Put all startpoints in a tree
	 */
	public void computePointTypes() {
		List<Vertex> vertices = (this.room.getFragments().get(0).getVertices());
		TreeNode_Vertex startNodes = new TreeNode_Vertex(null, false);
		TreeNode_Vertex splitNodes = new TreeNode_Vertex(null, false);
		
		Vertex sourceVertex = vertices.get(0);
		Vertex currentVertex = sourceVertex;
		do {
			PointType pointType = Utilities.computePointType(currentVertex);
			if (pointType == PointType.STARTVERTEX) {
				startNodes.add(currentVertex);
			} else if (pointType == PointType.SPLITVERTEX) {
				splitNodes.add(currentVertex);
			}
			
			currentVertex = currentVertex.getNext();
			
		} while (currentVertex != sourceVertex);
		
		this.startVertices = startNodes.getVertices();
		this.splitVertices = splitNodes.getVertices();
		
		this.onUpdate();
	}
	
	public void StartSweepStepWise() {
		this.status = new TreeNode_SweepDomain_Root();
		this.yLine = Double.MAX_VALUE;
		
		System.out.println("Sweep ready");
		
	}
	
	/**
	 * Jump to next SweepAction
	 */
	public void sweepNextPoint() {

		Vertex topDomainVertex = null;
		Vertex topAddVertex = null;
		Vertex topSplitVertex = null;
		
		topDomainVertex = this.status.firstVerticalVertex;
		if (!this.startVertices.isEmpty())
			topAddVertex = this.startVertices.peek();
		if (!this.splitVertices.isEmpty())
			topSplitVertex = this.splitVertices.peek();
		
		if (Utilities.isBelow(topAddVertex, topDomainVertex)) {			
			if (Utilities.isBelow(topSplitVertex, topDomainVertex)) {	
				if (topDomainVertex != null)
					this.sweepProcessDomain(topDomainVertex);
				else
					System.out.println("Sweep complete");
			} else {
				this.sweepProcessSplit();
			}	
			
		} else {
			if (Utilities.isBelow(topSplitVertex, topAddVertex)) {		
				this.sweepProcessAdd();
			} else {
				this.sweepProcessSplit();
			}	
		}

		this.onUpdate();
		
		
	}
	
	/**
	 * Trigger the topmost action in the status
	 * @param topDomainVertex
	 */
	private void sweepProcessDomain(Vertex topDomainVertex) {
		// Domain has a higher point
		System.out.println("NextPointInDomain");
		this.status.updateNextDomain();
		this.yLine = topDomainVertex.getY();
	}
	
	/**
	 * Add a new DomainSweep to the status
	 * @param vertex
	 */
	private void sweepProcessAdd() {
		Vertex vertex = this.startVertices.pop();
		SweepDomain newSweepDomain = new SweepDomain(vertex);
		
		this.status.add(newSweepDomain);
		System.out.println(newSweepDomain);
		this.yLine = vertex.getY();
	}
	
	private void sweepProcessSplit() {
		Vertex vertex = this.splitVertices.pop();
		this.yLine = vertex.getY();
		this.status.splitDomain(vertex);
	}
	


}
