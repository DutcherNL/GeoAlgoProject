package Tree_Sweep;

public abstract class TreeNode {
	protected TreeNode leftNode;
	protected TreeNode rightNode;
	protected TreeNode parentNode;

	/**
	 * Gets the rightmost Node encapsulated in this node
	 * @return
	 */
	protected TreeNode getLastNode() {		
		if (this.rightNode != null)
			return this.rightNode.getLastNode();
		else 
			return this;
	}		
	/**
	 * Gets the rightmost Node encapsulated in this node
	 * @return
	 */
	protected TreeNode getFirstNode() {		
		if (this.leftNode != null)
			return this.leftNode.getLastNode();
		else 
			return this;
	}	
	
	/**
	 * Returns the node that is before this node in the entire tree
	 * @return
	 */
	protected TreeNode getPreviousNode() {
		if (leftNode == null) {
			if (this.parentNode != null)
				return this.parentNode.getPreviousNode(this);
			else
				return null;
		} else {
			return this.leftNode.getLastNode();
		}
		
	}
	/** 
	 * Returns the leftmost node that is before the given node, used when looping upward in parents
	 * @param Origin The node that is either in the left or right side of this node
	 * @return
	 */
	protected TreeNode getPreviousNode(TreeNode Origin) {
		if (this.rightNode == Origin) {
			return this;
		}
		
		if (this.parentNode != null)
			return this.parentNode.getPreviousNode(this);
		else return null;
	}
	/**
	 * Returns the node that is before this node in the entire tree
	 * @return
	 */
	protected TreeNode getNextNode() {
		if (rightNode == null) {
			if (parentNode != null)
				return this.parentNode.getNextNode(this);
			else return null;
		} else {
			return rightNode.getFirstNode();
		}
	}
	/** 
	 * Returns the rightmost node that is before the given node, used when looping upward in parents
	 * @param Origin The node that is either in the left or right side of this node
	 * @return
	 */
	protected TreeNode getNextNode(TreeNode Origin) {
		if (this.leftNode == Origin) {
			return this;
		}
		
			// Other options failed, so go up if possible
		if (this.parentNode != null)
			return this.parentNode.getNextNode(this);
		else return null;
	}
	/**
	 * Returns the left node of this node
	 * @return
	 */
	protected TreeNode getLeftNode() {
		return this.leftNode;
	}
	/**
	 * Returns the right node of this node
	 * @return
	 */
	protected TreeNode getRightNode() {
		return this.rightNode;
	}
	
}
