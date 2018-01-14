package Space.PhaseControl;

import java.util.ArrayList;
import java.util.List;

import Space.Segment;
import Space.SweepDomain;
import Space.Vertex;

/**
 * The Lineair status tree for the linear sweep. Contains sweepDomains ordened from left (lowest) to right (highest)
 * @author Wouter Roos
 *
 */
public class TreeNode_SweepDomain {
	protected SweepDomain ownContent;
	protected TreeNode_SweepDomain leftNode;
	protected TreeNode_SweepDomain rightNode;
	protected Vertex firstVerticalVertex;
	protected boolean containsHighestVertex = true;
	protected TreeNode_SweepDomain parentNode;
	
	/**
	 * Constructor for the node
	 * @param content The content contained by the node
	 * @param ParentNode The parent of the node
	 */
	public TreeNode_SweepDomain(SweepDomain content, TreeNode_SweepDomain ParentNode) {
		this.ownContent = content;
		this.parentNode = ParentNode;
		if (this.ownContent != null)
			this.reCalcVerticalVertex();
	}
	
	/**
	 * Get the most left content encapsulated in this node
	 * @return SweepDomain with the lowest result
	 */
	public SweepDomain getFirstContent() {
		if (leftNode == null)
			return ownContent;
		else
			return leftNode.getFirstContent();
	}
	/**
	 * Get the most right content encapsulated in this node
	 * @return SweepDomain with the highest location
	 */
	public SweepDomain getLastContent() {
		if (rightNode == null)
			return ownContent;
		else
			return leftNode.getFirstContent();
	}

	/**
	 * Gets the rightmost Node encapsulated in this node
	 * @return
	 */
	protected TreeNode_SweepDomain getLastNode() {
		if (rightNode == null)
			return this;
		else
			return rightNode.getLastNode();
	}	
	/**
	 * Gets the leftmost Node encapsulated in this node
	 * @return
	 */
	protected TreeNode_SweepDomain getFirstNode() {
		if (leftNode == null)
			return this;
		else
			return leftNode.getFirstNode();
	}	
	/**
	 * Returns the node that is before this node in the entire tree
	 * @return
	 */
	protected TreeNode_SweepDomain getPreviousNode() {
		if (leftNode == null) {
			return this.parentNode.getNextNode(this);
		}
		return this.leftNode.getLastNode();
	}
	/** 
	 * Returns the leftmost node that is before the given node, used when looping upward in parents
	 * @param Origin The node that is either in the left or right side of this node
	 * @return
	 */
	protected TreeNode_SweepDomain getPreviousNode(TreeNode_SweepDomain Origin) {
		if (this.rightNode == Origin) {
			return this;
		} else {
			if (this.parentNode != null)
				return this.parentNode.getPreviousNode(this);
			else return null;
		}
	}
	/**
	 * Returns the node that is before this node in the entire tree
	 * @return
	 */
	protected TreeNode_SweepDomain getNextNode() {
		if (rightNode == null) {
			if (parentNode != null)
				return this.parentNode.getNextNode(this);
			else return null;
		}
		return rightNode.getFirstNode();
	}
	/** 
	 * Returns the rightmost node that is before the given node, used when looping upward in parents
	 * @param Origin The node that is either in the left or right side of this node
	 * @return
	 */
	protected TreeNode_SweepDomain getNextNode(TreeNode_SweepDomain Origin) {
		if (this.rightNode != Origin) {
			return this;
		} else {
			if (this.parentNode != null)
				return this.parentNode.getNextNode(this);
			else return null;
		}
	}
	
	
	
	
	/**
	 * Add the contents of this node to the list in the given order
	 * @param Domains The list on which the domains are added
	 * @return The list result as a symbolic gesture
	 */
	protected List<SweepDomain> getDomains(List<SweepDomain> Domains){
		if (leftNode != null)
			this.leftNode.getDomains(Domains);
		if (this.ownContent != null)
			Domains.add(this.ownContent);
		if (rightNode != null)
			this.rightNode.getDomains(Domains);
		
		return Domains;
	}
	
	/**
	 * Recalculate the highest encapsulated vertical vertex
	 */
	protected void reCalcVerticalVertex() {
		// Calculate the values of the internal SweepDomain and assume this is first
		this.firstVerticalVertex =  this.ownContent.getHighestPoint();
		this.containsHighestVertex = true;
		
		// Check whether there is a leftnode, if so, check whether it contains a higher Vertex
		// Adjust accordingly if necessary
		if (this.leftNode != null) {
			if (Space.Utilities.isBelow(this.firstVerticalVertex, this.leftNode.firstVerticalVertex))	{
				this.firstVerticalVertex = this.leftNode.firstVerticalVertex;
				this.containsHighestVertex = false;
		}}
		// Check whether there is a rightnode, if so, check whether it contains a higher Vertex
		// Adjust accordingly if necessary 
		if (this.rightNode != null) {
			if (Space.Utilities.isBelow(this.firstVerticalVertex, this.rightNode.firstVerticalVertex))	{
				this.firstVerticalVertex = this.rightNode.firstVerticalVertex;
				this.containsHighestVertex = false;
		}}
		
		// Recalculate the parent to process any change to the root of the tree
		if (this.parentNode != null)
			this.parentNode.reCalcVerticalVertex();
	}
	
	
	/**
	 * Return the sweep domain held in this node
	 * @return
	 */
	public SweepDomain getContent() {
		return this.getContent();
	}
	/**
	 * Returns the first sweeplocation encapsulated in this node
	 * @return
	 */
	public Vertex getTopVertex() {
		return this.firstVerticalVertex;
	}
	
	
	/**
	 * Add a new vertex to the tree
	 * @param NewVertex
	 */
	public void add(SweepDomain NewDomain) {	
		// If the new SweepDomain is located more left, place it in the leftnode.
		if (this.isLower(NewDomain)) {
			if (leftNode != null)
				this.leftNode.add(NewDomain);
			else {
				leftNode = new TreeNode_SweepDomain(NewDomain, this);
				this.reCalcVerticalVertex();
			}
		} else { // It was higher, place it in the right node
			if (rightNode != null)
				this.rightNode.add(NewDomain);
			else {
				rightNode = new TreeNode_SweepDomain(NewDomain, this);
				this.reCalcVerticalVertex();
			}
		}		
	}
	
	/**
	 * Add a node in the collection of this node. Either completely left or completely right.
	 * It is used when moving or removing nodes from the tree.
	 * @param SweepNode The to be added node
	 * @param asLeftNode whether it should be completely next or completely right
	 */
	private void add(TreeNode_SweepDomain SweepNode, boolean asLeftNode) {
		if (asLeftNode) {
			if (this.leftNode == null) {
				this.leftNode = SweepNode;
				this.reCalcVerticalVertex();
			} else
				this.leftNode.add(SweepNode, asLeftNode);
		} else {
			if (this.rightNode == null) {
				this.rightNode = SweepNode;
				this.reCalcVerticalVertex();
			} else
				this.rightNode.add(SweepNode, asLeftNode);
		}
	}
	
	/**
	 * Checks whether a certain SweepDomain is lower than this own sweepDomain. It is assumed this is for a single similar polygon
	 * @param NewDomain The Sweep domain that it is compared with
	 * @return Whether the given domain is lower than the one in this node
	 */
	public boolean isLower(SweepDomain NewDomain) {
		//TODO: implement code to check on leftsegment intersection 
		return 	NewDomain.leftSegment.startVertex.getX() < this.ownContent.leftSegment.startVertex.getX();
	}
	
	/**
	 * Remove this node from the list
	 */
	public void Remove() {
		// If this node is the rightnode of the parent:
		// Replace its rightnode with this rightnode and add the leftnode to the given node on its most left position
		if (this.parentNode.rightNode == this) {
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
		
			// reCalcVertical is either sides are null. Otherwise it's automatically done by the called add method
		if (this.leftNode == null || this.rightNode == null)
			this.parentNode.reCalcVerticalVertex();
	}
	
	/**
	 * Check the source of the firstVerticalVertex and activate the update
	 */
	public void updateNextDomain() {
		if (this.leftNode != null && this.leftNode.firstVerticalVertex == this.firstVerticalVertex) {
			this.leftNode.updateNextDomain();
		} else {
			if (this.containsHighestVertex)
			{
				this.updateLocalDomain();
			} else { // There is no other possibility but the right is side is the lowest value
				this.rightNode.updateNextDomain();
			}
		}
	}
	/**
	 * The own content contains the next activated method, update that segment
	 */
	private void updateLocalDomain() {

			// If the next verticalVertex is on the left side
		if(this.ownContent.leftSegment.endVertex == this.firstVerticalVertex) {
			// In case both segments lead to the same endpoint (i.e. mergeVertex)
			if (this.ownContent.leftSegment.endVertex == this.ownContent.rightSegment.endVertex) {
				this.Remove();
				return;
			}
			
			//TODO: intersection implementation
			// Replace left segment with a new segment
			this.ownContent.leftSegment = new Segment(
					this.ownContent.leftSegment.endVertex,
					this.ownContent.leftSegment.endVertex.getPrevious());
						
		} else { 	// Update Right Segment
					// Check whether the rightsection endpoint shares an endpoint with the next SweepDomain
			TreeNode_SweepDomain NextNode = this.getNextNode();
			if (NextNode != null && NextNode.ownContent.leftSegment.endVertex == this.ownContent.rightSegment.endVertex) {
				this.mergeNodes(NextNode);
				
			} else {
				//TODO: intersection implementation
				// Replace right segment with a new segment
				this.ownContent.rightSegment = new Segment(
						this.ownContent.rightSegment.endVertex,
						this.ownContent.rightSegment.endVertex.getNext());
			}
		}
		
		this.reCalcVerticalVertex();
	}
	
	/**
	 * Merge the given TreeNode with the current node to one Sweepdomain
	 * @param nextNode
	 */
	private void mergeNodes(TreeNode_SweepDomain nextNode) {
		this.ownContent.rightSegment = nextNode.ownContent.rightSegment;
		//TODO: intersection code
		nextNode.Remove();
	}
	
	/**
	 * Export all domains in order as a list
	 * @param Domains
	 */
	public void exportToList(List<SweepDomain> Domains) {
		if (this.leftNode != null)
			this.leftNode.exportToList(Domains);
		if (this.ownContent != null)
			Domains.add(this.ownContent);
		if (this.rightNode != null)
			this.rightNode.exportToList(Domains);
	}

}
