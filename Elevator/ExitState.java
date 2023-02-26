package Elevator;

public class ExitState implements ElevatorState{
	public void runState() {
		this.checkState();
	}

	public void checkState() {
		System.exit(0);
	}
}
