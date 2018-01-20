package Space;

import java.util.List;
import java.util.Stack;

import Space.PhaseControl.TreeNode_Vertex;
import Space.PhaseControl.TreeNode_VertexRoot;

public class Sweep_Form {
	public boolean isMain;
	
	private Vertex sourceVertex;
	
	private TreeNode_VertexRoot startNodes;
	private TreeNode_VertexRoot splitNodes;
	
	public Sweep_Form (Vertex vertex, boolean IsMain) {
		this.sourceVertex = vertex;
		this.isMain = IsMain;
		this.computePointTypes();
	}
	
	
	/**
	 * Put all startpoints in a tree
	 */
	public void computePointTypes() {
		startNodes = new TreeNode_VertexRoot();
		splitNodes = new TreeNode_VertexRoot();
		
		Vertex currentVertex = this.sourceVertex;
		if (this.sourceVertex == null)
			return;
		
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
	
	public void addStartVertex(Vertex v) {
		startNodes.add(v);
	}
	public void addSplitVertex(Vertex v) {
		splitNodes.add(v);
	}
	
	public void removeStartVertex(Vertex v) {
		System.out.println("Remove STart ordered");
		this.startNodes.remove(v);
	}
	public void removeSplitVertex(Vertex v) {
		System.out.println("Remove Split ordered");
		this.splitNodes.remove(v);
	}

	public List<Vertex> getStartVertices(){
		return startNodes.getVertices();
	}
	public List<Vertex> getSplitVertices(){
		return splitNodes.getVertices();
	}
}
