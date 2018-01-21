package Space.PhaseControl;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import Space.Lights;
import Space.PointType;
import Space.Room;
import Tree_Sweep.TreeNode_SweepRoot;
import Space.Sweep_Form;
import Space.Utilities;
import Space.Vertex;
import Space.VertexSegment;

public class PhaseControl_LineSweep  extends PhaseControl{

	public Room room;
	public Lights lights;
	public double yLine = 0;
	protected int shapeCounter = 1;
	public boolean shapeComplete = false;
	public boolean visualizeShape = false;
	public List<VertexSegment> Shape;
	
	public TreeNode_SweepRoot status;
	
	public List<Vertex> intersections;
	public boolean useRoomFragments = false;
	
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
	
	
	public PhaseControl_LineSweep(Room Room, Lights lights) {
		super();
		this.room = Room;
		this.lights = lights;
		
				
	}
	
	public void ReadyFirstRun(boolean fromRoom) {
		this.useRoomFragments = fromRoom;
		
		if (fromRoom) {
			this.mainForm = new Sweep_Form(this.room.getFragments().get(0).getVertices().get(0), true);
		} else {
			// Use the lights
			this.mainForm = new Sweep_Form(this.lights.getVisibilityRegions().get(0).get(0), true);
		}
		
		this.setUpNextSweep();
		this.onUpdate();
	}
	
	@Override
	public boolean canGoEnterNextPhase() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setUpNextSweep() {
		if (this.useRoomFragments) {
			// Use the room fragments as deliminator
			if (this.room.getFragments().size() > this.shapeCounter) {
				this.sideForm = new Sweep_Form(this.room.getFragments().get(this.shapeCounter).getVertices().get(0), true);
				this.shapeCounter++;
			} else {
				this.sideForm = new Sweep_Form(null, true);
				this.shapeComplete = true;
				return;
			}
		} else {
			// Use the room fragments as deliminator
			if (this.lights.getVisibilityRegions().size() > this.shapeCounter) {
				this.sideForm = new Sweep_Form(this.lights.getVisibilityRegions().get(this.shapeCounter).get(0), true);
				this.shapeCounter++;
			} else {
				this.sideForm = new Sweep_Form(null, true);
				this.shapeComplete = true;
				return;
			}
		}
		
		
		
		
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
						if (Utilities.isBelow(startSide, startMain)) {
							this.highestSplit = startMain;
							this.splitMAIN.remove(0);
							this.highestSplitInMain = true;
						} else {
							this.highestSplit = this.splitJOIN.get(0);
							this.splitJOIN.remove(0);
							this.highestSplitInMain = false;
						}				
					}
				}
		
		
	}	
	
	/**
	 * Runs a full sweep automatically
	 */
	public void runFullSweep() {
		while (sweepNextPoint()) {
			
		}
		setUpNextSweep();
		this.onUpdate();
	}
	public void runSingleSweep() {
		if (this.shapeComplete) {
			return;
		}
		if (!sweepNextPoint()) {
			setUpNextSweep();
		}
		this.onUpdate();
	}
	
	/**
	 * Runs a single sweep instance
	 * @return Whether the sweep was succesful (i.e. not at the end)
	 */
	protected boolean sweepNextPoint() {
		Vertex topDomainVertex = null;
		
		topDomainVertex = this.status.getHighestVertex();
		
		if (Utilities.isBelow(this.highestAdd, topDomainVertex)) {			
			if (Utilities.isBelow(this.highestSplit, topDomainVertex)) {	
				if (topDomainVertex != null)
					this.sweepProcessDomain(topDomainVertex);
				else {
					System.out.println("Sweep complete");
					return false;
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
		
		return true;
	}
	
	/**
	 * Trigger the topmost action in the status
	 * @param topDomainVertex
	 */
	private void sweepProcessDomain(Vertex topDomainVertex) {
		this.yLine = topDomainVertex.getY();
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
		this.Shape = new ArrayList<VertexSegment>();
		
		Vertex currVertex = null;
		Vertex rootVertex;
		
		// Loop over all known start vertices
		for (int i=0; (rootVertex = this.mainForm.getStartVertex(i)) != null; i++) {
			currVertex = rootVertex;
			// Loop over the entire figure, make all segments, remove any vertices from the lists encountered
			do {
				this.Shape.add(new VertexSegment(currVertex, currVertex.getNext()));
				if (currVertex != rootVertex) {
					this.mainForm.removeVertex(currVertex);
				}
				currVertex = currVertex.getNext();
				
			} while (currVertex != rootVertex);
		}
		
		// Loop over all split vertices
		for (int i=0; (rootVertex = this.mainForm.getSplitVertex(i)) != null; i++) {
			currVertex = rootVertex;
			// Loop over the entire figure, make all segments, remove any vertices from the lists encountered
			do {
				this.Shape.add(new VertexSegment(currVertex, currVertex.getNext()));
				
				if (currVertex != rootVertex) {
					this.mainForm.removeVertex(currVertex);
				}
				currVertex = currVertex.getNext();
				
			} while (currVertex != rootVertex);
		}
		
		System.out.println("Done");
		System.out.println(this.Shape.size());
		
	}
}
