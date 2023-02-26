package Elevator;

public class DeceleratingState implements ElevatorState{
	
	private Elevator elevator;
	
	public DeceleratingState(Elevator elevator) {
		this.elevator = elevator;
	}

	@Override
	public void runState() {
		while(elevator.getState() == this) {
			try {
				this.elevator.down();
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
		if(elevator.getCurFloor() != elevator.getFloorToGo()) {
			System.out.println("Decelerate -> DoorClosed");
			elevator.setState(new DoorClosedState(elevator));
		}else {
			System.out.println("Decelerate -> DoorOpen");
			elevator.setState(new DoorOpenState(elevator));
		}
		
	}

}