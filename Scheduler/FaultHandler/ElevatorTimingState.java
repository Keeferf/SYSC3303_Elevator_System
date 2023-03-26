package Scheduler.FaultHandler;

/**
 * An enum for representing the states and their associated time
 * @author Nicholas Rose - 101181935
 *
 */
public enum ElevatorTimingState {
	START(0),
	DOOR_CLOSED(5),
	ACCELERATING(DOOR_CLOSED.time() + 0), 
	DECELERATING(ACCELERATING.time() + 0), 
	DOOR_OPEN(DECELERATING.time() + 5.000);
	
	private final double time;
	
	private static final double FUDGE_FACTOR = 2;
	
	ElevatorTimingState(double time) {
		this.time = (time * FUDGE_FACTOR);
	}
	
	public double time() {
		return time;
	}
}
