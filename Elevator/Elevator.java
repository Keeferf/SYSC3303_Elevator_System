package Elevator;

import java.util.ArrayList;
import java.util.Optional;


public class Elevator implements Runnable{
	
	private int maxFloor;
	private int groundFloor;
	private int curFloor;
	private int floorToGo;
	private Scheduler schedule;

	
	public Elevator(int maxFloor, int groundFloor) {
		this.curFloor = groundFloor;
		this.maxFloor = maxFloor;
		this.groundFloor = groundFloor;
	}
	
	/**
	 * Moves the Elevator up to the floor the passenger needs to go.
	 * 
	 * @throws InterruptedException
	 */
	private void up() throws InterruptedException {
		
		if (curFloor == floorToGo) {
			System.out.print("Doors Open");
			Thread.sleep(1318);
		}
		else{
			System.out.print("Doors Closed");
			Thread.sleep(1318);
			while(curFloor != floorToGo) {
				curFloor++;
			}
			System.out.print("Doors Open");
		}
	}
	
	/**
	 * Moves the Elevator down to the floor the passenger needs to go.
	 * 
	 * @throws InterruptedException
	 */
	private void down() throws InterruptedException {
		
		if (curFloor == floorToGo) {
			System.out.print("Doors Open");
			Thread.sleep(1318);
		}
		else{
			System.out.print("Doors Closed");
			Thread.sleep(1318);
			while(curFloor != floorToGo) {
				curFloor--;
			}
			System.out.print("Doors Open");
		}
	}
	
	/**
	 * Moves the Elevator up or down depending on the floor the passenger's request.
	 * 
	 * @throws InterruptedException
	 */
	public void pressButton() throws InterruptedException {
		if (floorToGo < curFloor) {
			down();
		}
		else {
			up();
		}
		
	}
	
	
	/**
	 * Gets the destination from Scheduler and then moves the elevator as 
	 * per the passenger's request. 
	 * 
	 * @return true or false If the request array is null or not
	 * @throws InterruptedException 
	 */
	private boolean elevatorActivated() throws InterruptedException {
		Optional<ArrayList<String>> obj = schedule.getRequest(curFloor);
		ArrayList <String> arr = obj.get();
		
		if(!obj.isEmpty()) {
			
			for (int i = 0; i < arr.size(); i++) {
	           
				schedule.destinationReached(arr.get(i));
				Thread.sleep(500);
				
				// Printing and display the elements in ArrayList
				System.out.print(arr.get(i) + " ");
				
				}
			
			return true;
		}
		
		else {
			
			return false;
		}
		
	}
	
	/**
	 * Returns the Current floor that the Elevator is on.
	 * 
	 * @return curFloor
	 */
	public int getCurFloor() {
		return curFloor;
	}
	
	/**
	 * Returns true if the current floor of the Elevator is set.
	 * 
	 * @return true
	 */
	public boolean setCurFloor(int newCurFloor) {
		this.curFloor = newCurFloor;
		return true;
		
	}
	/**
	 * Returns the ground floor of the Elevator.
	 * 
	 * @return groundFloor
	 */
	public int getGroundFloor() {
		return groundFloor;
	}
	
	/**
	 * Returns the maximum floor of the Elevator.
	 * 
	 * @return maxFloor
	 */
	public int getMaxFloor() {
		return maxFloor;
	}
	
	/**
	 * Returns the floor the passenger wants to go.
	 * 
	 * @return floorToGo
	 */
	public int getFloorToGo() {
		return floorToGo;
	}
	
	/**
	 * Returns true if the current floor of the Elevator is set.
	 * 
	 * @return true
	 */
	public boolean setFloorToGo(int newFloorNum) {
		this.floorToGo = newFloorNum;
		return true;
		
	}
	
	/**
	 * Run method for the Thread.
	 */

	@Override
	public void run() {
		
		while (true) {
			
			System.out.println("Elevator Running");
			try {
				
				elevatorActivated();
				wait();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		
	}

}

