package Space;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

/**
 * Source:
 * https://martin-thoma.com/how-to-check-if-two-line-segments-intersect/
 */

public class LineIntersect {
	
	public static boolean doLinesIntersect(Point2D a_start, Point2D a_end, Point2D b_start, Point2D b_end) {
		return doLinesIntersect(new LineSegment(a_start, a_end), new LineSegment(b_start, b_end));
	}
	
	public static boolean doLinesIntersect(LineSegment a, LineSegment b) {
	    Rectangle box1 = a.getBoundingBox();
	    Rectangle box2 = b.getBoundingBox();
	    
	    boolean BoundingBoxes = doBoundingBoxesIntersect(box1, box2);
	    boolean segmentCrossAB = lineSegmentTouchesOrCrossesLine(a, b);
	    boolean segmentCrossBA = lineSegmentTouchesOrCrossesLine(b, a);
	    
	    /*
	    if (BoundingBoxes)
	    	System.out.println("Boundingbox intersected");
	    if (segmentCrossAB)
	    	System.out.println("AB intersected");
	    if (segmentCrossBA)
	    	System.out.println("BA intersected");
	    */
	    
	    return BoundingBoxes && segmentCrossAB && segmentCrossBA;
	}
	
	/**
	 * Check if bounding boxes do intersect. If one bounding box
	 * touches the other, they do intersect.
	 * @param a first bounding box
	 * @param b second bounding box
	 * @return <code>true</code> if they intersect,
	 *         <code>false</code> otherwise.
	 */
	private static boolean doBoundingBoxesIntersect(Rectangle a, Rectangle b) {
	    return a.getMinX() <= b.getMaxX() 
	        && a.getMaxX() >= b.getMinX() 
	        && a.getMinY() <= b.getMaxY()
	        && a.getMaxY() >= b.getMinY();
	}
	
	private static boolean lineSegmentTouchesOrCrossesLine(LineSegment a,
	        LineSegment b) {
	    return isPointOnLine(a, b.first)
	            || isPointOnLine(a, b.second)
	            || (isPointRightOfLine(a, b.first) ^ 
	                isPointRightOfLine(a, b.second));
	}
	
	/**
	 * Checks if a point is right of a line. If the point is on the
	 * line, it is not right of the line.
	 * @param a line segment interpreted as a line
	 * @param b the point
	 * @return <code>true</code> if the point is right of the line,
	 *         <code>false</code> otherwise
	 */
	private static boolean isPointRightOfLine(LineSegment a, Point2D b) {
	    // Move the image, so that a.first is on (0|0)
	    LineSegment aTmp = new LineSegment(new PointDouble(0, 0), new PointDouble(
	            a.second.getX() - a.first.getX(), a.second.getY() - a.first.getY()));
	    PointDouble bTmp = new PointDouble(b.getX() - a.first.getX(), b.getY() - a.first.getY());
	    return crossProduct(aTmp.second, bTmp) < 0;
	}
	
	/**
	 * Checks if a Point is on a line
	 * @param a line (interpreted as line, although given as line
	 *                segment)
	 * @param b point
	 * @return <code>true</code> if point is on line, otherwise
	 *         <code>false</code>
	 */
	private static boolean isPointOnLine(LineSegment a, Point2D b) {
	    // Move the image, so that a.first is on (0|0)
	    LineSegment aTmp = new LineSegment(new PointDouble(0, 0), new PointDouble(
	            a.second.getX() - a.first.getX(), a.second.getY() - a.first.getY()));
	    PointDouble bTmp = new PointDouble(b.getX() - a.first.getX(), b.getY() - a.first.getY());
	    double r = crossProduct(aTmp.second, bTmp);
	    return Math.abs(r) < 0.000001;
	}
	
	private static class LineSegment{
		Point2D first;
		Point2D second;
		
		public LineSegment(Point2D a, Point2D b) {
			this.first = a;
			this.second  = b;
		}
		
		public Rectangle getBoundingBox() {
			return new Rectangle(
					(int)Math.min(first.getX(), second.getX()),
					(int)Math.min(first.getY(), second.getY()),
					(int)Math.max(first.getX(), second.getX()),
					(int)Math.max(first.getY(), second.getY()));
		}
	}
	

    /**
     * Calculate the cross product of two points.
     * @param a first point
     * @param b second point
     * @return the value of the cross product
     */
    private static double crossProduct(Point2D a, Point2D b) {
        return a.getX() * b.getY() - b.getX() * a.getY();
    }
	
	
}
