package Space;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Room {

	private List<Point> points;
	private List<UpdateEvent> listeners = new ArrayList<UpdateEvent>();
	private boolean shapeCanClose;
	
	
	int x_min = Integer.MAX_VALUE;
	int x_max = Integer.MIN_VALUE;
	int y_min = Integer.MAX_VALUE;
	int y_max = Integer.MIN_VALUE;
	
	public Room(){
		points = new ArrayList<Point>();
		//AddPoint(10,10);
		//AddPoint(40,15);
		//AddPoint(15,40);
		
	}
	
	public List<Point> getPoints(){
		return points;
	}
	
	public boolean AddPoint(int x, int y) {
		// Compute whether the new line point does not intersect any point in the figure
		if (points.size() > 2)
		{
	    	Point start = points.get(points.size() - 1);
	    	Point end = new Point(x,y);
	    	
	    	for(int i=0; i + 2<points.size();i++) {
	    		if (LineIntersect.doLinesIntersect(start, end, points.get(i), points.get(i+1))) {
	    			System.out.println("New line intersects line "+i);			
	    			return false;
	    		}
			}
		}
		
		// Adjust boundaries
		if (x < x_min) x_min = x;
		if (y < y_min) y_min = y;
		if (x > x_max) x_max = x;
		if (y > y_max) y_max = y;
		
		points.add(new Point(x,y));
		this.onUpdate();
		
		return true;
	}
	
	public void RemoveLastPoint() {
		points.remove(points.size() - 1);
		this.onUpdate();
	}
	
	
	public Rectangle getBoundary(){
		//return new Rectangle(0, 0, 50, 50);
		return new Rectangle(x_min, y_min, x_max - x_min, y_max - y_min);
	}
	
	public void Clear() {
		points.clear();
		this.onUpdate();
	}
	
	public void addListener(UpdateEvent toAdd) {
        listeners.add(toAdd);
    }

    public void onUpdate() {
    	// recalc can close state
    	this.computeCanClose();
    	
        // Notify everybody that may be interested.
        for (UpdateEvent uE : listeners)
            uE.onUpdate();
    }
    
    public boolean canClose() {
    	return this.shapeCanClose;
    }
	
    private boolean computeCanClose() {
    	if (points.size() < 3) {
    		return this.shapeCanClose = false;
    	}
    	
    	Point start = points.get(0);
    	Point end = points.get(points.size() - 1);
    	
    	for(int i=1; i + 2<points.size();i++)
		{
    		if (LineIntersect.doLinesIntersect(start, end, points.get(i), points.get(i+1))) {
    			System.out.println("Failed on i="+i);			
    			return shapeCanClose = false;
    		}
		}
    	
    	return shapeCanClose = true;
    }
}
