package Elevator;

import FloorSystem.Direction;

public class DoorOpenState implements ElevatorState{

	private Elevator elevator;
	
	public DoorOpenState(Elevator elevator) {
		this.elevator = elevator;
	}
	@Override
	public void runState() {
		while(elevator.getState() == this) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.checkState();
		}
		
	}

	@Override
	public void checkState() {
		if(elevator.getCurFloor() == elevator.getFloorToGo()) {
			System.out.println("DoorOpen -> DoorClosed");
			elevator.setState(new DoorClosedState(elevator));
		}
		
	}

}