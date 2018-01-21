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
	    	System.out.println("BA intersected");*/
	    
	    
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
	    return isPointOnLine(a, b.startPoint)
	            || isPointOnLine(a, b.endPoint)
	            || (isPointRightOfLine(a, b.startPoint) ^ 
	                isPointRightOfLine(a, b.endPoint));
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
	            a.endPoint.getX() - a.startPoint.getX(), a.endPoint.getY() - a.startPoint.getY()));
	    PointDouble bTmp = new PointDouble(b.getX() - a.startPoint.getX(), b.getY() - a.startPoint.getY());
	    return crossProduct(aTmp.endPoint, bTmp) < 0;
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
	            a.endPoint.getX() - a.startPoint.getX(), a.endPoint.getY() - a.startPoint.getY()));
	    PointDouble bTmp = new PointDouble(b.getX() - a.startPoint.getX(), b.getY() - a.startPoint.getY());
	    double r = crossProduct(aTmp.endPoint, bTmp);
	    return Math.abs(r) < 0.000001;
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
	
	public static Point2D getIntersectionPoint(LineSegment a, LineSegment b) {
		Point2D workPoint = checkVerticalIntersect(a, b);
		if (workPoint != null)
			return workPoint;
		
		return checkSingleIntersectionPoint(a, b);		
	}
	
	private static Point2D checkVerticalIntersect(LineSegment A, LineSegment B) {
		if (A.startPoint.getX() == A.endPoint.getX()) {
			// Line A is a vertical line
			
			if (B.startPoint.getX() == B.endPoint.getX()) {
				// B is also vertical
				if (A.startPoint.getX() == B.startPoint.getX()) {
					// both lines are at the same x-coordinate
					return getIntersectionFromParallel(A, B);
				} else {
					System.out.println("Intersection run down on two non-touching vertical lines. Something went wrong in the code");
					return null;
				}
			}
				// A is vertical, B is not.
			double fraction = (A.startPoint.getX() - B.endPoint.getX()) / (B.startPoint.getX() - B.endPoint.getX());
			return new PointDouble(
					B.endPoint.getX() + (B.startPoint.getX() - B.endPoint.getX()) * fraction,
					B.endPoint.getY() + (B.startPoint.getY() - B.endPoint.getY()) * fraction);
		} else if (B.startPoint.getX() == B.endPoint.getX()) {
				// B is vertical, A is not
			double fraction = (B.startPoint.getX() - A.endPoint.getX()) / (A.startPoint.getX() - A.endPoint.getX());
			return new PointDouble(
					A.endPoint.getX() + (A.startPoint.getX() - A.endPoint.getX()) * fraction,
					A.endPoint.getY() + (A.startPoint.getY() - A.endPoint.getY()) * fraction);
		}
		return null;
	}
	
	private static Point2D checkSingleIntersectionPoint(LineSegment A, LineSegment B) {
		double[] a = getFormula(A);
		double[] b = getFormula(B);
		
		double x = (b[1] - a[1]) / (a[0] - b[0]);
		
		if (Math.abs(a[0] - b[0]) < 0.001) { // The angles are the same
			return getIntersectionFromParallel(A, B);
		} else {
			return new PointDouble(x, a[1] + a[0] * x);
		}
	}
	

	private static double[] getFormula(LineSegment Segment) {
		double a = (Segment.startPoint.getY() - Segment.endPoint.getY()) / (Segment.startPoint.getX() - Segment.endPoint.getX());
		double b = Segment.startPoint.getY() - a*Segment.startPoint.getX();
				
	    double[] result =  {a, b};
	    return result;
	}
	
	/**
	 * Assumes two lines as parallel and returns the lowest point of the vertex with the highest lowest point.
	 * @param A
	 * @param B
	 * @return
	 */
	private static Point2D getIntersectionFromParallel(LineSegment A, LineSegment B)
	{
		// Assume two lines are parallel at this point, return the highest end point (relative) of the two lines
		Point2D Alow = Utilities.isBelow(A.startPoint, A.endPoint) ? A.startPoint : A.endPoint;
		Point2D Blow = Utilities.isBelow(B.startPoint, B.endPoint) ? B.startPoint : B.endPoint;
		return Utilities.isBelow(Alow, Blow) ? Blow : Alow;
		
	}
}
