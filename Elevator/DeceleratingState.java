package Elevator;

import Scheduler.FaultHandler.ElevatorTimingState;

/**
 * Deceleration state class to handle the decelertion of the elevator states
 */
public class DeceleratingState implements ElevatorState{
	
	private Elevator elevator;

	/**
	 * Constructor for the Decelerating state class
	 * @param elevator
	 */
	public DeceleratingState(Elevator elevator) {
		this.elevator = elevator;
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
			System.out.println("Elevator " + this.elevator.getID() + ": Decelerating -> Door Open\n");	
			this.elevator.setState(new DoorOpenState(this.elevator));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}

}