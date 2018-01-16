package Space;

import com.sun.xml.internal.ws.developer.Serialization;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class Lights {

    public final static Color POINT_COLOR = Color.BLUE;
    public final static Color REGION_COLOR = new Color(211, 202, 188);

    private List<Point2D> lights = new ArrayList<>();
    private List<List<Vertex>> visibilityRegions = new ArrayList<>();
    private Room room;

    private List<Point2D> temp = new ArrayList<>();

    private List<UpdateEvent> listeners = new ArrayList<>();

    public Lights(Room room) {
        this.room = room;
    }

    public void calculateVisibilityRegions() {
        visibilityRegions.clear();

        for (Point2D light : lights) {
            visibilityRegions.add(calculateVisibilityRegion(light));
        }

        this.onUpdate();
    }

    private List<Vertex> calculateVisibilityRegion(Point2D light) {
        LinkedList<Vertex> visiblePoints = new LinkedList<>();

        visiblePoints.push(new Vertex(room.getVertices().get(0)));

        boolean previousCutOff = false;
        int i = 1;
        boolean fullIteration = false;

        while (!fullIteration) {
            fullIteration = visiblePoints.getLast().equals(room.getVertices().get(i)) || (visiblePoints.size() >= 2 && visiblePoints.get(visiblePoints.size() - 2).equals(room.getVertices().get(i)));

            boolean nextCutOff = false;
            Vertex current = new Vertex(room.getVertices().get(i));
            Vertex previous = new Vertex(visiblePoints.peek());

            if (isVisibleWRT(current, previous, light)) {
                while (visiblePoints.size() >= 1 && isCCWWRT(previous, current, light)) {
                    visiblePoints.pop();
                    nextCutOff = true;
                    previous = visiblePoints.peek();
                }
            } else {
                if (fullIteration) {
                    visiblePoints.removeLast();
                    fullIteration = false;
                }
                previousCutOff = true;
            }

            if (previous != null) {
                if (isVisibleWRT(current, previous, light)) {
                    if (previousCutOff) {
                        visiblePoints.push(createPreviousInnerVertex(previous, current, light));
                    }
                    if (nextCutOff) {
                        visiblePoints.push(createNextInnerVertex(current, previous, light));
                    }
                    if (!fullIteration) {
                        visiblePoints.push(current);
                    }
                    previousCutOff = false;
                }
            } else {
                visiblePoints.push(current);
                previousCutOff = false;
            }

            if (i < room.getVertices().size() - 1) {
                i++;
            } else {
                i = 0;
            }
        }

        return createPolygon(visiblePoints);
    }

    private boolean isVisibleWRT(Vertex a, Vertex b, Point2D p) {
        double angle = Utilities.computeAngle(b, p, a);

        if (angle < Math.PI) {
            return true;
        } else {
            double edgeAngle = Utilities.computeAngle(p, b, b.getPrevious());
            double newAngle = Utilities.computeAngle(p, b, a);

            if (edgeAngle >= newAngle) {
                return true;
            }
        }

        return false;
    }

    private boolean isCCWWRT(Vertex a, Vertex b, Point2D p) {
        return Utilities.computeAngle(b, p, a) < Math.PI;
    }

    private Vertex createNextInnerVertex(Vertex current, Vertex previous, Point2D light) {
        Line line = new Line(current, new Vertex(light));
        Segment segment = new Segment(previous, previous.getNext());

        Vertex intersection = line.intersect(segment);

        intersection.setPrevious(segment.getStartVertex());
        segment.getEndVertex().setPrevious(intersection);

        return intersection;
    }

    private Vertex createPreviousInnerVertex(Vertex current, Vertex previous, Point2D light) {
        Line line = new Line(current, new Vertex(light));
        Segment segment = new Segment(previous.getPrevious(), previous);

        Vertex intersection = line.intersect(segment);

        intersection.setPrevious(segment.getStartVertex());
        segment.getEndVertex().setPrevious(intersection);

        return intersection;
    }

    private List<Vertex> createPolygon(List<Vertex> vertices) {
        List<Vertex> polygon = new ArrayList<>();

        Iterator<Vertex> iterator = vertices.iterator();

        Vertex current = iterator.next();
        Vertex previous;
        polygon.add(new Vertex(current.x, current.y, true));

        while (iterator.hasNext()) {
            previous = current;
            current = iterator.next();

            polygon.add(new Vertex(current.x, current.y, previous));
        }

        return polygon;
    }

    public List<Point2D> getTemp() {
        return temp;
    }

    public void addLight(Point2D light) {
        lights.add(light);

        this.onUpdate();
    }

    public List<Point2D> getLights() {
        return lights;
    }

    public void setLights(List<Point2D> lights) { this.lights = lights; }

    public void clear() {
        lights.clear();
        visibilityRegions.clear();
    }

    public List<List<Vertex>> getVisibilityRegions() {
        return visibilityRegions;
    }

    public void addListener(UpdateEvent toAdd) {
        listeners.add(toAdd);
    }

    public void onUpdate() {
        // Notify everybody that may be interested.
        for (UpdateEvent uE : listeners) {
            uE.onUpdate();
        }
    }
}