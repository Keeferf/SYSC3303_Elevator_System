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

	
	public ElevatorTimingEvent(ElevatorEvent event, ElevatorTimingState timingState) {
		super((Object)event.getSource(), event.getTimestamp(), event.getDirection(), event.getFloorToGo(), event.getCurrFloor());
		
		elevatorNum = event.getElevatorNum();
		
		this.timingState = timingState;
		
		completedTime = LocalTime.now().toString();
	}
	
	
	
	
	
	public ElevatorTimingState getElevatorTimingState() {
		return timingState;
	}
	
	public String getCompletedTime() {
		return completedTime;
	}
	
	public int getElevatorId() {
		return elevatorNum;
	}
}
