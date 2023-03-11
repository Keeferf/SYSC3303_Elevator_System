package Elevator;

public class DoorOpenState implements ElevatorState{

	private Elevator elevator;
	
	public DoorOpenState(Elevator elevator) {
		this.elevator = elevator;
	}
	@Override
	public void runState() {
		while(elevator.getState() == this) {
			try {
				this.elevator.arrivedAtFloor(this.elevator.getCurFloor());
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.checkState();
		}
		
	}

	@Override
	public void checkState() {
		System.out.println("Elevator " + this.elevator.getID() + ": Door Open -> Door Closed\n");
		this.elevator.setState(new DoorClosedState(this.elevator));
	}

}