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

public class PhaseControl_LineSweep  extends PhaseControl{

	public Room room;
	public double yLine = 0;
	public boolean shapeComplete = false;
	public boolean visualizeShape = false;
	
	public TreeNode_SweepRoot status;
	
	public List<Vertex> intersections;
	
	public Sweep_Form mainForm;
	public Sweep_Form sideForm;
	
	private List<Vertex> addMAIN;
	private List<Vertex> addJOIN;
	private List<Vertex> splitMAIN;
	private List<Vertex> splitJOIN;
	private int addMAINCounter = 0;
	private int addJOINCounter = 0;
	private int splitMAINCounter = 0;
	private int splitJOINCounter = 0;
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
		this.addMAINCounter = 0;
		this.addJOINCounter = 0;
		this.splitMAINCounter = 0;
		this.splitJOINCounter = 0;
		this.storeHighestAdd();
		this.storeHighestSplit();
		
		
		System.out.println("Sweep ready");
		
	}
	
	public void storeHighestAdd() {	
		if (this.addMAIN == null || this.addJOIN == null) {
			highestAdd = null;
			return;
		}
		
		System.out.println("XXXP "+this.addMAIN.size() + this.addJOIN.size());
		
		
		// If either list has reached the end
		if (this.addMAIN.size() <= this.addMAINCounter) {
			if (this.addJOIN.size() <= this.addJOINCounter) {
				this.highestAdd = null; 
			} else {
				this.highestAdd = this.addJOIN.get(this.addJOINCounter);
				this.highestAddInMain = false;
				this.addJOINCounter++;
			}
		} else {
			System.out.println("HUUUHH");
			if (this.addJOIN.size() < this.addJOINCounter) {
				this.highestAdd = this.addMAIN.get(this.addMAINCounter);
				this.highestAddInMain = true;
				this.addMAINCounter++;
			} else {
				Vertex startMain = this.addMAIN.get(this.addMAINCounter);
				Vertex startSide = this.addJOIN.get(this.addJOINCounter);
				
				// Both have points, check order
				if (Utilities.isBelow(startMain, startSide)) {
					this.highestAdd = startSide;
					this.highestAddInMain = false;
					this.addJOINCounter++;
				} else {
					this.highestAdd = startMain;
					this.highestAddInMain = true;
					this.addMAINCounter++;
				}				
			}
		}
		
		
	}
	public void storeHighestSplit() {	
		if (this.splitMAIN == null || this.splitJOIN == null) {
			highestAdd = null;
			return;
		}
		
		// If either list has reached the end
		if (this.splitMAIN.size() > this.splitMAINCounter) {
			if (this.splitJOIN.size() < this.splitJOINCounter) {
				this.highestSplit = null; 
			} else {
				this.highestSplit = this.splitJOIN.get(this.splitJOINCounter);
				this.highestSplitInMain = false;
				this.splitJOINCounter++;
			}
		} else {
			if (this.splitJOIN.size() < this.splitJOINCounter) {
				this.highestSplit = this.splitMAIN.get(this.splitMAINCounter);
				this.highestSplitInMain = true;
				this.splitMAINCounter++;
			} else {
				Vertex startMain = this.splitMAIN.get(this.splitMAINCounter);
				Vertex startSide = this.splitJOIN.get(this.splitJOINCounter);
				
				// Both have points, check order
				if (Utilities.isBelow(startMain, startSide)) {
					this.highestSplit = startSide;
					this.highestSplitInMain = false;
					this.splitJOINCounter++;
				} else {
					this.highestSplit = startMain;
					this.highestSplitInMain = true;
					this.splitMAINCounter++;
				}				
			}
		}
		
		
	}	
	
	/**
	 * Jump to next SweepAction
	 */
	public void sweepNextPoint() {

		System.out.println(this.highestAdd);
		System.out.println(this.highestSplit);
		
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
		
		this.status.add(highestAdd, this.highestAddInMain);
		//TODO: feedback
		
		this.storeHighestAdd();
	}
	
	private void sweepProcessSplit() {
		this.yLine = this.highestSplit.getY();
		this.status.split(this.highestSplit, this.highestSplitInMain);
		//TODO: feedback
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
		
	}
}
