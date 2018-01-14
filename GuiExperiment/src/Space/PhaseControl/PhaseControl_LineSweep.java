package Space.PhaseControl;

import java.util.ArrayList;
import java.util.List;

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
	
	
	public List<Vertex> startVertices;
	public int startVerticesCounter = 0;
	
	
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
	public void ComputeStartPoints() {
		List<Vertex> vertices = (this.room.getFragments().get(0).getVertices());
		TreeNode_Vertex startNodes = null;
		
		Vertex sourceVertex = vertices.get(0);
		Vertex currentVertex = sourceVertex;
		do {
			PointType pointType = Utilities.computePointType(currentVertex);
			if (pointType == PointType.STARTVERTEX) {
				if (startNodes == null)
					startNodes = new TreeNode_Vertex(currentVertex, false);
				else
					startNodes.add(currentVertex);
			}
			currentVertex = currentVertex.getNext();
			
		} while (currentVertex != sourceVertex);
		
		this.startVertices = startNodes.getVertices();
		
		this.onUpdate();
	}
	
	public void StartSweepStepWise() {
		this.status = new TreeNode_SweepDomain_Root();
		this.startVerticesCounter = 0;
		this.yLine = Double.MAX_VALUE;
		
		System.out.println("Sweep ready");
		
	}
	
	/**
	 * Jump to next SweepAction
	 */
	public void sweepNextPoint() {
		if (this.startVerticesCounter < this.startVertices.size()) {

			Vertex topAddVertex = this.startVertices.get(this.startVerticesCounter);
			Vertex topDomainVertex = this.status.firstVerticalVertex;
		
			if (topDomainVertex != null && Utilities.isBelow(topAddVertex, topDomainVertex)) {
				this.sweepProcessDomain(topDomainVertex);
			} else {
				System.out.println("NextPointinAdd");
				this.sweepProcessAdd(topAddVertex);
			}
		} else {
			Vertex topDomainVertex = this.status.firstVerticalVertex;
			if (topDomainVertex != null)
				this.sweepProcessDomain(topDomainVertex);
			else
				System.out.println("Looping is complete");
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
	private void sweepProcessAdd(Vertex vertex) {
		SweepDomain newSweepDomain = new SweepDomain(vertex);
		
		this.status.add(newSweepDomain);
		System.out.println(newSweepDomain);
		this.startVerticesCounter++;
		this.yLine = vertex.getY();
	}

}
