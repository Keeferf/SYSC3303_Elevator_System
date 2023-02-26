package Elevator;

public class AcceleratingState implements ElevatorState{
	
	private Elevator elevator;
	
	public AcceleratingState(Elevator elevator) {
		this.elevator = elevator;
	}

	@Override
	public void runState() {
		while(elevator.getState() == this) {
			try {
				this.elevator.up();
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
			System.out.println("Accelerate -> DoorClosed");
			
			elevator.setState(new DoorClosedState(elevator));
		}else {
			System.out.println("Accelerate -> DoorOpen");
			elevator.setState(new DoorOpenState(elevator));
		}
		
	}

}