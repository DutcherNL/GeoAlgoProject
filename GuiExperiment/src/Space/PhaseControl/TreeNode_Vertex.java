package Space.PhaseControl;

import java.util.ArrayList;
import java.util.List;

import Space.Vertex;

public class TreeNode_Vertex {
	public Vertex ownContent;
	public TreeNode_Vertex leftNode;
	public TreeNode_Vertex rightNode;
	protected int encapsuledNodes;
	
	public TreeNode_Vertex(Vertex content) {
		this.ownContent = content;
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
	
	public List<Vertex> getVertices(){
		return getVertices(new ArrayList<Vertex>());
	}
	
	public List<Vertex> getVertices(List<Vertex> Vertices){
		if (rightNode != null)
			this.rightNode.getVertices(Vertices);
		Vertices.add(this.ownContent);
		if (leftNode != null)
			this.leftNode.getVertices(Vertices);
		
		return Vertices;
	}
	
	/**
	 * Adds a new vertex to the tree
	 * @param NewVertex
	 * @return The position on which it is placed
	 */
	public int add(Vertex NewVertex) {
		if (this.ownContent == null) {
			this.ownContent = NewVertex;
			return getLocalPosition();
		}
		
		this.encapsuledNodes++;
		
		if (Space.Utilities.isBelow(NewVertex, this.ownContent)) {
			if (rightNode != null) {
				return this.rightNode.add(NewVertex) + getLocalPosition();
			}
			else {
				rightNode = new TreeNode_Vertex(NewVertex); 
				return getLocalPosition() + 1;
			}
		} else {
			if (leftNode != null) {
				return this.leftNode.add(NewVertex);
			} else {
				leftNode = new TreeNode_Vertex(NewVertex);
				return 0;
			}
		}
		
		
	}

	public void remove(Vertex removedVertex) {
		if (this.ownContent == removedVertex) {
			this.remove();
			return;
		}
		
		
		if (Space.Utilities.isBelow(removedVertex, this.ownContent)) {
			if (rightNode != null)
				this.rightNode.add(removedVertex);
			else
				rightNode = new TreeNode_Vertex(removedVertex);
		} else {
			if (leftNode != null)
				this.leftNode.add(removedVertex);
			else
				leftNode = new TreeNode_Vertex(removedVertex);
		}
		
		encapsuledNodes--;
	}
	
	protected void remove() {
		
	}
	
	public Vertex get(int i) {
		if (leftNode != null) {
			if (leftNode.encapsuledNodes < i) {
				return leftNode.get(i);
			} else {
				i -= leftNode.encapsuledNodes;
			}
		}
		if (i == 0) {
			return this.ownContent;
		} else {
			i -= 1;
		}
		if (rightNode != null) {
			return rightNode.get(i);
		}
		
		System.out.println("You should not have reached here");
		return null;
	}


	protected int getLocalPosition() {
		if (this.leftNode != null) {
			return this.leftNode.encapsuledNodes;
		} else
			return 0;
	}
}
