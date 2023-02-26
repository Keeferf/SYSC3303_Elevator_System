package Elevator;

import FloorSystem.Direction;

public class DeceleratingState implements ElevatorState{
	
	private Elevator elevator;
	
	public DeceleratingState(Elevator elevator) {
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
		if(elevator.getCurFloor() == elevator.getFloorToGo()&& elevator.getFullRequest().getDirection().equals(Direction.UP)) {
			elevator.setFloorToGo(elevator.getFloorToGo());
			try {
				this.elevator.pressButton();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Decelerate -> DoorOpen");
			elevator.setState(new DoorClosedState(elevator));
		}else if (elevator.getCurFloor() == elevator.getFloorToGo()&& elevator.getFullRequest().getDirection().equals(Direction.DOWN)){
			elevator.setFloorToGo(elevator.getFloorToGo() - 1);
			try {
				this.elevator.pressButton();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Decelerate -> DoorOpen");
			elevator.setState(new DoorOpenState(elevator));
		}
		
	}

}