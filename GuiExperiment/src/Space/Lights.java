package Space;

import Visibility.NullVisiblityAlgorithm;
import Visibility.RayTracingVisibilityAlgorithm;
import Visibility.StackVisibilityAlgorithm;
import Visibility.VisibilityAlgorithm;
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

//        VisibilityAlgorithm algorithm = new StackVisibilityAlgorithm();
        VisibilityAlgorithm algorithm = new RayTracingVisibilityAlgorithm();
//        VisibilityAlgorithm algorithm = new NullVisiblityAlgorithm();

        // copy vertices into Point2D list
        List<Vertex> vertices = room.getVertices();
        List<Point2D> polygon = new ArrayList<>(vertices.size());
        for (Vertex vertex : vertices) {
            polygon.add(new Point2D.Double(vertex.getX(), vertex.getY()));
        }
        // createPolygon converts back to Vertex objects
        return createPolygon(algorithm.compute(light, polygon));
    }

    /**
     * Convert list of Point2D objects into a doubly linked list of Vertex objects.
     */
    private List<Vertex> createPolygon(List<Point2D> vertices) {
        List<Vertex> polygon = new ArrayList<>(vertices.size());

        Vertex prev = null;
        for (Point2D vertex : vertices) {
            Vertex newVertex = new Vertex(vertex);
            newVertex.setPrevious(prev);
            prev = newVertex;
            polygon.add(newVertex);
        }
        polygon.get(0).setPrevious(prev);

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