package Scheduler.FaultHandler;

import java.io.Serializable;
import java.time.LocalTime;

import FloorSystem.Direction;
import FloorSystem.ElevatorEvent;

/**
 * A timing event to be sent via UDP to the fault handler
 * Ensures the correct operation of the system
 * @author Nicholas Rose - 101181935
 *
 */
public class ElevatorTimingEvent extends ElevatorEvent implements Serializable{
	
	private String completedTime;
	
	private ElevatorTimingState timingState;
	
	private int elevatorNum;

	/**
	 * Constructor for the ElevatorTimingEvent class
	 * @param event The elevator event to be timed
	 * @param timingState The timing state  to be used by the ElevatorTimingEvent object
	 */
	public ElevatorTimingEvent(ElevatorEvent event, ElevatorTimingState timingState) {
		super(null, event.getTimestamp(), event.getDirection(), event.getFloorToGo(), event.getCurrFloor());
		
		elevatorNum = event.getElevatorNum();
		
		this.timingState = timingState;
		
		completedTime = LocalTime.now().toString();
		
		if(event.getElevatorNum() != -1) {
			elevatorNum = event.getElevatorNum();
		} else {
			throw new IllegalArgumentException("Elevator Number must be set in the ElevatorEvent Object");
		}
	}
	
	/**
	 * Getter method for the elevator timing event state
	 * @return Returns the timing state stored in the elevator timing event object
	 */
	public ElevatorTimingState getElevatorTimingState() {
		return timingState;
	}
	
	/**
	 * Getter method for the time at which the timing event was completed
	 */
	public String getCompletedTime() {
		return completedTime;
	}
	
	/**
	 * The id of the elevator handlingt the elevator timing event
	 * @return
	 */
	public int getElevatorId() {
		return elevatorNum;
	}
}
