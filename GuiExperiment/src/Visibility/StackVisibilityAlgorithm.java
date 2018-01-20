package Visibility;

import Space.Line;
import Space.Segment;
import Space.Utilities;
import Space.Vertex;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * A stack based algorithm.
 *
 * Works only when the first vertex is visible.
 * Some redundant conversion between Point2D and Vertex exist in this implementation,
 * to keep the interface more generalized.
 */
public class StackVisibilityAlgorithm implements VisibilityAlgorithm {

    @Override
    public List<Point2D> compute(Point2D light, List<Point2D> polygon) {
        List<Vertex> vertices = cloneVerices(polygon);
        Stack<Vertex> visiblePoints = new Stack<>();

        Vertex vertex = vertices.get(0);
        Vertex lastVisible;
        boolean isVisible;
        boolean stackReduced;
        boolean above = false;
        boolean done = false;
        while (true) {
            isVisible = false;
            lastVisible = (visiblePoints.size() > 0) ? visiblePoints.peek() : null;
            if (lastVisible == null) {
                visiblePoints.add(vertex);
                vertex = vertex.getNext();
                continue;
            }

            // once we're back at the start, we're done.
            for (int i = 0; i < Math.min(visiblePoints.size(), 3); i ++) {
                if (!visiblePoints.get(i).isIntermediate()) {
                    if (visiblePoints.get(i).equals(vertex)) {
                        done = true;
                        break;
                    }
                }
            }
            if (done) break;

            stackReduced = false;
            double vertexAngle = Utilities.computeAngle(visiblePoints.get(0), light, vertex);
            if (Utilities.computeAngle(visiblePoints.get(0), light, lastVisible) < vertexAngle) {
                isVisible = true;
                above = false;
            } else if ((Utilities.computeAngleZeroed(visiblePoints.get(visiblePoints.size() - 2), lastVisible, vertex) < 0 && lastVisible.equals(vertex.getPrevious())) || above) {
                if (vertexAngle + Utilities.computeAngle(lastVisible, light, vertex) > Math.PI * 2) {
                    visiblePoints.remove(0);
                    continue;
                }
                // point is in front of current stack.
                above = true;
                isVisible = true;
                while (Utilities.computeAngle(visiblePoints.get(0), light, lastVisible) >= vertexAngle) {
                    visiblePoints.pop();
                    stackReduced = true;
                    if (visiblePoints.size() <= 0) {
                        break;
                    }
                    lastVisible = visiblePoints.peek();
                }
                if (visiblePoints.size() <= 0) {
                    // entire stack wiped.
                    visiblePoints.add(vertex);
                    vertex = vertex.getNext();
                    continue;
                }
            }

            if (isVisible) {
                if (stackReduced) {
                    visiblePoints.push(createNextInnerVertex(vertex, lastVisible, light));
                } else if (!lastVisible.equals(vertex.getPrevious())) {
                    visiblePoints.push(createPreviousInnerVertex(lastVisible, vertex, light));
                }
                visiblePoints.push(vertex);
            }
            vertex = vertex.getNext();
        }

        List<Point2D> output = new ArrayList<>(visiblePoints.size());
        for (Vertex visiblePoint : visiblePoints) {
            output.add(new Point2D.Double(visiblePoint.getX(), visiblePoint.getY()));
        }

        return output;
    }

    private List<Vertex> cloneVerices(List<Point2D> vertices) {
        List<Vertex> list = new ArrayList<>(vertices.size());
        Vertex prev = null;
        for (Point2D v : vertices) {
            Vertex clone = new Vertex(v.getX(), v.getY());
            clone.setPrevious(prev);
            prev = clone;
            list.add(clone);
        }
        list.get(0).setPrevious(prev);
        return list;
    }

    /**
     * Calculates the new intersection vertex for when the far vertex is visible but the next vertex is not
     */
    private Vertex createNextInnerVertex(Vertex occluder, Vertex far, Point2D light) {
        Line line = new Line(occluder, new Vertex(light));
        Segment segment = new Segment(far, far.getNext());

        Vertex intersection = line.intersect(segment);
        intersection.setIntermediate(true);
        intersection.setPrevious(segment.getStartVertex());
        segment.getEndVertex().setPrevious(intersection);

        return intersection;
    }

    /**
     * Calculates the new intersection vertex for when the far vertex is no longer visible but the previous vertex was
     */
    private Vertex createPreviousInnerVertex(Vertex occluder, Vertex far, Point2D light) {
        Line line = new Line(occluder, new Vertex(light));
        Segment segment = new Segment(far.getPrevious(), far);

        Vertex intersection = line.intersect(segment);
        intersection.setIntermediate(true);
        intersection.setPrevious(segment.getStartVertex());
        segment.getEndVertex().setPrevious(intersection);

        return intersection;
    }

//    /**
//     * Calculate the point p that intersects segment a--b at (global) angle `angle` from light.
//     *
//     *     a(*)-----------------(*)light
//     *  ap -> \ beta      __--//
//     *         \alpha _-''  _'
//     *        p(*)-''    _'
//     *           \     _'
//     *            \ _'
//     *           b(*)
//     *
//     * t = ap / ab = weight of b, 1-weight of a
//     */
//    private Point2D getPointFromSegmentAtAngle(Point2D light, Point2D a, Point2D b, double angle) {
//        angle -= Utilities.computeAngleTo(light, a);
//        double beta = Utilities.computeAngle(b, a, light);
//        double alpha = Math.PI - beta - angle;
//        double ap = (light.distance(a) / Math.sin(alpha)) * Math.sin(angle);
//        double t = ap / a.distance(b);
//        return new Point2D.Double(
//                a.getX() * (1 - t) + b.getX() * t,
//                a.getY() * (1 - t) + b.getY() * t
//        );
//    }
//
//    private boolean isVisibleWRT(Vertex current, Vertex lastVisible, Point2D light, boolean CW) {
//        double angle = Utilities.computeAngle(lastVisible, light, current);
//
//        if (angle < Math.PI) {
//            return true;
//        } else if (!CW) {
//            double edgeAngle = Utilities.computeAngle(light, lastVisible, lastVisible.getPrevious());
//            double newAngle = Utilities.computeAngle(light, lastVisible, current);
//
//            if (edgeAngle > newAngle) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    private boolean isCCWWRT(Vertex a, Vertex b, Point2D light) {
//        return Utilities.computeAngle(b, light, a) < Math.PI;
//    }
}
