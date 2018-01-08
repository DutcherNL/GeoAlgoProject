package Space;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Room {

	/**
	 * A collection of all fragment sections.
	 */
	private List<RoomFragment> sections;
	
	/**
	 * A collection of all listeners
	 */
	private List<UpdateEvent> listeners = new ArrayList<>();
	
	/**
	 * The bounding perimters of the room
	 */
	private int x_min = Integer.MAX_VALUE;
	private int x_max = Integer.MIN_VALUE;
	private int y_min = Integer.MAX_VALUE;
	private int y_max = Integer.MIN_VALUE;
	
	/**
	 * Constructor
	 */
	public Room(){
		sections = new ArrayList<RoomFragment>();		
	}
	
	/**
	 * Return all fragments in the room
	 * @return
	 */
	public List<RoomFragment> getFragments(){
		return sections;
	}
	
	/**
	 * Add a room fragment (section)
	 * @param RoomFragment The fragment added
	 */
	public void addFragment(RoomFragment RoomFragment) {
		this.sections.add(RoomFragment);
		
		// Adjust the bounding box
		Rectangle R = RoomFragment.getBoundaryBox();
		if (R.getMinX() < x_min) x_min = (int) R.getMinX();
		if (R.getMinY() < y_min) y_min = (int) R.getMinY();
		if (R.getMaxX() > x_max) x_max = (int) R.getMaxX();
		if (R.getMaxY() > y_max) y_max = (int) R.getMaxY();
		
		
		this.onUpdate();
	}	
	
	/**
	 * Get the bounding box of the room
	 * @return The entire bounding box
	 */
	public Rectangle getBoundary(){
		return new Rectangle(x_min, y_min, x_max - x_min, y_max - y_min);
	}
	
	/**
	 * Clear the entire room contents
	 */
	public void clear() {
		sections.clear();
		x_min = Integer.MAX_VALUE;
		x_max = Integer.MIN_VALUE;
		y_min = Integer.MAX_VALUE;
		y_max = Integer.MIN_VALUE;
		
		this.onUpdate();
	}
	
	/**
	 * Add a listener for updates
	 * @param toAdd The event listener
	 */
	public void addListener(UpdateEvent toAdd) {
        listeners.add(toAdd);
    }

	/**
	 * Trigger update method
	 */
    public void onUpdate() {    	
        // Notify everybody that may be interested.
        for (UpdateEvent uE : listeners)
            uE.onUpdate();
    }
}
