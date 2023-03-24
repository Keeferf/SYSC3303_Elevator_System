package Elevator;

import Elevator.Components.ElevatorArrivalSensor;
import FloorSystem.Direction;
import FloorSystem.ElevatorEvent;

/**
 * Acceleration state class to handle the accelertion of the elevator states
 */
public class AcceleratingState implements ElevatorState{
	
	private Elevator elevator;
	private ElevatorArrivalSensor sensor;
	
	/**
	 * Constructor for the acceleration state class
	 * @param elevator
	 */
	public AcceleratingState(Elevator elevator) {
		this.elevator = elevator;
		this.sensor = new ElevatorArrivalSensor();
	}

	/**
	 * A method to run the elevator state(acceleration)
	 */
	@Override
	public void runState() {
		while(elevator.getState() == this) {
			this.checkState();
			if (this.elevator.getState() == this) {
				try {
					this.elevator.moveElevator();
					this.sensor.setHasArrived(false);
					System.out.println("Elevator " + this.elevator.getID() + " passes floor " + this.elevator.getCurrFloor());
					Thread.sleep(1318);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * A method to check the state and activate the acceleration state
	 */
	@Override
	public void checkState() {
		ElevatorEvent req = elevator.getRequest();
		if (req.getFloorToGo() == elevator.getFloorToGo()) {
			if (req.getDirection() == Direction.UP) {
				if(elevator.getCurrFloor() == elevator.getFloorToGo() - 1) {
					System.out.println("Elevator " + elevator.getID() + ": Accelerate -> Decelerate\n");
					elevator.setState(new DeceleratingState(elevator));
				}
			} else {
				if(elevator.getCurrFloor() == elevator.getFloorToGo() + 1) {
					System.out.println("Elevator " + elevator.getID() + ": Accelerate -> Decelerate\n");
					elevator.setState(new DeceleratingState(elevator));
				}
			}
		} else {
			if(elevator.getCurrFloor() >= elevator.getFloorToGo() - 1 && elevator.getCurrFloor() <= elevator.getFloorToGo() + 1) {
				System.out.println("Elevator " + elevator.getID() + ": Accelerate -> Decelerate\n");
				elevator.setState(new DeceleratingState(elevator));
			}
		}
	}
}