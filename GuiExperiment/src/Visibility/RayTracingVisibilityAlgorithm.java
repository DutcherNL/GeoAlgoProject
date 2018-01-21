package Visibility;

import Space.Utilities;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class RayTracingVisibilityAlgorithm implements VisibilityAlgorithm {

    private List<Event> events;

    private class Edge {
        Point2D start;
        Point2D end;
        Edge(Point2D start, Point2D end) {
            this.start = start;
            this.end = end;
        }
        Point2D getPoint(Point2D light, double angle) {
            angle -= Utilities.computeAngleTo(light, start);
            double beta = Utilities.computeAngle(end, start, light);
            double alpha = Math.PI - beta - angle;
            double ap = (light.distance(start) / Math.sin(alpha)) * Math.sin(angle);
            double t = Math.min(1, Math.max(0, ap / start.distance(end)));
            return new Point2D.Double(
                    start.getX() * (1 - t) + end.getX() * t,
                    start.getY() * (1 - t) + end.getY() * t
            );
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "" + start +
                    "--" + end +
                    '}';
        }
    }
    private abstract class Event {
        Edge edge;
        double angle;
        double distance;
        Event(Edge edge, double angle, double distance) {
            this.edge = edge;
            this.angle = angle;
            this.distance = distance;
        }
    }
    private class EdgeStartEvent extends Event {
        EdgeStartEvent(Edge edge, Point2D light) {
            super(
                    edge,
                    (edge.start.equals(light)) ?
                            Utilities.computeAngleTo(edge.end, light) :
                            Utilities.computeAngleTo(edge.start, light),
                    edge.start.distance(light)
            );
        }
    }
    private class EdgeEndEvent extends Event {
        EdgeEndEvent(Edge edge, Point2D light) {
            super(
                    edge,
                    (edge.end.equals(light)) ?
                            Utilities.computeAngleTo(edge.start, light) :
                            Utilities.computeAngleTo(edge.end, light),
                    edge.end.distance(light)
            );
        }
    }

    @Override
    public List<Point2D> compute(Point2D light, List<Point2D> polygon) {
        setup(light, polygon);

        PriorityQueue<Edge> edgeHeap = new PriorityQueue<>((a, b) -> compareEdges(a, b, light));
        List<Point2D> output = new LinkedList<>();

        Edge closestEdge = null;

        for (int pass = 0; pass < 2; pass++) {
            for (Event event : events) {
                if (event instanceof EdgeStartEvent) {
                    edgeHeap.add(event.edge);
                    if (event.edge.equals(edgeHeap.peek())) {
                        // new edge is closest
                        if (closestEdge != null && output.size() > 0 && !closestEdge.end.equals(event.edge.start)) {
                            output.add(closestEdge.getPoint(light, event.angle));
                        }
                        if (pass > 0 && output.get(0).equals(event.edge.start) && !event.edge.start.equals(light)) {
                            break;
                        }
                        output.add(event.edge.start);
                    }
                    closestEdge = edgeHeap.peek();
                }
                if (event instanceof EdgeEndEvent) {
                    if (!edgeHeap.remove(event.edge)) {
                        // remove nonexistent edge? => we'll use it next pass.
                        // for now, remove all visible points, we'll add those next pass as well
                        output.clear();
                        continue;
                    }

                    if (event.edge.equals(closestEdge)) {
                        if (pass > 0 && output.get(0).equals(event.edge.end) && !event.edge.end.equals(light)) {
                            break;
                        }
                        output.add(event.edge.end);
                        closestEdge = edgeHeap.peek();
                        if (closestEdge != null && !closestEdge.start.equals(event.edge.end)) {
                            output.add(closestEdge.getPoint(light, event.angle));
                        }
                    }
                }
            }
        }

        // improve robustness by removing consecutive points which are very close to each other
        for (int i = 1; i < output.size(); i++) {
            if (output.get(i).distance(output.get(i-1)) < 1e-9) {
                output.remove(i);
                i -= 1;
            }
        }

        return output;
    }

    /**
     * Returns which edge is closer to the light. 1 for a, -1 for b
     * It is given the edges do not intersect.
     */
    private int compareEdges(Edge a, Edge b, Point2D light) {
        Point2D intervalStart = a.start;
        Point2D intervalEnd = a.end;
        if (Utilities.computeAngleZeroed(a.start, light, b.start) > 0) {
            intervalStart = b.start;
        }
        if (Utilities.computeAngleZeroed(a.end, light, b.end) <= 0) {
            intervalEnd = b.end;
        }

        double angle = Utilities.computeAngleTo(intervalStart, light) +
                Utilities.computeAngleZeroed(intervalStart, light, intervalEnd) / 2;
        double aDist = a.getPoint(light, angle).distance(light);
        double bDist = b.getPoint(light, angle).distance(light);

        if (Math.abs(aDist - bDist) < 1e-9) {
            return 0;
        }
        return (int) Math.signum(aDist - bDist);
    }

    private void setup(Point2D light, List<Point2D> polygon) {
        // list of edges
        List<Edge> edges = new ArrayList<>(polygon.size());
        Point2D prev = null;
        for (Point2D vertex : polygon) {
            if (prev != null) {
                edges.add(new Edge(prev, vertex));
            }
            prev = vertex;
        }
        edges.add(new Edge(prev, polygon.get(0)));

        // list of radial sweep events sorted by occurrence angle.
        events = new ArrayList<>();
        for (Edge edge : edges) {
            if (Utilities.computeAngleZeroed(edge.start, light, edge.end) < 0) {
                Point2D t = edge.start;
                edge.start = edge.end;
                edge.end = t;
            }
            events.add(new EdgeStartEvent(edge, light));
            events.add(new EdgeEndEvent(edge, light));
        }
        events.sort((a, b) -> {
            int compare = (int) Math.signum(a.angle - b.angle);
            if (compare == 0) {
                // start before end
                if (a instanceof EdgeStartEvent ^ b instanceof EdgeStartEvent) {
                    return (a instanceof EdgeStartEvent) ? -1 : 1;
                }
                return (int) Math.signum(b.distance - a.distance);
            }
            return compare;
        });
    }
}
