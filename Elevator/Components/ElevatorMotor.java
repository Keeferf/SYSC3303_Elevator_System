package Elevator.Components;

public class ElevatorMotor {
	
	private boolean isActive;
	private Status status;
	
	public enum Status{
		UP, DOWN, STOPPED
	}
	
	public ElevatorMotor() {
		isActive = false;
		status = Status.STOPPED;
	}
	
	public void activateUp() {
		status = Status.UP;
		isActive = true;
	}
	
	public void activateDown() {
		status = Status.DOWN;
		isActive = true;
	}
	
	public void disableMotor() {
		isActive = false;
		status = Status.STOPPED;
	}
	
	
	public boolean isActive() {
		return isActive;
	}
	
	public Status getStatus() {
		return status;
	}
	
	
}
