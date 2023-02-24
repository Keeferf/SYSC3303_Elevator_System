package Elevator;

/**
 * A class for representing the state of a door in the elevator
 * @Author Nicholas Rose - 101181935
 */
public class ElevatorDoor {

	private boolean isOpen;
	
	
	public ElevatorDoor() {
		isOpen = false;
	}
	
	
	public void open() {
		isOpen = true;
	}
	
	public void close() {
		isOpen = false;
	}
	
	public boolean getDoorState() {
		return isOpen;
	}
}
