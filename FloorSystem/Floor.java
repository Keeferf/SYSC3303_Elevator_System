package FloorSystem;

import java.util.Random;

/**
 * The Floor class is used for everything involved in
 * calling the elevator and accessing the current floor
 * 
 * @author Keefer Belanger 101152085
 */
public class Floor{
	
	private int floorNum;
	private int people;
	private boolean upLamp;
	private boolean downLamp;
	private Random rand;
	
	private boolean isOn;
	
	/**
	 * Constructor for Floor class
	 * @param floorNum
	 */
	public Floor(int floorNum) {
		this.floorNum = floorNum;
		this.people = 0;
		this.isOn = false;
		this.rand = new Random();
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
	 */
	public int setPeople() {
		return rand.nextInt(6);
	}
	
	/**
	 * Getter to see if up lamp is true/false
	 * @return
	 */
	public boolean getUpLamp() {
		return upLamp;
	}
	
	/**
	 * Getter to see if down lamp is true/false
	 * @return
	 */
	public boolean getDownLamp() {
		return downLamp;
	}
	
	/**
	 * Method to check if the up/down lamp is on
	 * @return a boolean
	 */
	public boolean isOn() {
		return isOn;
	}
	
	/**
	 * Method to toggle the lamp light on/off
	 */
	public void toggle() {
		isOn = !isOn;
	}
}
