package Elevator;

import java.util.List;

import FloorSystem.Direction;
import FloorSystem.ElevatorEvent;

import Scheduler.Scheduler;


public class Elevator implements Runnable{
	
	private int maxFloor;
	private int groundFloor;
	private int curFloor;
	private int floorToGo;
	private Scheduler schedule;

	
	public Elevator(int maxFloor, int groundFloor, Scheduler sc) {
		this.curFloor = groundFloor;
		this.maxFloor = maxFloor;
		this.groundFloor = groundFloor;
		this.schedule = sc;
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
		List<ElevatorEvent> arr = schedule.getRequest(this.curFloor);
		if (arr.size() == 0) {
			return false;
		}
		
		for (int i = 0; i < arr.size(); i++) {
            System.out.println(arr.get(i).getDirection());
            if (arr.get(i).equals(Direction.UP)) {
            	this.curFloor = Math.max(this.curFloor, arr.get(i).getFloorToGo());
            } else {
            	this.curFloor = Math.min(this.curFloor, arr.get(i).getFloorToGo());
            }
			schedule.destinationReached(arr.get(i));
			Thread.sleep(1000);
		}
		return true;
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
			try {
				elevatorActivated();
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}

