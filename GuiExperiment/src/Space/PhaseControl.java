package Space;

/**
 * Interface for the PhaseControl
 * @author i_wou_000
 *
 */
public interface PhaseControl {
	
	/**
	 * Determine whether the next phase can be entered
	 * @return If the next phase can be entered
	 */
	public abstract boolean canGoEnterNextPhase();

}
