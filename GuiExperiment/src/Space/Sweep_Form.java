package Space;

import java.util.List;
import java.util.Stack;

import Space.PhaseControl.TreeNode_Vertex;

public class Sweep_Form {
	public boolean isMain;
	
	private Vertex sourceVertex;
	
	private TreeNode_Vertex startNodes;
	private TreeNode_Vertex splitNodes;
	
	private int startCounter;
	private int splitCounter;
	
	public Sweep_Form (Vertex vertex, boolean IsMain) {
		this.sourceVertex = vertex;
		this.isMain = IsMain;
		this.computePointTypes();
	}
	
	
	/**
	 * Put all startpoints in a tree
	 */
	public void computePointTypes() {
		startNodes = new TreeNode_Vertex(null);
		splitNodes = new TreeNode_Vertex(null);
		
		Vertex currentVertex = this.sourceVertex;
		
		do {
			PointType pointType = Utilities.computePointType(currentVertex);
			if (pointType == PointType.STARTVERTEX) {
				startNodes.add(currentVertex);
			} else if (pointType == PointType.SPLITVERTEX) {
				splitNodes.add(currentVertex);
			}
			
			currentVertex = currentVertex.getNext();
			
		} while (currentVertex != this.sourceVertex);
	}

	public void reset() {
		this.startCounter = 0;
		this.splitCounter = 0;
	}
	
	public void addStartVertex(Vertex v) {
		//TODO: insert code
	}
	public void addSplitVertex(Vertex v) {
		
	}
	
	public void removeStartVertex() {
		
	}
	public void removeSplitVertex() {
		
	}

	public List<Vertex> getStartVertices(){
		return startNodes.getVertices();
	}
	public List<Vertex> getSplitVertices(){
		return splitNodes.getVertices();
	}
	
	public Vertex getNextStartVertex() {
		this.startCounter++;
		return this.startNodes.get(this.startCounter - 1);
	}
	public Vertex getNextSplitVertex() {
		this.splitCounter++;
		return this.splitNodes.get(this.splitCounter - 1);
	}
}
