package Space.PhaseControl;

import java.util.ArrayList;
import java.util.List;

import Space.SweepDomain;

public class TreeNode_SweepDomain_Root extends TreeNode_SweepDomain{

	public TreeNode_SweepDomain_Root() {
		super(null, null);
	}
	
	@Override
	public void add(SweepDomain NewDomain) {
		if (this.ownContent == null) {
			this.ownContent = NewDomain;
			this.reCalcVerticalVertex();
			return;
		}
		
		super.add(NewDomain);
		
	}
	
	@Override
	public void Remove() {
		System.out.println("Remove method triggered on root");
		
		if (this.leftNode != null) {
			// If there are elements in the left side, get the previous element and replace it with the current content
			TreeNode_SweepDomain movedNode = this.leftNode.getLastNode();
			this.ownContent = movedNode.ownContent;
			movedNode.Remove();
		} else if (this.rightNode != null) {
			// If there are elements in the right side, get the next element and replace it with the current content
			TreeNode_SweepDomain movedNode = this.rightNode.getLastNode();
			this.ownContent = movedNode.ownContent;
			movedNode.Remove();
		} else {
			// There is neither a left nor right side, clear the own content (i.e. empty tree)
			this.ownContent = null;
			System.out.println("Root cleared");
			this.firstVerticalVertex = null;
			this.containsHighestVertex = false;
		}
	}
	
	/**
	 * Return the contents of the tree as a list in the given order.
	 * @return
	 */
	public List<SweepDomain> getDomains(){
		return getDomains(new ArrayList<SweepDomain>());
	}

}
