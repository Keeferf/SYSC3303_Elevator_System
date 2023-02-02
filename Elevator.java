package Project;

import java.util.ArrayList;


public class Elevator {
	
	private int maxFloor;
	private int groundFloor;
	private int curFloor;
	private int floorToGo;
	//private ArrayList<Floor.Rider> contents = null; // contents
	//private boolean empty = true; // empty?
	
	public Elevator(int floorToGo, int curFloor, int maxFloor, int groundFloor) {
		this.floorToGo = floorToGo;
		this.curFloor = curFloor;
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
	public void pressButton(Schedule sch) throws InterruptedException {
		if (floorToGo < curFloor) {
			down();
		}
		else {
			up();
		}
		
	}
	
	
	
	/* public synchronized void put(ArrayList<Floor.Rider> items) {
	        while (!empty) {
	            try {
	                wait();
	            } catch (InterruptedException e) {
	                return;
	            }
	        }
	        contents = items;
	        empty = false;
	        notifyAll();
	    }
	
	
	
	public synchronized ArrayList<Floor.Rider> get() {
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                return null;
            }
        }
        
        ArrayList<Floor.Rider> items = contents;
        contents = null;
        empty = true;
        notifyAll();
        return items;
    }
	
	*/
	
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


}
