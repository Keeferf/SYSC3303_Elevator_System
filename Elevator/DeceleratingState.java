package Elevator;


import Scheduler.FaultHandler.ElevatorTimingState;

import Elevator.Components.ElevatorArrivalSensor;


/**
 * Deceleration state class to handle the decelertion of the elevator states
 */
public class DeceleratingState implements ElevatorState{
	
	private Elevator elevator;
	private ElevatorArrivalSensor sensor;

	/**
	 * Constructor for the Decelerating state class
	 * @param elevator
	 */
	public DeceleratingState(Elevator elevator) {
		this.elevator = elevator;
		this.sensor = new ElevatorArrivalSensor();
	}

	/**
	 * A method to run the elevator state(deceleration)
	 */
	@Override
	public void runState() {
		while(elevator.getState() == this) {
			try {
				this.elevator.moveElevator();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.checkState();
		}
		
		
	}

	/**
	 * A method to check the state and activate the deceleration state
	 */
	@Override
	public void checkState() {
		try {
			this.elevator.moveElevator();

			this.sensor.setHasArrived(false);
			System.out.println("Elevator " + this.elevator.getID() + ": Decelerating -> Door Open\n");

			this.elevator.setState(new DoorOpenState(this.elevator));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}

}