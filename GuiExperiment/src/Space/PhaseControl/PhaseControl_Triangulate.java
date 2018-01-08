package Space.PhaseControl;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import Space.Room;
import Space.RoomFragment;

public class PhaseControl_Triangulate extends PhaseControl {

	private List<Point> startVertex;
	private List<Point> splitVertex;
	private List<Point> endVertex;
	private List<Point> mergeVertex;
	
	public Room room;
	
	public PhaseControl_Triangulate(Room Room) {
		this.room = Room;
		
	}
	
	public List<Point> getPointType(String Type) {
		switch(Type) {
		case "START":
			return this.startVertex;
		case "SPLIT":
			return this.splitVertex;
		case "END":
			return this.endVertex;
		case "MERGE":
			return this.mergeVertex;
		}

		
		return null;
	}
	
	public void ComputePointTypes() {
		this.startVertex = new ArrayList<Point>();
		this.splitVertex = new ArrayList<Point>();
		this.endVertex = new ArrayList<Point>();
		this.mergeVertex = new ArrayList<Point>();
		
		List<Point> points;
		
		for(RoomFragment Fragment : this.room.getFragments()) {
			points = Fragment.getPoints();
			
			int correction;
			for(int i=0; i<points.size();i++) {
				if(i-1 < 0)
					correction = points.size();
				else correction = 0;
				
				this.computePointType(
						points.get(i-1 + correction),
						points.get(i),
						points.get((i+1)%points.size())
						);
			}
		}
		
		this.onUpdate();
	}
	
	private void computePointType(Point Previous, Point Current, Point Next) {
		double angle;
		
		if (isBelow(Previous,Current) &&
			isBelow(Next, Current)) {
			if ((angle = ComputeAngle(Previous, Current, Next)) < Math.PI)
				this.startVertex.add(Current);
			else if (angle > Math.PI)
				this.splitVertex.add(Current);
		}
		else if (isBelow(Current, Previous) &&
				 isBelow(Current, Next)){
			if ((angle = ComputeAngle(Previous, Current, Next)) < Math.PI)
				this.endVertex.add(Current);
			else if (angle > Math.PI)
				this.mergeVertex.add(Current);
		}
	}
	
	/**
	 * Compute if A is below B
	 * @param A
	 * @param B
	 * @return
	 */
	private boolean isBelow(Point A, Point B) {
		if (A.y < B.y) return true;
		if (A.y == B.y && A.x > B.x) return true;
		return false;
	}
	
	/**
	 * Computes the angle between two points from an origin
	 * @param A
	 * @param Origin
	 * @param B
	 * @return
	 */
	private double ComputeAngle(Point A, Point Origin, Point B) {
		
		double angleA = Math.atan2(A.x - Origin.x, A.y - Origin.y);
		double angleB = Math.atan2(B.x - Origin.x, B.y - Origin.y);
		double result = angleB - angleA;
		if (result < 0) result+= 2*Math.PI;
		System.out.println("Result angle: "+result);
		
		return result;
	}

	
	
	@Override
	public boolean canGoEnterNextPhase() {
		// TODO Auto-generated method stub
		return false;
	}

}
