package Space;

import java.awt.geom.Point2D;

public class Vertex extends PointDouble {
    private Vertex previous;
    private Vertex next;
    private boolean start;
    private boolean intermediate = false;

    public void setIntermediate(boolean intermediate) {
        this.intermediate = intermediate;
    }

    public boolean isIntermediate() {
        return intermediate;
    }

    public Vertex(double x, double y, Vertex previous) {
        super(x, y);
        this.setPrevious(previous);
    }

    public Vertex(double x, double y, boolean start) {
        super(x, y);
        this.start = start;
    }
    public Vertex(double x, double y) {
        super(x, y);
    }

    public Vertex(Point2D start2) {
		// TODO remove this constructor
    	super(start2.getX(), start2.getY());
	}

	public Vertex getPrevious() {
        return previous;
    }

    public void setPrevious(Vertex previous) {
        this.previous = previous;
        if (previous != null) {
            previous.setNext(this);
        }
    }

    public Vertex getNext() {
        return next;
    }
    
    public void setNext(Vertex next) {
        this.next = next;
        if (next != null)
        	this.next.previous = this;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }
    
    public String toString() {
    	String result = "x: "+x + " y: "+y + "  -  ";
    	if (this.next == null)
    		result+= "No next present";
    	else
    		result+= "Next: x: " + this.next.x + " y: "+this.next.y + "   -    " ;
    	
    	if (this.previous == null)
    		result+= "No previous present";
    	else
    		result+= "Previous: x: " + this.previous.x + " y: "+this.previous.y;
    	
    	return result;
    }
    
    public String printCoordinates() {
    	return "x: "+x + " y: "+y;
    }
    
    /**
     * Returns whether the next coordinate is at the same position
     * @return
     */
    public boolean isHeldHere() {
    	if (Math.abs(this.y - this.next.y) < 0.0001) {
    		if(Math.abs(this.x - this.next.x)  < 0.0001) {
    			return true;
    		}
    	}
    	return false;
    }
}