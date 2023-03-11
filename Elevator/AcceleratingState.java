package Elevator;

import FloorSystem.Direction;
import FloorSystem.ElevatorEvent;

/**
 * Acceleration state class to handle the accelertion of the elevator states
 */
public class AcceleratingState implements ElevatorState{
	
	private Elevator elevator;
	
	/**
	 * Constructor for the acceleration state class
	 * @param elevator
	 */
	public AcceleratingState(Elevator elevator) {
		this.elevator = elevator;
	}

	/**
	 * A method to run the elevator state(acceleration)
	 */
	@Override
	public void runState() {
		while(elevator.getState() == this) {
			try {
				this.elevator.moveElevator();
				Thread.sleep(1318);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.checkState();
		}
		
	}

	/**
	 * A method to check the state and activate the acceleration state
	 */
	@Override
	public void checkState() {
		ElevatorEvent req = elevator.getRequest();
		if (req.getFloorToGo() == this.elevator.getFloorToGo()) {
			if (req.getDirection() == Direction.UP) {
				if(elevator.getCurrFloor() >= elevator.getFloorToGo() - 1) {
					System.out.println("Elevator " + this.elevator.getID() + ": Accelerate -> Decelerate\n");
					elevator.setState(new DeceleratingState(elevator));
				}
			} else {
				if(elevator.getCurrFloor() == elevator.getFloorToGo() + 1) {
					System.out.println("Elevator " + this.elevator.getID() + ": Accelerate -> Decelerate\n");
					elevator.setState(new DeceleratingState(elevator));
				}
			}
		} else {
			if(elevator.getCurrFloor() >= elevator.getFloorToGo() - 1 || elevator.getCurrFloor() >= elevator.getFloorToGo() + 1) {
				System.out.println("Elevator " + this.elevator.getID() + ": Accelerate -> Decelerate\n");
				elevator.setState(new DeceleratingState(elevator));
			}
		}
	}
}