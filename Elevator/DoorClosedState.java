package Elevator;

public class DoorClosedState implements ElevatorState{

	private Elevator elevator;
	
	public DoorClosedState(Elevator elevator) {
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
				System.out.println("DoorClosed -> Idle");
				elevator.setState(new IdleState(elevator));
			}
		
	}

}