package Elevator.Components;

/**
 * A class for representing the state of an elevator lamp for a certain floor
 * @author Nicholas Rose - 101181935
 *
 */
public class ElevatorLamp {
	
	private boolean isOn;
	private int floorNum;
	
	public ElevatorLamp(int floorNum) {
		isOn = false;
		this.floorNum = floorNum;
	}
	
	public void toggle(boolean t) {
		isOn = t;
	}
	
	public boolean getState() {
		return isOn;
	}
	
	public int getFloorNum() {
		return floorNum;
	}
}
