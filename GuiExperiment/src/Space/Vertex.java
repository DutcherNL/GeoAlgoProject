package Space;

import java.awt.geom.Point2D;

;

public class Vertex extends PointDouble {
    private Vertex previous;
    private Vertex next;
    private boolean start;

    public Vertex(double x, double y, Vertex previous) {
        super(x, y);
        this.previous = previous;
        previous.setNext(this);
        this.start = false;
    }

    public Vertex(double x, double y, boolean start) {
        super(x, y);
        this.start = start;
    }

    public Vertex(double x, double y) {
        super(x, y);
    }

    public Vertex(Point2D point) {
        super(point.getX(), point.getY());
    }

    public Vertex(Vertex vertex) {
        super(vertex.getX(), vertex.getY());
        this.previous = vertex.getPrevious();
        this.next = vertex.getNext();
    }

    public Vertex getPrevious() {
        return previous;
    }

    public void setPrevious(Vertex previous) {
        this.previous = previous;
        previous.setNext(this);
    }

    public Vertex getNext() {
        return next;
    }

    public void setNext(Vertex next) {
        this.next = next;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }
    
    public String toString() {
    	String result = "x: "+x + " y: "+y + "  -  ";
//    	if (this.next == null)
//    		result+= "No next present";
//    	else
//    		result+= "Next: x: " + this.next.x + " y: "+this.next.y + "   -    " ;
//
//    	if (this.previous == null)
//    		result+= "No previous present";
//    	else
//    		result+= "Previous: x: " + this.previous.x + " y: "+this.previous.y;
    	
    	return result;
    }
}