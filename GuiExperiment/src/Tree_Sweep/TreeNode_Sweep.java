package Tree_Sweep;

import java.util.List;

import Space.Vertex;
import Space.VertexSegment;

public abstract class TreeNode_Sweep{
	protected TreeNode_Sweep leftNode;
	protected TreeNode_Sweep rightNode;
	protected TreeNode_Sweep parentNode;
	
	protected TreeNode_Sweep highestVertex;
	
	protected Tree_Sweep_Type type;
	protected int treeheight = 0;
	
	protected SweepDomain data;
	
	protected boolean isLeft;
	protected VertexSegment segment;
	protected TreeNode_Sweep opposite;
	
	protected boolean containsMain;
	protected boolean containsAlt;
	
	protected boolean isConflicted = false;
	
	public TreeNode_Sweep(VertexSegment segment, boolean isMain, boolean isLeft, TreeNode_Sweep parent) {
		this.segment = segment;
		this.type = (isMain ? Tree_Sweep_Type.MAIN : Tree_Sweep_Type.JOIN);
		this.isLeft = isLeft;
		this.calcNodeVariables();
		this.parentNode = parent;
		if (parent != null)
			this.treeheight = parent.treeheight + 1;
		
		this.data = new SweepDomain();
		
		
	}
	

	
	/**
	 * Gets the rightmost Node encapsulated in this node
	 * @return
	 */
	protected TreeNode_Sweep getLastNode(Tree_Sweep_Type type) {
			// Ideally this if is never passed, this is just a security measure
		if (!this.contains(type)) {
			System.out.println("ERROR TreeNode_SweepDomain getLastNode triggered on nonpossible node");
			return null;
		}
		
		if (rightNode != null && this.rightNode.contains(type)) {
			return rightNode.getLastNode(type);
		}
		else {
			if (this.isOfType(type))
				return this;
			else
				return this.leftNode.getLastNode(type);
		}
	}	
	/**
	 * Gets the rightmost Node encapsulated in this node
	 * @return
	 */
	protected TreeNode_Sweep getLastNode() {		
		if (this.rightNode != null)
			return this.rightNode.getLastNode();
		else 
			return this;
	}	
	/**
	 * Gets the leftmost Node encapsulated in this node
	 * @return
	 */
	protected TreeNode_Sweep getFirstNode(Tree_Sweep_Type type) {
			// Ideally this if is never passed, this is just a security measure
		if (!this.contains(type)) {
			System.out.println("ERROR TreeNode_SweepDomain getFirstNode triggered on nonpossible node");
			return null;
		}
		
		if (leftNode != null && this.leftNode.contains(type)) {
			return leftNode.getFirstNode(type);
		}
		else {
			if (this.isOfType(type))
				return this;
			else
				return this.rightNode.getFirstNode(type);
		}
	}	
	/**
	 * Gets the rightmost Node encapsulated in this node
	 * @return
	 */
	protected TreeNode_Sweep getFirstNode() {		
		if (this.leftNode != null)
			return this.leftNode.getLastNode();
		else 
			return this;
	}	
	
	/**
	 * Returns the node that is before this node in the entire tree
	 * @return
	 */
	protected TreeNode_Sweep getPreviousNode(Tree_Sweep_Type type) {
		if (leftNode == null) {
			if (this.parentNode != null)
				return this.parentNode.getPreviousNode(this, type);
			else
				return null;
		} else {
			if (this.leftNode.contains(type))
				return this.leftNode.getLastNode(type);
			else {
				if (parentNode != null)
					return this.parentNode.getPreviousNode(this, type);
				else
					return null;
			}
		}
		
	}
	/** 
	 * Returns the leftmost node that is before the given node, used when looping upward in parents
	 * @param Origin The node that is either in the left or right side of this node
	 * @return
	 */
	protected TreeNode_Sweep getPreviousNode(TreeNode_Sweep Origin, Tree_Sweep_Type type) {
		if (this.rightNode == Origin) {
			if (this.isOfType(type))
				return this;
			else if (this.leftNode != null && this.leftNode.contains(type))
					return this.leftNode.getLastNode(type);
		}
		
		if (this.parentNode != null)
			return this.parentNode.getPreviousNode(this, type);
		else return null;
	}

	/**
	 * Returns the node that is before this node in the entire tree
	 * @return
	 */
	protected TreeNode_Sweep getNextNode(Tree_Sweep_Type type) {
		if (rightNode == null) {
			if (parentNode != null)
				return this.parentNode.getNextNode(this, type);
			else return null;
		} else {
			if (this.rightNode.contains(type))
				return rightNode.getFirstNode(type);
			else {
				if (parentNode != null)
					return this.parentNode.getNextNode(this, type);
				else return null;
			}
		}
	}
	/** 
	 * Returns the rightmost node that is before the given node, used when looping upward in parents
	 * @param Origin The node that is either in the left or right side of this node
	 * @return
	 */
	protected TreeNode_Sweep getNextNode(TreeNode_Sweep Origin, Tree_Sweep_Type type) {
		if (this.rightNode != Origin) {
			if (this.isOfType(type))
				return this;
			else if (this.rightNode != null && this.rightNode.contains(type))
					return this.rightNode.getFirstNode(type);
		}
		
			// Other options failed, so go up if possible
		if (this.parentNode != null)
			return this.parentNode.getNextNode(this, type);
		else return null;
	}
	/**
	 * Returns the left node of this node
	 * @return
	 */
	protected TreeNode_Sweep getLeftNode() {
		return this.leftNode;
	}
	/**
	 * Returns the right node of this node
	 * @return
	 */
	protected TreeNode_Sweep getRightNode() {
		return this.rightNode;
	}
	
	
	/**
	 * Add the given node to this tree. Does not recompute position
	 * @param newNode
	 * @param asLeftNode
	 */
	protected void add(TreeNode_Sweep newNode, boolean asLeftNode) {
		if (asLeftNode) {
			if (this.leftNode == null) {
				this.leftNode = newNode;
				newNode.parentNode = this;
				this.leftNode.calcNodeVariables();
				this.leftNode.calcHeight();
			} else
				this.leftNode.add(newNode, asLeftNode);
		} else {
			if (this.rightNode == null) {
				this.rightNode = newNode;
				newNode.parentNode = this;
				this.rightNode.calcNodeVariables();
				this.rightNode.calcHeight();
			} else {
				this.rightNode.add(newNode, asLeftNode);
			}
		}
		
	}
	/**
 	 * Removes this node from the tree.
 	 * WARNING: DOES NOT DO INTERSECTIONS
 	 */
 	protected void remove() {
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
			
			if (this.parentNode.rightNode != null) this.parentNode.rightNode.calcHeight();
			
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
			
			if (this.parentNode.leftNode != null) this.parentNode.leftNode.calcHeight();
		}
 		
 		this.leftNode = null;
 		this.rightNode = null;
 		
 		this.parentNode.calcNodeVariables();
 	}
	
 	
 	protected void moveLeftOf(TreeNode_Sweep Target, Tree_Sweep_Type processType) {
 		TreeNode_Sweep nextNode = this.getPreviousNode(processType);
 		this.remove();
 		
 		while (nextNode != Target) {
 			nextNode.computeIntersection(this);	
 			nextNode = nextNode.getPreviousNode(processType);
 		} 
 		
 		if (Target.leftNode != null) {
 			Target.leftNode.add(this, false);
 			this.calcHeight();
 		} else {
 			Target.leftNode = this;
 			this.parentNode = Target;
 			this.calcHeight();
 			this.calcNodeVariables();
 		}
	}
 	protected void moveRightOf(TreeNode_Sweep Target, Tree_Sweep_Type processType) {
 		TreeNode_Sweep nextNode = this.getNextNode(processType);
 		this.remove();
 		
 		while (nextNode != Target) {
 			nextNode.computeIntersection(this);	
 			nextNode = nextNode.getNextNode(processType);
 		} 
 		
 		if (Target.rightNode != null) {
 			Target.rightNode.add(this, true);
 			this.calcHeight();
 		} else {
 			Target.rightNode = this;
 			this.parentNode = Target;
 			this.calcHeight();
 			this.calcNodeVariables();
 		}
 		
 	}
 	
 	/**
 	 * Check for possible intersections with nodes left of this node
 	 * @param y The height of the sweep line
 	 */
 	protected void checkIntersectLeft(double y) {
 		// check if something left is now right
 		// move that node to the right of this node
 		// continue
 		
 		
 		TreeNode_Sweep checkNode = this.getPreviousNode(this.getAntiType());
 		if (checkNode == null) {
 			this.checkIntersectRight(y);
 			return;
 		}
		TreeNode_Sweep storeNode;
 		
		
 		double xOwn = this.segment.getHorizontalIntersection(y).getX();
 		double xAlt;
 		try{
 			xAlt = checkNode.segment.getHorizontalIntersection(y).getX();
 		}	catch (Exception e) {
 			// There is a nasty error where somehow the intersection of the previous element is out of range.
 			// Likely due to the previous element not being the previous element, I don't know why though...
 			System.out.println(e);
 			this.checkIntersectRight(y);
 			return;
 		}
		
		if (xAlt < xOwn) {
			this.checkIntersectRight(y);
			return;
		}
		
		while (xOwn < xAlt) {
			System.out.println("Intersection found on LEFT side");
			// It has moved right of this
			storeNode = checkNode.getPreviousNode(this.getAntiType());
			checkNode.moveRightOf(this, checkNode.getAntiType());
			computeIntersection(checkNode);
			
			checkNode = storeNode;
	 		if (checkNode == null) {
	 			break;
	 		}
			xAlt = checkNode.segment.getHorizontalIntersection(y).getX();
		}
 	}
 	/**
 	 * Check for intersections with nodes on the right side of this node
 	 * @param y
 	 */
 	protected void checkIntersectRight(double y) {
 		// check if something left is now right
 		// move that node to the right of this node
 		// continue
 		
 		TreeNode_Sweep checkNode = this.getNextNode(this.getAntiType());
 		if (checkNode == null) {
 			return;
 		}
		TreeNode_Sweep storeNode;
 		
 		double xOwn = this.segment.getHorizontalIntersection(y).getX();
 		double xAlt;
 		try{
 			xAlt = checkNode.segment.getHorizontalIntersection(y).getX();
 		}	catch (Exception e) {
 			// There is a nasty error where somehow the intersection of the previous element is out of range.
 			// Likely due to the previous element not being the previous element, I don't know why though...
 			System.out.println(e);
 			return;
 		}
		
		while (xAlt < xOwn) {
			System.out.println("Intersection found on RIGHT side");
			// It has moved left of this
			storeNode = checkNode.getNextNode(this.getAntiType());
			checkNode.moveLeftOf(this, checkNode.getAntiType());
			computeIntersection(checkNode);
			
			checkNode = storeNode;
	 		if (checkNode == null) {
	 			break;
	 		}
			xAlt = checkNode.segment.getHorizontalIntersection(y).getX();
		}
 	}
 
 	/**
 	 * Fix local positioning without processing local intersections. Used for add and merge points
 	 * @param y
 	 */
 	protected void checkAbstractMoves(double y) {
 		this.checkAbstractMovesLeft(y, Tree_Sweep_Type.MAIN);
 		this.checkAbstractMovesLeft(y, Tree_Sweep_Type.JOIN);
 	}
 	/**
 	 * Used when an add or split is inserted, does not intersect with the point itself
 	 */
 	protected void checkAbstractMovesLeft(double y, Tree_Sweep_Type type) {
 	 		
 		// Get the next node
 		TreeNode_Sweep checkNode = this.getPreviousNode(type);
 		if (checkNode == null) {
 			this.checkAbstractMovesRight(y, type);
 			return;
 		}
		TreeNode_Sweep storeNode;
 		
 		double xOwn = this.segment.getHorizontalIntersection(y).getX();
		double xAlt = checkNode.segment.getHorizontalIntersection(y).getX();
				
		if (xAlt < xOwn) {
			this.checkAbstractMovesRight(y,type);
			return;
		}
		
		while (xOwn < xAlt) {
			// It has moved right of this
			storeNode = checkNode.getPreviousNode(this.getAntiType());
			System.out.println("Abstract move to the right");
			checkNode.moveRightOf(this, Tree_Sweep_Type.ALL);
			this.changeConflictedState();
			checkNode = storeNode;
	 		if (checkNode == null) {
	 			break;
	 		}
			xAlt = checkNode.segment.getHorizontalIntersection(y).getX();
		}
 	}
 	protected void checkAbstractMovesRight(double y, Tree_Sweep_Type type) {
 		
 		TreeNode_Sweep checkNode = this.getNextNode(type);
 		if (checkNode == null) {
 			return;
 		}
		TreeNode_Sweep storeNode;
 		
 		double xOwn = this.segment.getHorizontalIntersection(y).getX();
		double xAlt = checkNode.segment.getHorizontalIntersection(y).getX();
		
		while (xAlt < xOwn) {
			// It has moved left of this
			System.out.println("Abstract move to the left");
			storeNode = checkNode.getNextNode(type);
			checkNode.moveLeftOf(this, Tree_Sweep_Type.ALL);
			this.changeConflictedState();
			
			checkNode = storeNode;
	 		if (checkNode == null) {
	 			break;
	 		}
			xAlt = checkNode.segment.getHorizontalIntersection(y).getX();
		}
 	}
 	
 	
 	/**
 	 * Compute the location of the intersection and adjust the segments to represent the new outer line
 	 * @param intersegment
 	 */
 	protected abstract void computeIntersection(TreeNode_Sweep intersegment);

 	/**
 	 * Broadcast the found Vertex intersection upward in the tree
 	 * @param intersection
 	 */
 	protected void broadcastIntersection(Vertex intersection) {
 		this.parentNode.broadcastIntersection(intersection);
 	}
 	
 	
	/**
	 * Returns whether the given type is encapsulated in this node
	 * @param type2
	 * @return
	 */
	protected boolean contains(Tree_Sweep_Type type) {
		switch (type) {
		case ALL: 
			return true;
		case JOIN:
			return this.containsAlt;
		case MAIN:
			return this.containsMain;
		default:
			return false;
		}
		
	}
	/**
	 * Returns whether this node is of the given type
	 */
	protected boolean isOfType(Tree_Sweep_Type Type) {
		
		switch (Type) {
		case ALL: 
			return true;
		case JOIN:
			return this.type == Tree_Sweep_Type.JOIN;
		case MAIN:
			return this.type == Tree_Sweep_Type.MAIN;
		default:
			return false;
		}
	}
	/**
	 * Returns the opposite type of this figure
	 * @return
	 */
	protected Tree_Sweep_Type getAntiType() {
		return Tree_Sweep_Type.oppositeType(this.type);
	}
	
	/**
	 * Add new sweep interval starting at the given vertex
	 * @param vertex
	 * @param isMain
	 */
	public boolean add(Vertex vertex, boolean isMain) {
		boolean inLeftNode;
		
		if (this.isLower(vertex)) {
			if (this.leftNode == null) {
				inLeftNode = true;
			} else {
				return this.leftNode.add(vertex, isMain);
			}
		} else {
			if (this.rightNode == null) {
				inLeftNode = false;
			} else {
				return this.rightNode.add(vertex, isMain);
			}
		}
		
		return this.addLocal(vertex, inLeftNode, isMain);
	}
	protected boolean addLocal(Vertex vertex, boolean inLeftNode, boolean isMain) {
		
		// Add the primary treeNode
		TreeNode_Sweep newSweep = new TreeNode_SweepLeft(
				new VertexSegment(vertex, vertex.getNext()),
				isMain,
				this);
		if (inLeftNode) {
			this.leftNode = newSweep;
		} else {
			this.rightNode = newSweep;
		}
		
		// Set the state according to the spawnpoint
		TreeNode_Sweep altSweep = newSweep.getPreviousNode(newSweep.getAntiType());
		if (altSweep != null) {
			newSweep.isConflicted = altSweep.isLeft;
		} else {
			newSweep.isConflicted = false;
		}	
		
		// Make sure the positioning is correct
		newSweep.checkAbstractMoves(vertex.getY());
		
		// Share the data of the two segments
		newSweep.rightNode = new TreeNode_SweepRight(
				new VertexSegment(vertex.getPrevious(), vertex),
				isMain,
				newSweep);
		newSweep.rightNode.isConflicted = newSweep.isConflicted;
		
		newSweep.rightNode.data = newSweep.data;
		newSweep.opposite = newSweep.rightNode;
		newSweep.opposite.opposite = newSweep;
		newSweep.rightNode.calcNodeVariables();
		
		
		// Return whether the addition was in an outer layer
		return !newSweep.isConflicted;
	}
 	
	/**
	 * Finds the node under which the split node should be placed
	 * @param vertex
	 * @return
	 */
 	protected TreeNode_Sweep findSplitNodeLocation(Vertex vertex) {
 		// Find the vertex left of this point
 		// get previous point
		if (this.isLower(vertex)) {
			if (this.leftNode == null) {
				return null;
			} else {
				return this.leftNode.findSplitNodeLocation(vertex);
			}
		} else {
			if (this.rightNode == null) {
				// Place it here
			} else {
				TreeNode_Sweep splitLocation = this.rightNode.findSplitNodeLocation(vertex);
				if (splitLocation != null) {
					return splitLocation;
				} else {
					// place it in this node
				}
			}
		}
		
		return this;
 	}
 	/**
 	 * Locally splits the given shape
 	 * @param vertex
 	 * @param isMain
 	 * @return
 	 */
 	protected boolean splitLocal(Vertex vertex, boolean isMain) {		
 		// Make the left side node
 		TreeNode_Sweep newSweep = new TreeNode_SweepRight(
				new VertexSegment(vertex.getPrevious(), vertex),
				isMain,
				this);
 		
		if (this.rightNode == null) {
			this.rightNode = newSweep;
		} else {
			this.rightNode.add(newSweep, true);
		}
 		
		if (this.isOfType(isMain ? Tree_Sweep_Type.MAIN : Tree_Sweep_Type.JOIN)) {
			newSweep.isConflicted = this.isConflicted;
		} else {
			newSweep.isConflicted = this.isLeft;
		}
		
		// Sort the local data
		newSweep.checkAbstractMoves(vertex.y);
		
		// Add the right half
		newSweep.rightNode = new TreeNode_SweepLeft(
				new VertexSegment(vertex, vertex.getNext()),
				isMain,
				newSweep);
		
		// Get the left part of this form
		TreeNode_Sweep leftNode = newSweep.getPreviousNode(newSweep.type);
		
		// Transfer Data
		newSweep.rightNode.opposite = leftNode.opposite;
		leftNode.opposite.opposite = newSweep.rightNode;
		leftNode.opposite.data = newSweep.rightNode.data;
				
		newSweep.data = leftNode.data;
		leftNode.opposite = newSweep;
		newSweep.opposite = leftNode;
		newSweep.rightNode.isConflicted = newSweep.isConflicted;
		
 		newSweep.rightNode.calcNodeVariables();
 		// Result is complete, output accurate
 		
 		// Return whether the split was in a non-conflicted area
 		return !newSweep.isConflicted;
 	}
 	

 	/**
 	 * Update this side, i.e. move to the next segment and trigger any even necessary
 	 */
 	protected abstract void update();
 	
 	
 	
 	
	protected void changeConflictedState() {
		this.isConflicted = !this.isConflicted;
	}
 	/**
	 * Recalculate the highest encapsulated vertical vertex
	 */
	protected void calcNodeVariables() {
		// Calculate the values of the internal SweepDomain and assume this is first
		//Vertex highestVertex = this.segment.getHighestValue();
		
		this.highestVertex =  this;
		this.containsMain = this.isOfType(Tree_Sweep_Type.MAIN);
		this.containsAlt =  this.isOfType(Tree_Sweep_Type.JOIN);
		
		// Check whether there is a leftnode, if so, check whether it contains a higher Vertex
		// Adjust accordingly if necessary
		if (this.leftNode != null) {
			// The order below is important in case both points are equal
			if (!Space.Utilities.isBelow(
					this.leftNode.highestVertex.segment.getLowestValue(),
					highestVertex.segment.getLowestValue()))	{
				this.highestVertex = this.leftNode.highestVertex;					
			}
			if (this.leftNode.containsMain)
				this.containsMain = true;
			if (this.leftNode.containsAlt)
				this.containsAlt = true;
		}
		// Check whether there is a rightnode, if so, check whether it contains a higher Vertex
		// Adjust accordingly if necessary 
		if (this.rightNode != null) {
			if (Space.Utilities.isBelow(
					highestVertex.segment.getLowestValue(),
					this.rightNode.highestVertex.segment.getLowestValue()))	{
				highestVertex = this.rightNode.highestVertex;
			}
			if (this.rightNode.containsMain)
				this.containsMain = true;
			if (this.rightNode.containsAlt)
				this.containsAlt = true;
			
		}
		
		// Recalculate the parent to process any change to the root of the tree
		if (this.parentNode != null)
			this.parentNode.calcNodeVariables();
	}
	
	
	

	/**
	 * Recalculates the height of this node, computed top down
	 */
	protected void calcHeight() {
		if (this.parentNode.treeheight + 1 != this.treeheight) {
			this.treeheight = this.parentNode.treeheight +1;
			if (this.leftNode != null)
				this.leftNode.calcHeight();
			if (this.rightNode != null)
				this.rightNode.calcHeight();
		}
	}
	
	/**
	 * Return whether the given value is lower than the current value
	 * @param vertex
	 * @return
	 */
	protected boolean isLower(Vertex Vertex) {
		double res = this.segment.getHorizontalIntersection(Vertex.getY()).getX();
		return Vertex.getX() < res;
	}

	
	/**
	 * Export all domains in order as a list
	 * @param Domains
	 */
 	protected void exportToList(List<SweepDomain> Domains) {
		if (this.leftNode != null)
			this.leftNode.exportToList(Domains);

		if (this.isLeft) {
			this.data.leftSegment = this.segment;
		} else {
			this.data.rightSegment = this.segment;
			Domains.add(this.data);
		}
		
		if (this.rightNode != null)
			this.rightNode.exportToList(Domains);
	}

 	/**
 	 * Print the current tree underneith this node
 	 * @param y
 	 */
 	public void Print(double y) {
 		if (this.leftNode != null)
 			this.leftNode.Print(y);
 		
 		if (this.parentNode != null ) {
			System.out.println(this.toString() + 
					"  x: " + this.segment.getHorizontalIntersection(y).getX());
 		}
 		if (this.rightNode != null)
 			this.rightNode.Print(y);
 	}



 	
 	@Override
 	public String toString() {
 		return 
 				"Name: " +this.data.value + 
				" h:" + this.treeheight + 
				" " + (this.type) +
				" " + (this.isLeft ? "L" : "R") + 
				" " + (this.isConflicted ? "X" : "_") +
				" " + (this.containsMain ? "M" : "_") +
				" " + (this.containsAlt ? "A" : "_")
				;
 	}
}
