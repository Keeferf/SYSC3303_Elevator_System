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
				this.elevator.moveElevator();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.checkState();
		}
		
	}

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