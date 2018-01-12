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
}