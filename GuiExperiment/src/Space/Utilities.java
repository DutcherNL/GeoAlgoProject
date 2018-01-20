package Space;

import sun.nio.cs.ext.MacThai;

import java.awt.geom.Point2D;

public class Utilities {
    public static double computeAngle(Point2D A, Point2D Origin, Point2D B) {
    	// remember children: Y comes first, X comes second.
		double angleA = Math.atan2(A.getY() - Origin.getY(), A.getX() - Origin.getX());
        double angleB = Math.atan2(B.getY() - Origin.getY(), B.getX() - Origin.getX());
        double result = angleB - angleA;

        if (result < 0) {
            result += 2*Math.PI;
        }

		return result;
    }

	public static double computeAngleZeroed(Point2D A, Point2D Origin, Point2D B) {
		double angle = Math.atan2(B.getY() - Origin.getY(), B.getX() - Origin.getX())
				- Math.atan2(A.getY() - Origin.getY(), A.getX() - Origin.getX());

		if (angle < -Math.PI) {
			angle += 2 * Math.PI;
		}
		if (angle > Math.PI) {
			angle -= 2 * Math.PI;
		}

		return angle;
	}

	public static double computeAngleTo(Point2D point, Point2D offset) {
    	double angle = Math.atan2(point.getY() - offset.getY(), point.getX() - offset.getX());
    	if (angle < 0) {
    		return angle + 2*Math.PI;
		}
		return angle;
	}

    public static Point2D intersectLines(Point2D a1, Point2D a2, Point2D b1, Point2D b2) {
		double A1 = a2.getY() - a1.getY();
		double B1 = a2.getX() - a1.getX();
		double C1 = A1 * a1.getX() + B1 * a1.getY();

		double A2 = b2.getY() - b1.getY();
		double B2 = b2.getX() - b1.getX();
		double C2 = A1 * b1.getX() + B1 * b1.getY();

		double det = A1 * B2 - A2 * B1;
		if (det == 0) {
			// lines are parallel; return the end segment point
			return a2;
		}

		return new Point2D.Double(
				(B2*C1 - B1*C2) / det,
				(A1*C2 - A2*C1) / det
		);
	}

    public static double computeInternalAngle(Vertex Source) {
    	double result = computeAngle(Source.getNext(), Source, Source.getPrevious());

    	System.out.println((result / (2*Math.PI) * 360) + " rad");
    	return result;
    }

    /**
	 * Compute if A is below B
	 * @param A
	 * @param B
	 * @return
	 */
	public static boolean isBelow(PointDouble A, PointDouble B) {
		if (A == null) return true;
		if (B == null) return false;

		if (A.y < B.y) return true;
		if (A.y == B.y && A.x < B.x) return true;
		return false;
	}



	public static PointType computePointType(Vertex Vertex) {
		double angle =  Utilities.computeInternalAngle(Vertex);

		System.out.println(Vertex.toString());

		if (Utilities.isBelow(Vertex.getPrevious(),Vertex) &&
			Utilities.isBelow(Vertex.getNext(), Vertex)) {
			if ((angle) < Math.PI)
				return PointType.STARTVERTEX;
			else if (angle > Math.PI)
				return PointType.SPLITVERTEX;
		}
		if (Utilities.isBelow(Vertex.getPrevious(),Vertex) &&
				Utilities.isBelow(Vertex.getNext(), Vertex)) {
				if ((angle = Utilities.computeInternalAngle(Vertex)) < Math.PI)
					return PointType.ENDVERTEX;
				else if (angle > Math.PI)
					return PointType.MERGEVERTEX;
		}
		return PointType.REGULARVERTEX;
}
}