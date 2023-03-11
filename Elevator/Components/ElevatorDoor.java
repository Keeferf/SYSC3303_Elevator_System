package Elevator.Components;

/**
 * A class for representing the state of a door in the elevator
 * @Author Nicholas Rose - 101181935
 */
public class ElevatorDoor {

	private boolean isOpen;
	
	/**
	 * Constructor for the elevator class
	 */
	public ElevatorDoor() {
		isOpen = false;
	}
	
	/**
	 * A method to set the elevator door to open
	 */
	public void open() {
		isOpen = true;
	}
	
	/**
	 * A method to set the elevator door to closed
	 */
	public void close() {
		isOpen = false;
	}
	
	/**
	 * Getter for the door state
	 * @return isOpen: boolean
	 */
	public boolean getDoorState() {
		return isOpen;
	}
}
