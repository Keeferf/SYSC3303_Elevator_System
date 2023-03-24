package Elevator;

import Scheduler.FaultHandler.ElevatorTimingState;

/**
 * Door close state class to handle the doors of the elevator states
 */
public class DoorClosedState implements ElevatorState{

	private Elevator elevator;
	
	/**
	 * Constructor for door close state class
	 * @param elevator
	 */
	public DoorClosedState(Elevator elevator) {
		this.elevator = elevator;
	}
	
	/**
	 * A method to run the elevator state(close doors)
	 */
	@Override
	public void runState() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.checkState();
	}

	/**
	 * A method to check the state and activate the door close state
	 */
	@Override
	public void checkState() {
		if(elevator.getCurrFloor() == this.elevator.getRequest().getFloorToGo()) {
			this.elevator.arrivedAtFloor(this.elevator.getCurrFloor());
			System.out.println("Elevator " + this.elevator.getID() + ": Dropped off passenger, Door Closed -> Idle\n");
			elevator.setState(new IdleState(elevator));
		} else {
			this.elevator.setFloorToGo(this.elevator.getRequest().getFloorToGo());
			System.out.println("Elevator " + this.elevator.getID() + " Picked up passenger, Door Closed -> Accelerating\n");
			
			elevator.sendTimingEvent(ElevatorTimingState.DOOR_CLOSED);
			
			elevator.setState(new AcceleratingState(elevator));
		}
	}

}
