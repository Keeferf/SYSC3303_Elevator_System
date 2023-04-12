package Scheduler;

/**
 * Exit State for Scheduler class, once the Scheduler enters this state it exits the system
 * @author Colin Mandeville
 */
public class Exit implements SchedulerState {
	
	private Scheduler s;
	
	/**
	 * Constructor for the Exit State
	 * @param s Scheduler instance
	 */
	public Exit(Scheduler s) {
		this.s = s;
	}
	
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
		this.s.printThroughput();
		System.exit(0);
	}
}
