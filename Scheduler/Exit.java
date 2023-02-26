package Scheduler;

/**
 * Exit State for Scheduler class, once the Scheduler enters this state it exits the system
 * @author Colin Mandeville
 */
public class Exit implements SchedulerState {
	
	/**
	 * Method to execute the exit state
	 */
	public void executeState() {
		this.checkStateChange();
	}

	/**
	 * Method to move to the next state, by exiting the exiting the system.
	 */
	public void checkStateChange() {
		System.exit(0);
	}
}
