package Tree_Sweep;

import java.util.ArrayList;
import java.util.List;

import Space.Vertex;

public class TreeNode_VertexRoot extends TreeNode_Vertex{

	public TreeNode_VertexRoot() {
		super(null, null);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int add(Vertex newVertex) {
		if (this.leftNode == null) {
			this.leftNode = new TreeNode_Vertex(newVertex, this);
			return 0;
		} else {
			return this.leftNode.add(newVertex);
		}
	}
	
	@Override
	public Vertex get(int i) {
		if (this.leftNode == null) {
			return null;
		} else {
			return this.leftNode.get(i);
		}
	}
	
	@Override
	public void remove(Vertex removedVertex) {
		if (this.leftNode == null) {
			;
		} else {
			this.leftNode.remove(removedVertex);
		}
	}

	@Override
	public Vertex getLastContent() {
		if (this.leftNode != null) {
			return this.leftNode.getLastContent();
		} else {
		return null;
		}
	}
	
	public List<Vertex> getVertices(){
		if (this.leftNode != null) {
			return this.leftNode.getVertices(new ArrayList<Vertex>());
		} else {
			return new ArrayList<Vertex>();
		}
	}
}


