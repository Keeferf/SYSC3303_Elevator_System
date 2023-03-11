package Elevator.Components;

/**
 * A class for representing the state of an elevator button
 * @author Nicholas Rose - 101181935
 *
 */
public class ElevatorButton {
	
	private int floorNum;
	private boolean isPressed;
	
	public ElevatorButton(int floorNum) {
		this.floorNum = floorNum;
		isPressed = false;
	}
	
	public void toggle(boolean t) {
		isPressed = t;
	}
	
	public boolean getState() {
		return isPressed;
	}
	
	public int getFloorNum() {
		return floorNum;
	}
}
