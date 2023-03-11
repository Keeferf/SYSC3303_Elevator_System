package Elevator;

/**
 * A class for representing the state of an elevator button
 * @author Nicholas Rose - 101181935
 *
 */
public class ElevatorButton {
	
	private int floorNum;
	private boolean isPressed;
	
	/**
	 * Constructor for the elevator button class
	 * @param floorNum: int
	 */
	public ElevatorButton(int floorNum) {
		this.floorNum = floorNum;
		isPressed = false;
	}
	
	/**
	 * A method to toggle the elevator button
	 * @param t: boolean
	 */
	public void toggle(boolean t) {
		isPressed = t;
	}
	
	/**
	 * Getter for the state of the elevator button(true/false)
	 * @return isPressed: boolean
	 */
	public boolean getState() {
		return isPressed;
	}
	
	/**
	 * Getter for the floor number
	 * @return floor number: int
	 */
	public int getFloorNum() {
		return floorNum;
	}
}
