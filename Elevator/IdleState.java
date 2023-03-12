package Elevator;

/**
 * The idle state class handles the idle state for the elevator
 */
public class IdleState implements ElevatorState{
	private Elevator elevator;
	
	/**
	 * Constructor for Idle state class
	 * @param elevator
	 */
	public IdleState(Elevator elevator) {
		this.elevator = elevator;
	}

	/**
	 * A method to run the elevator state(idle)
	 */
	@Override
	public void runState() {
		while(elevator.getState() == this) {
			try {
				elevator.elevatorActivated();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * A method to check the state of the elevator and activate the idle state
	 */
	@Override
	public void checkState() {
		if (elevator.getCurrFloor() == elevator.getRequest().getCurrFloor()){
			System.out.println("Elevator " + this.elevator.getID() + " Idle -> DoorOpen\n");
			elevator.setState(new DoorOpenState(elevator));
		} else {
			this.elevator.setFloorToGo(this.elevator.getRequest().getCurrFloor());
			System.out.println("Elevator " + this.elevator.getID() + " Idle -> Accelerate\n");
			elevator.setState(new AcceleratingState(elevator));
		}
	}

}