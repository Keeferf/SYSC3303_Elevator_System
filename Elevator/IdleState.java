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
				elevator.elevatorActivated();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.checkState();
		}
		
	}

	@Override
	public void checkState() {
		
		if (elevator.getCurFloor() == elevator.getRequest()){
		System.out.println("Idle -> DoorOpen");
		elevator.setState(new DoorOpenState(elevator));
		}else if (elevator.getCurFloor() != elevator.getFloorToGo()) {
			System.out.println("Idle -> Accelerate");
			elevator.setState(new AcceleratingState(elevator));
		}else if (elevator.getCurFloor() != elevator.getRequest()) {
			System.out.println("Idle -> Accelerate");
			elevator.setState(new AcceleratingState(elevator));
		}
	}

}