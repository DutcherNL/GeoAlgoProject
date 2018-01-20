package Visibility;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * Outputs input. To illustrate how to use different algorithms.
 */
public class NullVisiblityAlgorithm implements VisibilityAlgorithm {
    @Override
    public List<Point2D> compute(Point2D light, List<Point2D> polygon) {
        return polygon;
    }
}
