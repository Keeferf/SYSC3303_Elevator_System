package FloorSystem;

/**
 * The Floor class is used for everything involved in
 * calling the elevator and accessing the current floor
 * 
 * @author Keefer Belanger 101152085
 */
public class Floor implements Runnable{
	
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
	
	/**
	 * Setter method for the floor number
	 * @param floorNum : desired floor number
	 */
	public void setFloorNum(int floorNum) {
		this.floorNum = floorNum;
	}
	
	/**
	 * Prints out the request which was completed, i.e. the passenger arrived at their destination floor
	 * @param completedRequest Passenger request which was completed
	 */
	public void alert(String completedRequest) {
		System.out.println(completedRequest);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
