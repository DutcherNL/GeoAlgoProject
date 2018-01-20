package Space.PhaseControl;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import Space.PointType;
import Space.Room;
import Tree_Sweep.TreeNode_SweepRoot;
import Space.Sweep_Form;
import Space.Utilities;
import Space.Vertex;
import Space.VertexSegment;

public class PhaseControl_LineSweep  extends PhaseControl{

	public Room room;
	public double yLine = 0;
	public boolean shapeComplete = false;
	public boolean visualizeShape = false;
	public List<VertexSegment> Shape;
	
	public TreeNode_SweepRoot status;
	
	public List<Vertex> intersections;
	
	public Sweep_Form mainForm;
	public Sweep_Form sideForm;
	
	private List<Vertex> addMAIN;
	private List<Vertex> addJOIN;
	private List<Vertex> splitMAIN;
	private List<Vertex> splitJOIN;
	private Vertex highestAdd;
	private Vertex highestSplit;
	private boolean highestAddInMain = false;
	private boolean highestSplitInMain = false;
	
	
	public PhaseControl_LineSweep(Room Room) {
		this.room = Room;
		
	}
	
	@Override
	public boolean canGoEnterNextPhase() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Put all startpoints in a tree
	 */
	public void computePointTypes() {
		
		this.mainForm = new Sweep_Form(this.room.getFragments().get(0).getVertices().get(0), true);
		if (this.room.getFragments().size() > 1)
			this.sideForm = new Sweep_Form(this.room.getFragments().get(1).getVertices().get(0), true);
		else
			this.sideForm = new Sweep_Form(null, true);
		
		this.onUpdate();
	}
	
	public void StartSweepStepWise() {
		this.status = new TreeNode_SweepRoot(this);
		this.intersections = new ArrayList<Vertex>();
		this.yLine = Double.MAX_VALUE;
		
		this.addMAIN = mainForm.getStartVertices();
		this.addJOIN = sideForm.getStartVertices();
		this.splitMAIN = mainForm.getSplitVertices();
		this.splitJOIN = sideForm.getSplitVertices();
		this.storeHighestAdd();
		this.storeHighestSplit();
		
		
		System.out.println("Sweep ready");
		
	}
	
	public void storeHighestAdd() {	
		if (this.addMAIN == null || this.addJOIN == null) {
			highestAdd = null;
			return;
		}
		
		
		// If either list has reached the end
		if (this.addMAIN.size() == 0) {
			if (this.addJOIN.size() == 0) {
				this.highestAdd = null; 
			} else {
				this.highestAdd = this.addJOIN.get(0);
				this.addJOIN.remove(0);
				this.highestAddInMain = false;
			}
		} else {
			if (this.addJOIN.size() == 0) {
				this.highestAdd = this.addMAIN.get(0);
				this.addMAIN.remove(0);
				this.highestAddInMain = true;
			} else {				
				
				Vertex startMain = this.addMAIN.get(0);
				Vertex startSide = this.addJOIN.get(0);
				
				// Both have points, check order
				if (Utilities.isBelow(startMain, startSide)) {
					this.highestAdd = this.addJOIN.get(0);
					this.addJOIN.remove(0);
					this.highestAddInMain = false;
				} else {
					this.highestAdd = startMain;
					this.addMAIN.remove(0);
					this.highestAddInMain = true;
				}				
			}
		}
		
		
	}
	public void storeHighestSplit() {	
		if (this.splitMAIN == null || this.splitJOIN == null) {
			highestSplit = null;
			return;
		}
		
		// If either list has reached the end
				if (this.splitMAIN.size() == 0) {
					if (this.splitJOIN.size() == 0) {
						this.highestSplit = null; 
					} else {
						this.highestSplit = this.splitJOIN.get(0);
						this.splitJOIN.remove(0);
						this.highestSplitInMain = false;
					}
				} else {
					if (this.splitJOIN.size() == 0) {
						this.highestSplit = this.splitMAIN.get(0);
						this.splitMAIN.remove(0);
						this.highestSplitInMain = true;
					} else {						
						
						Vertex startMain = this.splitMAIN.get(0);
						Vertex startSide = this.splitJOIN.get(0);
						
						// Both have points, check order
						if (Utilities.isBelow(startMain, startSide)) {
							this.highestSplit = this.splitJOIN.get(0);
							this.splitJOIN.remove(0);
							this.highestSplitInMain = false;
						} else {
							this.highestSplit = startMain;
							this.splitMAIN.remove(0);
							this.highestSplitInMain = true;
						}				
					}
				}
		
		
	}	
	
	/**
	 * Jump to next SweepAction
	 */
	public void sweepNextPoint() {
		Vertex topDomainVertex = null;
		
		topDomainVertex = this.status.getHighestVertex();
		
		if (Utilities.isBelow(this.highestAdd, topDomainVertex)) {			
			if (Utilities.isBelow(this.highestSplit, topDomainVertex)) {	
				if (topDomainVertex != null)
					this.sweepProcessDomain(topDomainVertex);
				else {
					System.out.println("Sweep complete");
					this.shapeComplete = true;
					this.onUpdate();
				}
			} else {
				this.sweepProcessSplit();
			}	
			
		} else {
			if (Utilities.isBelow(this.highestSplit, this.highestAdd)) {		
				this.sweepProcessAdd();
			} else {
				this.sweepProcessSplit();
			}	
		}

		this.onUpdate();
		
		System.out.println("\n");
	}
	
	/**
	 * Trigger the topmost action in the status
	 * @param topDomainVertex
	 */
	private void sweepProcessDomain(Vertex topDomainVertex) {
		this.yLine = topDomainVertex.getY();
		System.out.println("YLine:"+this.yLine);
		// Domain has a higher point
		this.status.update();

	}
	
	/**
	 * Add a new DomainSweep to the status
	 * @param vertex
	 */
	private void sweepProcessAdd() {
		this.yLine = this.highestAdd.getY();
		
		if (this.status.add(this.highestAdd, this.highestAddInMain)) {
			// Split was in visible area, add it to the main stack
			if (!this.highestAddInMain) {
				this.mainForm.addStartVertex(this.highestAdd);
			}
		} else {
			// Addition was in an invisible area, remove it
			if (this.highestAddInMain) {
				this.mainForm.removeStartVertex(this.highestAdd);
			}
		}
		
		this.storeHighestAdd();
	}
	
	private void sweepProcessSplit() {
		this.yLine = this.highestSplit.getY();
		
		if (this.status.split(this.highestSplit, this.highestSplitInMain)) {
			// Split was in visible area, add it to the main stack
			if (!this.highestSplitInMain) {
				this.mainForm.addSplitVertex(this.highestSplit);
			}
		} else {
			// Addition was in an invisible area, remove it
			if (this.highestSplitInMain) {
				this.mainForm.removeSplitVertex(this.highestSplit);
			}
		}

		this.storeHighestSplit();
	}
	
	public void PrintTree() {
		this.status.Print(yLine);
		
		System.out.println("Intersections:");
		for (Point2D point2d : intersections) {
			System.out.println(point2d);
		}
	}

	public void processNewIntersection (Vertex v) {
		PointType pointType = Utilities.computePointType(v);
		switch(pointType) {
		case STARTVERTEX:
			this.mainForm.addStartVertex(v);
			break;
		case SPLITVERTEX:
			this.mainForm.addSplitVertex(v);
			break;
		default:
			break;
		}
		
		intersections.add(v);
	}
	
	public void CompleteShape() {
		// This is pure for visualisation and is not optimised yet
		// Current duration O(kn^2) with k= the number of unique outlines
		this.Shape = new ArrayList<VertexSegment>();
		
		this.addMAIN = this.mainForm.getStartVertices();
		this.splitMAIN = this.mainForm.getSplitVertices();
		Vertex currVertex = null;
		
		// Loop over all known start vertices
		for (Vertex vertex : this.addMAIN) {
			currVertex = vertex;
			// Loop over the entire figure, make all segments, remove any vertices from the lists encountered
			do {
				this.Shape.add(new VertexSegment(currVertex, currVertex.getNext()));
				currVertex = currVertex.getNext();
				this.splitMAIN.remove(currVertex);
				if (currVertex != vertex) {
					this.addMAIN.remove(currVertex);
				}
				
			} while (currVertex != vertex);
		}
		
		// Loop over all split vertices
		for (Vertex vertex : this.splitMAIN) {
			currVertex = vertex;
			// Loop over the entire figure, make all segments, remove any vertices from the lists encountered
			do {
				this.Shape.add(new VertexSegment(currVertex, currVertex.getNext()));
				currVertex = currVertex.getNext();
				this.addMAIN.remove(currVertex);
				if (currVertex != vertex) {
					this.splitMAIN.remove(currVertex);
				}
				
			} while (currVertex != vertex);
		}
		
	}
}
