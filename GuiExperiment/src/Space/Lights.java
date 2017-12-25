package Space;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Lights {

    public final static Color POINT_COLOR = Color.BLUE;
    public final static Color REGION_COLOR = new Color(211, 202, 188);

    private List<Point> lights = new ArrayList<>();
    private List<List<Point>> visibilityRegions = new ArrayList<>();
    private Room room;

    private List<UpdateEvent> listeners = new ArrayList<>();

    public Lights(Room room) {
        this.room = room;
    }

    public void calculateVisibilityRegions() {
        visibilityRegions.clear();

        // TODO Sort room polygon in CCW order

        for (Point light : lights) {
            visibilityRegions.add(calculateVisibilityRegion(light));
        }

        visibilityRegions.add(new ArrayList<>(room.getPoints()));

        this.onUpdate();
    }

    private List<Point> calculateVisibilityRegion(Point light) {
        Stack<Point> visiblePoints = new Stack<>();
        Iterator<Point> roomPoints = room.getPoints().iterator();

        visiblePoints.push(roomPoints.next());

        while (roomPoints.hasNext()) {
            Point current = roomPoints.next();
            Point previous = visiblePoints.peek();

            if (isVisibleWRT(current, previous)) {
                while (isVisibleWRT(previous, current)) {
                    visiblePoints.pop();
                    previous = visiblePoints.peek();
                }
            }

            if (isVisibleWRT(current, previous)) {
                visiblePoints.push(current);
            }
        }

        return visiblePoints;
    }

    private boolean isVisibleWRT(Point a, Point b) {
        // TODO Visibility between points

        return new Random().nextBoolean();
    }

    public void addLight(Point light) {
        lights.add(light);

        this.onUpdate();
    }

    public List<Point> getLights() {
        return lights;
    }

    public void clear() {
        lights.clear();
        visibilityRegions.clear();
    }

    public List<List<Point>> getVisibilityRegions() {
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