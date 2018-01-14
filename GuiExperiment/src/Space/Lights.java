package Space;

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

    private List<UpdateEvent> listeners = new ArrayList<>();

    public Lights(Room room) {
        this.room = room;
    }

    public void calculateVisibilityRegions() {
        visibilityRegions.clear();

        for (Point2D light : lights) {
            System.out.println(calculateVisibilityRegion(light));
        }

        // TODO: visibilityRegions.add(new ArrayList<>(room.getVertices()));

        this.onUpdate();
    }

    private List<Vertex> calculateVisibilityRegion(Point2D light) {
        LinkedList<Vertex> visiblePoints = new LinkedList<>();
        Iterator<Vertex> roomPoints = room.getVertices().iterator();

        visiblePoints.push(roomPoints.next());

        while (roomPoints.hasNext()) {
            Vertex current = roomPoints.next();
            Vertex previous = visiblePoints.peek();

            isVisibleWRT(current, previous, light);
            visiblePoints.push(current);

            /*if (isVisibleWRT(current, previous, light)) {
                while (isVisibleWRT(previous, current, light)) {
                    visiblePoints.pop();
                    previous = visiblePoints.peek();
                }
            }

            if (isVisibleWRT(current, previous, light)) {
                visiblePoints.push(current);
            }*/
        }

        return visiblePoints;
    }

    private boolean isVisibleWRT(Vertex a, Vertex b, Point2D p) {
        double angle = Utilities.computeAngle(b, p, a);

        if (angle < Math.PI) {
            return true;
        } else {
            double edgeAngle = Utilities.computeAngle(p, b, b.getPrevious());
            double newAngle = Utilities.computeAngle(p, b, a);

            System.out.println(edgeAngle + " vs " + newAngle);
//
//            if () {
//
//            }
        }

        return new Random().nextBoolean();
    }

    public void addLight(Point2D light) {
        lights.add(light);

        this.onUpdate();
    }

    public List<Point2D> getLights() {
        return lights;
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