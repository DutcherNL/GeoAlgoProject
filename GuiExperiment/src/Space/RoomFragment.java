package Space;

import java.awt.*;
import java.util.List;

/**
 * A single polygon for a section of the room.
 * @author i_wou_000
 *
 */
public class RoomFragment {

	private List<Vertex> vertices;
	public boolean IsInterior = true;
	
	private int x_min = Integer.MAX_VALUE;
	private int x_max = Integer.MIN_VALUE;
	private int y_min = Integer.MAX_VALUE;
	private int y_max = Integer.MIN_VALUE;
	
	public RoomFragment(List<Vertex> vertices) {
		this.vertices = vertices;
		this.calcBoundaryBox();
	}
	
	public List<Vertex> getVertices(){
		return vertices;
	}
	
	private void calcBoundaryBox() {
		for(PointDouble point : vertices) {
			if (point.x < x_min) x_min = (int) point.x;
			if (point.y < y_min) y_min = (int) point.y;
			if (point.x > x_max) x_max = (int) point.x;
			if (point.y > y_max) y_max = (int) point.y;
		}
	}
	
	public Rectangle getBoundaryBox() {
		return new Rectangle(x_min, y_min, x_max - x_min, y_max - y_min);
	}
}
