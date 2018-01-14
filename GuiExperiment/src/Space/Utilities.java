package Space;

import java.awt.*;

public class Utilities {
    public static double computeAngle(Point A, Point Origin, Point B) {
        double angleA = Math.atan2(A.x - Origin.x, A.y - Origin.y);
        double angleB = Math.atan2(B.x - Origin.x, B.y - Origin.y);
        double result = angleB - angleA;

        if (result < 0) {
            result += 2*Math.PI;
        }

        return result;
    }
    
    public static double computeInternalAngle(Vertex Source) {
    	return computeAngle(Source.getNext(), Source, Source.getPrevious());
    }
    
    /**
	 * Compute if A is below B
	 * @param A
	 * @param B
	 * @return
	 */
	public static boolean isBelow(Point A, Point B) {
		if (A.y < B.y) return true;
		if (A.y == B.y && A.x < B.x) return true;
		return false;
	}
	
	
	
	public static PointType computePointType(Vertex Vertex) {
		double angle;
		
		System.out.println(Vertex.toString());
		
		if (Utilities.isBelow(Vertex.getPrevious(),Vertex) &&
			Utilities.isBelow(Vertex.getNext(), Vertex)) {
			if ((angle = Utilities.computeInternalAngle(Vertex)) < Math.PI)
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