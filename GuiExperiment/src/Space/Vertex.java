package Space;

import java.awt.*;

public class Vertex extends Point {
    private Vertex previous;
    private Vertex next;
    private boolean start;

    public Vertex(int x, int y, Vertex previous) {
        super(x, y);
        this.previous = previous;
        previous.setNext(this);
    }

    public Vertex(int x, int y, boolean start) {
        super(x, y);
        this.start = start;
    }

    public Vertex getPrevious() {
        return previous;
    }

    public void setPrevious(Vertex previous) {
        this.previous = previous;
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
}