package Space;

import java.awt.geom.Point2D;

public class Utilities {
    public static double computeAngle(Point2D A, Point2D Origin, Point2D B) {
        double angleA = Math.atan2(A.getX() - Origin.getX(), A.getY() - Origin.getY());
        double angleB = Math.atan2(B.getX() - Origin.getX(), B.getY() - Origin.getY());
        double result = angleB - angleA;

        if (result < 0) {
            result += 2*Math.PI;
        }

        return result;
    }
    
    public static double computeInternalAngle(Vertex Source) {
    	double result = computeAngle(Source.getPrevious(), Source, Source.getNext());
    	
    	//System.out.println((result / (2*Math.PI) * 360) + " rad");
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
		if (A.y == B.y && A.x > B.x) return true;
		return false;
	}
	
	
	
	public static PointType computePointType(Vertex Vertex) {
		if (Vertex == null)
			return PointType.REGULARVERTEX;
		
		double angle =  Utilities.computeInternalAngle(Vertex);
		
		//System.out.println(Vertex.toString());
		
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