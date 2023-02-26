package Elevator;

public class IdleState implements ElevatorState{
	private Elevator elevator;
	
	public IdleState(Elevator elevator) {
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
		System.out.println("Idle -> DoorClosed");
		elevator.setState(new DoorClosedState(elevator));
		
	}

}