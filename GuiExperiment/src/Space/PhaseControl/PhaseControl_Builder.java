package Space.PhaseControl;

import Space.LineIntersect;
import Space.PointDouble;
import Space.Room;
import Space.RoomFragment;
import Space.VertexSegment;
import Space.Vertex;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Control options for the first phase of the program. Constructing of the room.
 * @author i_wou_000
 *
 */
public class PhaseControl_Builder extends PhaseControl{
	
	public Point2D Intersection;

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
		} else if (vertices.size() == 1) {
			Vertex previous = vertices.get(0);
			vertices.add(new Vertex(x, y, previous));
		} else {
			
			// Compute whether the new line point does not intersect any point in the figure
			Vertex previous = vertices.get(vertices.size() - 1);
	    	Point2D currentPoint = new PointDouble(x, y);

	    	if (this.doesIntersect(previous, currentPoint, false, false, true)) {
				return false;/*
	    		if (this.Intersection == null)
	    			System.out.println("Intersection found, but position unknown");
	    		else
	    			System.out.println("Intersection at "+this.Intersection.getX() + "  " + this.Intersection.getY());*/
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
			// Complete the loop in the vertices
			Vertex lastVertex = this.vertices.get(this.vertices.size() - 1);
			lastVertex.setNext(this.vertices.get(0));
			this.vertices.get(0).setPrevious(lastVertex);
			
			// Add the fragment to the room
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
    	return shapeCanClose = !doesIntersect(start, end, true, false, false);
    }
    
    /**
     * Calculates whether a line intersects with any of the lines in the working path
     * @param start The start position of the line
     * @param end The end position of the line
     * @return Whether the line indeed intersects
     */
    private boolean doesIntersect(Point2D start, Point2D end, boolean ignoreEndpoints, boolean ignoreLastPlacedVertex, boolean Store) {
    	int t = 0;
    	if (ignoreEndpoints)
    		t = 1;
    	
    	for(int i = t; i < vertices.size() - (ignoreLastPlacedVertex ? 3 : 2); i++)
		{
    		if (LineIntersect.doLinesIntersect(start, end, vertices.get(i), vertices.get(i + 1))) {
    			System.out.println("Intersected on i="+i);
    			if (Store) {
	    			VertexSegment S = new VertexSegment(vertices.get(i), vertices.get(i+1));
	    			this.Intersection = S.getIntersectionPoint(new VertexSegment(new Vertex(start), new Vertex(end)));
	    			System.out.println(this.Intersection);
    			}
    			return true;
    		}
		}
    	
    	return false;
    	
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
