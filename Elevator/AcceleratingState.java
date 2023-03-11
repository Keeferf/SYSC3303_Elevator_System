package Elevator;

import FloorSystem.Direction;
import FloorSystem.ElevatorEvent;

public class AcceleratingState implements ElevatorState{
	
	private Elevator elevator;
	
	public AcceleratingState(Elevator elevator) {
		this.elevator = elevator;
	}

	@Override
	public void runState() {
		while(elevator.getState() == this) {
			this.checkState();
			if (this.elevator.getState() == this) {
				try {
					this.elevator.moveElevator();
					Thread.sleep(1318);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

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