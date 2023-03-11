package Elevator;

public class DoorClosedState implements ElevatorState{

	private Elevator elevator;
	
	public DoorClosedState(Elevator elevator) {
		this.elevator = elevator;
	}
	
	@Override
	public void runState() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.checkState();
	}

	@Override
	public void checkState() {
		if(elevator.getCurrFloor() == this.elevator.getRequest().getFloorToGo()) {
			this.elevator.arrivedAtFloor(this.elevator.getCurrFloor());
			System.out.println("Elevator " + this.elevator.getID() + ": Dropped off passenger, Door Closed -> Idle\n");
			elevator.setState(new IdleState(elevator));
		} else {
			this.elevator.setFloorToGo(this.elevator.getRequest().getFloorToGo());
			System.out.println("Elevator " + this.elevator.getID() + " Picked up passenger, Door Closed -> Accelerating\n");
			elevator.setState(new AcceleratingState(elevator));
		}
	}

}
