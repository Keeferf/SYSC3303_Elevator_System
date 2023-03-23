package Scheduler.FaultHandler;

/**
 * An enum for representing the states and their associated time
 * @author Nicholas Rose - 101181935
 *
 */
public enum ElevatorTimingState {
	DOOR_CLOSED(1000),
	ACCELERATING(DOOR_CLOSED.time() + 1318), 
	DECELERATING(ACCELERATING.time() + 1000), 
	DOOR_OPEN(DECELERATING.time() + 5000);
	
	private final double time;
	
	private static final double FUDGE_FACTOR = 1.5;
	
	ElevatorTimingState(double time) {
		this.time = (time * FUDGE_FACTOR);
	}
	
	public double time() {
		return time;
	}
}


