package Elevator;

import Scheduler.FaultHandler.ElevatorTimingState;

/**
 * Door open state class to handle the doors of the elevator states
 */
public class DoorOpenState implements ElevatorState{

	private Elevator elevator;
	
	/**
	 * Constructor for door open state class
	 * @param elevator
	 */
	public DoorOpenState(Elevator elevator) {
		this.elevator = elevator;
	}

	/**
	 * A method to run the elevator state(open doors)
	 */
	@Override
	public void runState() {
		
		System.out.println("Elevator " + this.elevator.getID() + " Arrived on floor " + this.elevator.getCurrFloor() + "\n");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		elevator.sendTimingEvent(ElevatorTimingState.DOOR_OPEN);
		this.checkState();		
	}

	/**
	 * A method to check the state and activate the door open state
	 */
	@Override
	public void checkState() {
		
		System.out.println("Elevator " + this.elevator.getID() + " Door Open -> Door Closed\n");
		this.elevator.setState(new DoorClosedState(this.elevator));
	}

}