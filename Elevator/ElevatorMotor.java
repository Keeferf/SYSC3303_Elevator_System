package Elevator;

/**
 * The elevator motor class handles all the activation setting states for the elevator
 */
public class ElevatorMotor {
	
	private boolean isActive;
	private Status status;
	
	/**
	 * Status enumerator for the elevator motor
	 */
	public enum Status{
		UP, DOWN, STOPPED
	}
	
	/**
	 * Constructor for the elevator motor class
	 */
	public ElevatorMotor() {
		isActive = false;
		status = Status.STOPPED;
	}
	
	/**
	 * A method to change the status to up
	 */
	public void activateUp() {
		status = Status.UP;
		isActive = true;
	}
	
	/**
	 * A method to change the status to down
	 */
	public void activateDown() {
		status = Status.DOWN;
		isActive = true;
	}
	
	/**
	 * A method to disbale the motor, changing active to false
	 */
	public void disableMotor() {
		isActive = false;
		status = Status.STOPPED;
	}
	
	/**
	 * A method to check if the elevator motor is active
	 * @return isActive: boolean
	 */
	public boolean isActive() {
		return isActive;
	}
	
	/**
	 * Getter for the status of the elevator motor
	 * @return status: Status
	 */
	public Status getStatus() {
		return status;
	}
	
	
}
