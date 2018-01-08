package Space.PhaseControl;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import Space.*;

/**
 * Control options for the first phase of the program. Constructing of the room.
 * @author i_wou_000
 *
 */
public class PhaseControl_Builder extends PhaseControl{

	/**
	 *  Store a collection of working points to create a shape.
	 */
	private List<Point> points;

	private boolean shapeCanClose;
	/**
	 * 
	 * @return Whether the current shape is viable for closing
	 */
    public boolean canClose() {
    	return this.shapeCanClose;
    }
	
    /**
     * Store the room locally to be constructed upon.
     */
	public Room room;
	
	/**
	 * Constructor with the room in progress
	 * @param Room
	 */
	public PhaseControl_Builder(Room Room){
		this.room = Room;
		
		points = new ArrayList<Point>();		
	}
	
	/**
	 * Return the current points forming a to be constructed area of the room.
	 * @return All points of the new addition in progress
	 */
	public List<Point> getPoints(){
		return points;
	}
	
	/**
	 * Add a new point to the current shape
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * @return Whether the new position was legit (if not no point is created)
	 */
	public boolean addPoint(int x, int y) {
		// Compute whether the new line point does not intersect any point in the figure
		if (points.size() > 2)
		{
	    	Point start = points.get(points.size() - 1);
	    	Point end = new Point(x,y);
	    	
	    	if (!this.doesIntersect(start, end, false))
	    		return false;
		}

		points.add(new Point(x,y));
		this.computeCanClose();
		this.onUpdate();
		
		return true;
	}
	
	/**
	 * Removes the last point from the current creating shape
	 */
	public void removeLastPoint() {
		if (points.size() > 0)
			points.remove(points.size() - 1);
		this.computeCanClose();
		this.onUpdate();
	}
	
	/**
	 * Clears all points from the current shape being created.
	 */
	public void clearPoints() {
		points.clear();
		this.onUpdate();
	}
	
	/**
	 * Clear all currently created fragments from the room.
	 */
	public void clearFragments() {
		room.clear();
		this.onUpdate();
	}
	
	/**
	 * Store the current created design in the room as a fragment
	 */
	public void ExportToRoom() {
		if (shapeCanClose) {
			room.addFragment(new RoomFragment(this.points));
			this.points = new ArrayList<Point>();
			
			this.computeCanClose();
			this.onUpdate();
		}
		
	}
	
	

	/**
	 * Computes whether the shape can legitimately close
	 * @return
	 */
    private boolean computeCanClose() {
    	if (points.size() < 3) {
    		return this.shapeCanClose = false;
    	}
    	
    	Point start = points.get(0);
    	Point end = points.get(points.size() - 1);
    	return shapeCanClose = doesIntersect(start, end, true);
    }
    
    /**
     * Calculates whether a line intersects with any of the lines in the working path
     * @param start The start position of the line
     * @param end The end position of the line
     * @return Whether the line indeed intersects
     */
    private boolean doesIntersect(Point start, Point end, boolean ignoreEndpoints) {
    	int t = 0;
    	if (ignoreEndpoints)
    		t = 1;
    	
    	for(int i=t; i + 2<points.size();i++)
		{
    		if (LineIntersect.doLinesIntersect(start, end, points.get(i), points.get(i+1))) {
    			System.out.println("Failed on i="+i);			
    			return false;
    		}
		}
    	
    	//TODO: implement intersection with other fragments
    	
    	return true;
    	
    }

    /**
     * Get the boundary box of the entire room
     * @return
     */
	public Rectangle getBoundary() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean canGoEnterNextPhase() {
		return room.getFragments().size() > 0;
	}
}
