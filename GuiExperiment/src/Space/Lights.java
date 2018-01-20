package Space;

import javafx.util.Pair;

import javax.rmi.CORBA.Util;
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
        List<Vertex> vertices = cloneVerices(room.getVertices());
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

        for (Vertex visiblePoint : visiblePoints) {
//            System.out.println(visiblePoint);
        }


        return createPolygon(visiblePoints);
    }

    /**
     * Provides a clone of the list of vertices, with updated next/prev relationships.
     * To prevent destroying the source data.
     */
    private List<Vertex> cloneVerices(List<Vertex> vertices) {
        List<Vertex> list = new ArrayList<>(vertices.size());
        Vertex prev = null;
        for (Vertex v : vertices) {
            Vertex clone = new Vertex(v.getX(), v.getY());
            clone.setPrevious(prev);
            prev = clone;
            list.add(clone);
        }
        list.get(0).setPrevious(list.get(vertices.size()-1));
        return list;
    }

    /**
     * Calculate the point p that intersects segment a--b at (global) angle `angle` from light.
     *
     *     a(*)-----------------(*)light
     *  ap -> \ beta      __--//
     *         \alpha _-''  _'
     *        p(*)-''    _'
     *           \     _'
     *            \ _'
     *           b(*)
     *
     * t = ap / ab = weight of b, 1-weight of a
     */
    private Point2D getPointFromSegmentAtAngle(Point2D light, Point2D a, Point2D b, double angle) {
        angle -= Utilities.computeAngleTo(light, a);
        double beta = Utilities.computeAngle(b, a, light);
        double alpha = Math.PI - beta - angle;
        double ap = (light.distance(a) / Math.sin(alpha)) * Math.sin(angle);
        double t = ap / a.distance(b);
        return new Point2D.Double(
                a.getX() * (1 - t) + b.getX() * t,
                a.getY() * (1 - t) + b.getY() * t
        );
    }

    private boolean isVisibleWRT(Vertex current, Vertex lastVisible, Point2D light, boolean CW) {
        double angle = Utilities.computeAngle(lastVisible, light, current);

        if (angle < Math.PI) {
            return true;
        } else if (!CW) {
            double edgeAngle = Utilities.computeAngle(light, lastVisible, lastVisible.getPrevious());
            double newAngle = Utilities.computeAngle(light, lastVisible, current);

            if (edgeAngle > newAngle) {
                return true;
            }
        }

        return false;
    }

    private boolean isCCWWRT(Vertex a, Vertex b, Point2D light) {
        return Utilities.computeAngle(b, light, a) < Math.PI;
    }

    /*
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

    /*
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

    public void setLights(List<Point2D> lights) {
        this.lights = lights;
    }

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