package Space.PhaseControl;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import Space.Room;
import Space.UpdateEvent;

/**
 * Interface for the PhaseControl
 * @author i_wou_000
 *
 */
public abstract class PhaseControl {
	
	/**
	 * Store a list of current listeners
	 */
	private List<UpdateEvent> listeners;
	
	/**
	 * CONSTRUCTOR
	 */
	public PhaseControl(){		
		listeners = new ArrayList<>();		
	}
	
	/**
	 * Determine whether the next phase can be entered
	 * @return If the next phase can be entered
	 */
	public abstract boolean canGoEnterNextPhase();
	
	
	/**
	 * Calls the update events
	 */
    public void onUpdate() {   
        // Notify everybody that may be interested.
        for (UpdateEvent uE : listeners)
            uE.onUpdate();
    }
	/**
	 * Adds a listener eager for updates
	 * @param toAdd The added listener event
	 */
	public void addListener(UpdateEvent toAdd) {
        listeners.add(toAdd);
    }

}
