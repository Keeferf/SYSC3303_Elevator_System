package Elevator;

import Elevator.Components.ElevatorArrivalSensor;

/**
 * The idle state class handles the idle state for the elevator
 */
public class IdleState implements ElevatorState{
	private Elevator elevator;
	private ElevatorArrivalSensor sensor;
	
	/**
	 * Constructor for Idle state class
	 * @param elevator
	 */
	public IdleState(Elevator elevator) {
		this.elevator = elevator;
		this.sensor = new ElevatorArrivalSensor();
	}

	/**
	 * A method to run the elevator state(idle)
	 */
	@Override
	public void runState() {
		while(elevator.getState() == this) {
			try {
				elevator.elevatorActivated();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * A method to check the state of the elevator and activate the idle state
	 */
	@Override
	public void checkState() {
		if (elevator.getCurrFloor() == elevator.getRequest().getCurrFloor()){
			this.sensor.setHasArrived(true);
			System.out.println("Elevator " + this.elevator.getID() + " Idle -> DoorOpen\n");
			elevator.setState(new DoorOpenState(elevator));
		} else {
			this.elevator.setFloorToGo(this.elevator.getRequest().getCurrFloor());
			this.sensor.setHasArrived(false);
			System.out.println("Elevator " + this.elevator.getID() + " Idle -> Accelerate\n");
			elevator.setState(new AcceleratingState(elevator));
		}
	}

}