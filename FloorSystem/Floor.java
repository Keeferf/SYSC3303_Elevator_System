package FloorSystem;

/**
 * The Floor class is used for everything involved in
 * calling the elevator and accessing the current floor
 * 
 * @author Keefer Belanger 101152085
 */
public class Floor{
	
	private int floorNum;
	private int people;
	private boolean upLamp, downLamp;
	
	/**
	 * Constructor for Floor class
	 * @param floorNum
	 */
	public Floor(int floorNum) {
		this.floorNum = floorNum;
		this.people = 0;
		this.upLamp = false;
		this.downLamp = false;
	}
	
	/**
	 * Getter method for the floor number
	 * @return the current floor number
	 */
	public int getFloorNum() {
		return floorNum;
	}
	
	/**
	 * Setter method for the floor number
	 * @param floorNum : desired floor number
	 */
	public void setFloorNum(int floorNum) {
		this.floorNum = floorNum;
	}
	
	/**
	 * Getter to get number of people on the floor
	 * @return number of people
	 */
	public int getPeople() {
		return people;
	}
	
	/**
	 * Setter to set number of people on the floor
	 * @param people
	 */
	public void setPeople(int people) {
		this.people = people;
	}
	
	/**
	 * Method for next interation
	 * @return
	 */
	public int numOfDoors() {
		return 0;
	}
}
