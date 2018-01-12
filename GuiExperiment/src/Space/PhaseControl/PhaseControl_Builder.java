package Space.PhaseControl;

import Space.LineIntersect;
import Space.Room;
import Space.RoomFragment;
import Space.Vertex;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Control options for the first phase of the program. Constructing of the room.
 * @author i_wou_000
 *
 */
public class PhaseControl_Builder extends PhaseControl{

	/**
	 *  Store a collection of working vertices to create a shape.
	 */
	private List<Vertex> vertices;

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
		
		vertices = new ArrayList<Vertex>();
	}
	
	/**
	 * Return the current vertices forming a to be constructed area of the room.
	 * @return All vertices of the new addition in progress
	 */
	public List<Vertex> getVertices(){
		return vertices;
	}
	
	/**
	 * Add a new point to the current shape
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * @return Whether the new position was legit (if not no point is created)
	 */
	public boolean addVertex(int x, int y) {
		if (vertices.size() == 0) {
			vertices.add(new Vertex(x, y, true));
		} else if (vertices.size() == 2) {
			Vertex previous = vertices.get(0);
			vertices.add(new Vertex(x, y, previous));
		} else {
			// Compute whether the new line point does not intersect any point in the figure
			Vertex previous = vertices.get(vertices.size() - 1);
	    	Point currentPoint = new Point(x, y);
	    	
	    	if (!this.doesIntersect(previous, currentPoint, false)) {
				return false;
			}

			vertices.add(new Vertex(x, y, previous));
		}

		this.computeCanClose();
		this.onUpdate();
		
		return true;
	}
	
	/**
	 * Removes the last point from the current creating shape
	 */
	public void removeLastVertex() {
		if (vertices.size() > 0)
			vertices.remove(vertices.size() - 1);
		this.computeCanClose();
		this.onUpdate();
	}
	
	/**
	 * Clears all vertices from the current shape being created.
	 */
	public void clearVertices() {
		vertices.clear();
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
			room.addFragment(new RoomFragment(this.vertices));
			this.vertices = new ArrayList<Vertex>();
			
			this.computeCanClose();
			this.onUpdate();
		}
		
	}
	
	

	/**
	 * Computes whether the shape can legitimately close
	 * @return
	 */
    private boolean computeCanClose() {
    	if (vertices.size() < 3) {
    		return this.shapeCanClose = false;
    	}
    	
    	Vertex start = vertices.get(0);
    	Vertex end = vertices.get(vertices.size() - 1);
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
    	
    	for(int i = t; i + 2 < vertices.size(); i++)
		{
    		if (LineIntersect.doLinesIntersect(start, end, vertices.get(i), vertices.get(i+1))) {
    			System.out.println("Failed on i="+i);			
    			return false;
    		}
		}
    	
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
