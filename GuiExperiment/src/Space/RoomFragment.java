package Space;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

/**
 * A single polygon for a section of the room.
 * @author i_wou_000
 *
 */
public class RoomFragment {

	private List<Point> points;
	public boolean IsInterior = true;
	
	private int x_min = Integer.MAX_VALUE;
	private int x_max = Integer.MIN_VALUE;
	private int y_min = Integer.MAX_VALUE;
	private int y_max = Integer.MIN_VALUE;
	
	public RoomFragment(List<Point> Points) {
		this.points = Points;
		this.calcBoundaryBox();
	}
	
	public List<Point> getPoints(){
		return points;
	}
	
	private void calcBoundaryBox() {
		for(Point point : points) {
			if (point.x < x_min) x_min = point.x;
			if (point.y < y_min) y_min = point.y;
			if (point.x > x_max) x_max = point.x;
			if (point.y > y_max) y_max = point.y;
		}
	}
	
	public Rectangle getBoundaryBox() {
		return new Rectangle(x_min, y_min, x_max - x_min, y_max - y_min);
	}
}
