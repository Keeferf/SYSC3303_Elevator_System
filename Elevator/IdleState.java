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
				if (this.elevator.getRequest() != null && this.elevator.getCurrFloor() == this.elevator.getRequest().getFloorToGo()) {
					this.elevator.requestComplete();
				}
				elevator.elevatorActivated();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void checkState() {
		if (elevator.getCurrFloor() == elevator.getRequest().getCurrFloor()){
			this.elevator.setFloorToGo(this.elevator.getRequest().getFloorToGo());
			System.out.println("Elevator " + this.elevator.getID() + " Idle -> DoorOpen\n");
			elevator.setState(new DoorOpenState(elevator));
		} else {
			this.elevator.setFloorToGo(this.elevator.getRequest().getCurrFloor());
			System.out.println("Elevator " + this.elevator.getID() + " Idle -> Accelerate\n");
			elevator.setState(new AcceleratingState(elevator));
		}
	}

}