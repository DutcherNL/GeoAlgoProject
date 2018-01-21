package Visibility;

import Space.Utilities;

import javax.rmi.CORBA.Util;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import static Space.Utilities.computeAngle;

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
            double t = ap / start.distance(end);
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
        EdgeStartEvent(Edge edge, Point2D start, Point2D light) {
            super(edge, Utilities.computeAngleTo(start, light), start.distance(light));
        }
    }
    private class EdgeEndEvent extends Event {
        EdgeEndEvent(Edge edge, Point2D end, Point2D light) {
            super(edge, Utilities.computeAngleTo(end, light), end.distance(light));
        }
    }

    @Override
    public List<Point2D> compute(Point2D light, List<Point2D> polygon) {
        setup(light, polygon);

        PriorityQueue<Edge> edgeHeap = new PriorityQueue<>((a, b) -> compareEdges(a, b, light));
        List<Point2D> output = new LinkedList<>();

        Edge closestEdge = null;

        while (events.size() > 0) {
            Event event = events.remove(0);

            if (event instanceof EdgeStartEvent) {
                System.out.println("Start of " + event.edge);
                edgeHeap.add(event.edge);
                if (event.edge.equals(edgeHeap.peek())) {
                    // new edge is closest
                    if (closestEdge != null && !closestEdge.end.equals(event.edge.start)) {
                        output.add(closestEdge.getPoint(light, event.angle));
                    }
                    output.add(event.edge.start);
                }
                closestEdge = edgeHeap.peek();
            }

            if (event instanceof EdgeEndEvent) {
                if (!edgeHeap.remove(event.edge)) {
                    // remove nonexistent edge? => add event back in since we apparently still need to find the start
                    events.add(event);
                    continue;
                }

                System.out.println("End of " + event.edge);
                if (event.edge.equals(closestEdge)) {
                    output.add(event.edge.end);
                    closestEdge = edgeHeap.peek();
                    if (closestEdge != null) {
                        output.add(closestEdge.getPoint(light, event.angle));
                    }
                }
            }
        }

        output.remove(output.size() - 1); // remove duplicate end point.

        for (Point2D point2D : output) {
            System.out.println(point2D);
        }

        return output;
    }

    /**
     * Returns which edge is closer to the light. 1 for a, -1 for b
     * It is given the edges do not intersect.
     */
    private int compareEdges(Edge a, Edge b, Point2D light) {
//        Point2D comparePoint = a.start;
//        if (Utilities.computeAngleZeroed(a.start, light, b.start) > 0) {
//            comparePoint = b.start;
//        }
//
//        double angle = Utilities.computeAngleTo(comparePoint, light);
//
//        return (a.getPoint(light, angle).distance(light) < b.getPoint(light, angle).distance(light)) ? -1 : 1;

        // TODO: fix in case edges intersect at endpoints
        return 1;
    }

    private void setup(Point2D light, List<Point2D> polygon) {
        // list of edges, to be sorted by starting angle and distance.
        List<Edge> edges = new ArrayList<>(polygon.size());
        Point2D prev = null;
        for (Point2D vertex : polygon) {
            if (prev != null) {
                edges.add(new Edge(prev, vertex));
            }
            prev = vertex;
        }
        edges.add(new Edge(prev, polygon.get(0)));
        edges.sort((a, b) -> (int) Math.signum(Utilities.computeAngleZeroed(b.start, light, a.start)));

        // list of radial sweep events sorted by occurrence angle.
        events = new ArrayList<>();
        for (Edge edge : edges) {
            if (Utilities.computeAngleZeroed(edge.start, light, edge.end) < 0) {
                Point2D t = edge.start;
                edge.start = edge.end;
                edge.end = t;
            }
            events.add(new EdgeStartEvent(edge, edge.start, light));
            events.add(new EdgeEndEvent(edge, edge.end, light));
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
