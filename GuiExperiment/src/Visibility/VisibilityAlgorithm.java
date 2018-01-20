package Visibility;

import Space.Vertex;

import java.awt.geom.Point2D;
import java.util.List;

public interface VisibilityAlgorithm {
    List<Point2D> compute(Point2D light, List<Point2D> polygon);
}
