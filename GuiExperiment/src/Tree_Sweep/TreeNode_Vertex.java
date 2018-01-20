package Tree_Sweep;

import java.util.ArrayList;
import java.util.List;

import Space.Vertex;

public class TreeNode_Vertex {
	public Vertex ownContent;
	protected TreeNode_Vertex leftNode;
	protected TreeNode_Vertex rightNode;
	protected TreeNode_Vertex parentNode;
	protected int encapsuledNodes;
	
	public TreeNode_Vertex(Vertex content, TreeNode_Vertex parentNode) {
		this.ownContent = content;
		this.parentNode = parentNode;
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
	
	public List<Vertex> getVertices(List<Vertex> Vertices){
		
		if (leftNode != null)
			this.leftNode.getVertices(Vertices);
		Vertices.add(this.ownContent);
		if (rightNode != null)
			this.rightNode.getVertices(Vertices);
		
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
				rightNode = new TreeNode_Vertex(NewVertex, this); 
				return getLocalPosition() + 1;
			}
		} else {
			if (leftNode != null) {
				return this.leftNode.add(NewVertex);
			} else {
				leftNode = new TreeNode_Vertex(NewVertex, this);
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
			if (rightNode != null) {
				this.rightNode.remove(removedVertex);
			} else {
				System.out.println("Tried and failed in node removal, node not found");
				return;
			}
		} else {
			if (leftNode != null) {
				this.leftNode.remove(removedVertex);
			} else { 
				System.out.println("Tried and failed in node removal, node not found");
				return;
			}
		}
		
		encapsuledNodes--;
	}
	
	protected void remove() {
		System.out.println("Start remove procedure");
		
 		// If this node is the rightnode of the parent:
 		// Replace its rightnode with this rightnode and add the leftnode to the given node on its most left position
 		if (this.parentNode.getRightNode() == this) {
			this.parentNode.rightNode = this.rightNode;
			if (this.rightNode != null)
				this.rightNode.parentNode = this.parentNode;
			
			if (this.leftNode != null) {
				if (this.rightNode != null)
					this.rightNode.add(this.leftNode, true);
				else {// In case this contained no rightNode, set leftnode as the current node
					this.parentNode.rightNode = this.leftNode;
					this.leftNode.parentNode = this.parentNode;
				}
			}
			
		} else {
			// This node was contained as the parents left node, replace the leftnode
			
			this.parentNode.leftNode = this.leftNode;
			if (this.leftNode != null)
				this.leftNode.parentNode = this.parentNode;
			
			if (this.rightNode != null) {
				if (this.leftNode != null)
					this.leftNode.add(this.rightNode, false);
				else {
					this.parentNode.leftNode = this.rightNode;
					this.rightNode.parentNode = this.parentNode;
				}
			}
		}
 		
 		this.leftNode = null;
 		this.rightNode = null;
	}
	
	/**
	 * Returns the right Node of the TreeNode
	 * @return
	 */
	private TreeNode_Vertex getRightNode() {
		return this.rightNode;
	}

	/**
	 * Adds a node to the tree
	 * @param newNode
	 * @param asLeftNode
	 */
	private void add(TreeNode_Vertex newNode, boolean asLeftNode) {
		if (asLeftNode) {
			if (this.leftNode == null) {
				this.leftNode = newNode;
				newNode.parentNode = this;
			} else
				this.leftNode.add(newNode, asLeftNode);
		} else {
			if (this.rightNode == null) {
				this.rightNode = newNode;
				newNode.parentNode = this;
			} else {
				this.rightNode.add(newNode, asLeftNode);
			}
		}
		
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
