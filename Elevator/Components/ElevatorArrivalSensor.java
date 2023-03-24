package Elevator.Components;

/**
 * A class for representing if the elevator has arrived
 * @Author Joshua Noronha - 101194076
 */
public class ElevatorArrivalSensor {
	
	private boolean hasArrived;
	
	
	/**
	 * Setter for the elevator arrival
	 */
	public void setHasArrived(boolean input) {
		this.hasArrived = input;
	}
	
	/**
	 * Getter for the elevator arrival
	 * @return hasArrived
	 */
	public boolean getasArrived() {
		return this.hasArrived;
	}

}
