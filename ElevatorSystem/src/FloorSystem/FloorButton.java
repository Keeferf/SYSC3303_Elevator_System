package FloorSystem;

/**
 * The FloorButton class is in charge of the setter and getter methods for the floor
 * subsystem of the elevator, it controls the up and down buttons on the floors.
 * 
 * @author Keefer Belanger 101152085
 */
public class FloorButton {
	
	private String time;
	private Direction direction;
	private String currFloorNum;
	private String destFloorNum;
	
	/**
	 * Constructor for FloorButton class
	 * @param time
	 * @param currFloorNum
	 * @param destFloorNum
	 * @param direction
	 */
	public FloorButton(String time, String currFloorNum, Direction direction, String destFloorNum) {
		this.time = time;
		this.currFloorNum = currFloorNum;
		this.destFloorNum = destFloorNum;
		this.direction = direction;
	}
	
	/**
	 * Getter method for time
	 * @return a string for time that the elevator took to perform a floor change
	 */
	public String getTime() {
		return time;
	}
	
	/**
	 * Setter method for time
	 * @param time : time it takes for elevator to change floors
	 */
	public void setTime(String time) {
		this.time = time;
	}
	
	/**
	 * Getter method for the current floor
	 * @return an int that shows the elevators current floor
	 */
	public String getCurrFloorNum() {
		return currFloorNum;
	}
	
	/**
	 * Setter method for the current floor
	 * @param currFloorNum : the current floor the elevator is on
	 */
	public void setCurrFloorNum(String currFloorNum) {
		this.currFloorNum = currFloorNum;
	}
	
	/**
	 * Getter method for the direction the elevator is moving
	 * @return the direction the elevator is moving
	 */
	public Direction getDirection() {
		return direction;
	}
	
	/**
	 * Setter method for the direction of the elevator
	 * @param direction : direction of the way the elevator is moving
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	/**
	 * Getter method for the destination number where the elevator wants to move to
	 * @return the destination floor
	 */
	public String getDestFloorNum() {
		return destFloorNum;
	}
	
	/**
	 * Setter method for the selected floor
	 * @param destFloorNum : floor number destination
	 */
	public void setDestFloorNum(String destFloorNum) {
		this.destFloorNum = destFloorNum;
	}
	
}
