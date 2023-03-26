package Elevator;


import Scheduler.FaultHandler.ElevatorTimingState;

import Elevator.Components.ElevatorArrivalSensor;


/**
 * Door close state class to handle the doors of the elevator states
 */
public class DoorClosedState implements ElevatorState{

	private Elevator elevator;
	private ElevatorArrivalSensor sensor;
	
	/**
	 * Constructor for door close state class
	 * @param elevator
	 */
	public DoorClosedState(Elevator elevator) {
		this.elevator = elevator;
		this.sensor = new ElevatorArrivalSensor();
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
		if(elevator.getRequest().getCurrFloor() == elevator.getCurrFloor() ) {
			elevator.sendTimingEvent(ElevatorTimingState.START);
			elevator.checkDoorFault();
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
			this.sensor.setHasArrived(true);
			System.out.println("Elevator " + this.elevator.getID() + ": Dropped off passenger, Door Closed -> Idle\n");
			elevator.setState(new IdleState(elevator));
		} else {
			this.elevator.setFloorToGo(this.elevator.getRequest().getFloorToGo());
			this.sensor.setHasArrived(false);
			System.out.println("Elevator " + this.elevator.getID() + " Picked up passenger, Door Closed -> Accelerating\n");
			
			elevator.sendTimingEvent(ElevatorTimingState.START);
			elevator.checkDoorFault();
			
			elevator.sendTimingEvent(ElevatorTimingState.DOOR_CLOSED);
			
			elevator.setState(new AcceleratingState(elevator));
		}
	}

}
