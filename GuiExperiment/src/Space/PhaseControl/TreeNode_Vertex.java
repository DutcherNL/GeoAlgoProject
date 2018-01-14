package Space.PhaseControl;

import java.util.ArrayList;
import java.util.List;

import com.sun.webkit.Utilities;

import Space.Vertex;

public class TreeNode_Vertex {
	public Vertex ownContent;
	public TreeNode_Vertex leftNode;
	public TreeNode_Vertex rightNode;
	private boolean lowToHigh;
	
	public TreeNode_Vertex(Vertex content, boolean LowToHigh) {
		this.ownContent = content;
		this.lowToHigh = LowToHigh;
	}
	
	
	public Vertex getFirstContent() {
		if (leftNode == null)
			return ownContent;
		else
			return leftNode.getFirstContent();
	}
	public Vertex getLastContent() {
		if (rightNode == null)
			return ownContent;
		else
			return leftNode.getFirstContent();
	}
	
	private Vertex getPrevious() {
		if (leftNode == null)
			return null;
		return leftNode.getLastContent();
	}
	
	private Vertex getNext() {
		if (rightNode == null)
			return null;
		return rightNode.getLastContent();
	}
	
	public List<Vertex> getVertices(){
		return getVertices(new ArrayList<Vertex>());
	}
	
	public List<Vertex> getVertices(List<Vertex> Vertices){
		if (leftNode != null)
			this.leftNode.getVertices(Vertices);
		Vertices.add(this.ownContent);
		if (rightNode != null)
			this.rightNode.getVertices(Vertices);
		
		return Vertices;
	}
	
	/**
	 * Add a new vertex to the tree
	 * @param NewVertex
	 */
	public void add(Vertex NewVertex) {
		if (Space.Utilities.isBelow(NewVertex, this.ownContent)) {
			if (lowToHigh) {
				if (leftNode != null)
					this.leftNode.add(NewVertex);
				else
					leftNode = new TreeNode_Vertex(NewVertex, this.lowToHigh);
			} else {
				if (rightNode != null)
					this.rightNode.add(NewVertex);
				else
					rightNode = new TreeNode_Vertex(NewVertex, this.lowToHigh);
			}
		} else {
			if (lowToHigh) {
				if (rightNode != null)
					this.rightNode.add(NewVertex);
				else
					rightNode = new TreeNode_Vertex(NewVertex, this.lowToHigh);
			} else {
				if (leftNode != null)
					this.leftNode.add(NewVertex);
				else
					leftNode = new TreeNode_Vertex(NewVertex, this.lowToHigh);
			}
		}
	}
	
}
