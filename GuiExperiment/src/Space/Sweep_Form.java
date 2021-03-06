package Space;

import java.util.List;
import java.util.Stack;

import Tree_Sweep.TreeNode_Vertex;
import Tree_Sweep.TreeNode_VertexRoot;

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
	
	public void removeVertex(Vertex v) {
		PointType pointType = Utilities.computePointType(v);
		if (pointType == PointType.STARTVERTEX) {
			System.out.println("Remove start vertex");
			this.startNodes.remove(v);
		} else if (pointType == PointType.SPLITVERTEX) {
			System.out.println("Remove split vertex");
			this.splitNodes.remove(v);
		}
	}

	public List<Vertex> getStartVertices(){
		return startNodes.getVertices();
	}
	public List<Vertex> getSplitVertices(){
		return splitNodes.getVertices();
	}
	
	public Vertex getStartVertex(int i) {
		return startNodes.get(i);
	}
	public Vertex getSplitVertex(int i) {
		return splitNodes.get(i);
	}
}
