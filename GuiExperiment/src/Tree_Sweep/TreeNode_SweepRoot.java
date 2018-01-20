package Tree_Sweep;

import java.util.ArrayList;
import java.util.List;

import Space.Vertex;
import Space.PhaseControl.PhaseControl_LineSweep;

public class TreeNode_SweepRoot extends TreeNode_Sweep{

	PhaseControl_LineSweep lineSweep;
	
	public TreeNode_SweepRoot(PhaseControl_LineSweep lineSweep) {
		super(null, false, false, null);
		
		this.lineSweep = lineSweep;
	}
	
	@Override
	public void add(Vertex vertex, boolean isMain) {
		if (this.leftNode == null) {
			this.addLocal(vertex, true, isMain);
		} else {
			this.leftNode.add(vertex, isMain);
		}
	}
	@Override
	public boolean split(Vertex vertex, boolean isMain) {
		if (this.leftNode != null)
			return this.leftNode.split(vertex, isMain);
		return false;
	}
	
	public List<SweepDomain> exportToList(){
		List<SweepDomain> Domains = new ArrayList<SweepDomain>();
		if (this.leftNode != null) {
			this.leftNode.exportToList(Domains);
		}
		
		return Domains;
	}
	
	public Vertex getHighestVertex() {
		if (this.highestVertex != null)
			return highestVertex.segment.getLowestValue();
		else return null;
	}
	
	@Override
	protected void broadcastIntersection(Vertex intersection) {
		
		this.lineSweep.processNewIntersection(intersection);
		
		
	}
	
	public void update() {
		if (this.highestVertex != null) {
			this.highestVertex.update();
		}
	}
	
	@Override
	protected void calcNodeVariables() {
		if (this.leftNode != null)
			this.highestVertex = this.leftNode.highestVertex;
		else
			this.highestVertex = null;
	}

	@Override
	protected TreeNode_Sweep getLastNode() {
		if (this.leftNode != null) {
			return this.leftNode.getLastNode();
		} else {
			return null;
		}
	}
	@Override
	protected TreeNode_Sweep getLastNode(Tree_Sweep_Type type) {
		if (this.leftNode != null) {
			return this.leftNode.getLastNode(type);
		} else {
			return null;
		}
	}
	@Override
	protected TreeNode_Sweep getFirstNode() {
		if (this.leftNode != null) {
			return this.leftNode.getFirstNode();
		} else {
			return null;
		}
	}
	@Override
	protected TreeNode_Sweep getFirstNode(Tree_Sweep_Type type) {
		if (this.leftNode != null) {
			return this.leftNode.getFirstNode(type);
		} else {
			return null;
		}
	}

	@Override
	protected TreeNode_Sweep getNextNode(Tree_Sweep_Type type) {
		if (this.leftNode != null) {
			return this.leftNode.getNextNode(type);
		} else {
			return null;
		}
	}
	@Override
	protected TreeNode_Sweep getNextNode(TreeNode_Sweep Origin, Tree_Sweep_Type type) {
		return null;
	}
	@Override
	protected TreeNode_Sweep getPreviousNode(Tree_Sweep_Type type) {
		if (this.leftNode != null) {
			return this.leftNode;
		} else {
			return null;
		}
	}
	@Override
	protected TreeNode_Sweep getPreviousNode(TreeNode_Sweep Origin, Tree_Sweep_Type type) {
		return null;
	}

	@Override
	protected TreeNode_Sweep getLeftNode() {
		return null;
	}
	@Override
	protected TreeNode_Sweep getRightNode() {
		return null;
	}

	@Override
	protected void computeIntersection(TreeNode_Sweep intersegment) {
		System.out.println("Ha, he wanted to compute an intersection on the root node, the idiot.... XD");
		
	}


}
