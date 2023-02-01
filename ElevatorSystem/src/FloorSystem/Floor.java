package FloorSystem;

/**
 * The Floor class is used for everything involved in
 * calling the elevator and accessing the current floor
 * 
 * @author Keefer Belanger 101152085
 */
public class Floor {
	
	private int floorNum;
	
	/**
	 * Constructor for Floor class
	 * @param floorNum
	 */
	public Floor(int floorNum) {
		this.floorNum = floorNum;
	}
	
	/**
	 * Getter method for the floor number
	 * @return the current floor number
	 */
	public int getFloorNum() {
		return floorNum;
	}
	
	public void setFloorNum(int floorNum) {
		this.floorNum = floorNum;
	}
}
