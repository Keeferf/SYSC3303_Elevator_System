package Elevator;

/**
 * A class for representing the state of an elevator lamp for a certain floor
 * @author Nicholas Rose - 101181935
 *
 */
public class ElevatorLamp {
	
	private boolean isOn;
	private int floorNum;
	
	/**
	 * Constructor for the elevator lamp class
	 * @param floorNum
	 */
	public ElevatorLamp(int floorNum) {
		isOn = false;
		this.floorNum = floorNum;
	}
	
	/**
	 * Setter for the state of the elevator lamp
	 * @param t: boolean
	 */
	public void setState(boolean t) {
		isOn = t;
	}
	
	/**
	 * Getter for the state of the elevator lamp
	 * @return isOn: boolean
	 */
	public boolean getState() {
		return isOn;
	}
	
	/**
	 * Getter for the floor number
	 * @return floorNum: int
	 */
	public int getFloorNum() {
		return floorNum;
	}
}
