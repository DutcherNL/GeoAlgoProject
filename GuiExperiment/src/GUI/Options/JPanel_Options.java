package GUI.Options;

import javax.swing.JPanel;

import Space.Room;

/**
 * Basic abstract class for the JPanel_Options
 * @author i_wou_000
 *
 */
public abstract class JPanel_Options extends JPanel{
	
	/**
	 * Necessary implementation, don't know why....
	 */
	protected static final long serialVersionUID = 1L;
	
	protected Room room;	
	
	public JPanel_Options(Room Room) {
		this.room = Room;
	}
	
	
	
}
