package Project;

import java.util.ArrayList;


public class Elevator {
	
	private int maxFloor = 2;
	private int groundFloor = 0;
	private int curFloor;
	private int floorToGo;
	private ArrayList<Floor.Rider> contents = null; // contents
	private boolean empty = true; // empty?
	
	public Elevator(int floorToGo, int curFloor) {
		this.floorToGo = floorToGo;
		this.curFloor = curFloor;
	}
	
	public void up() {
		
		if (curFloor == maxFloor && floorToGo == maxFloor) {
			System.out.print("Doors Open");
		}
		else{
			System.out.print("Doors Closed");
			while(curFloor != floorToGo) {
				curFloor++;
			}
			System.out.print("Doors Open");
		}
	}
	
	public void down() {
		
		if (curFloor == groundFloor && floorToGo == groundFloor) {
			System.out.print("Doors Open");
		}
		else{
			System.out.print("Doors Closed");
			while(curFloor != floorToGo) {
				curFloor--;
			}
			System.out.print("Doors Open");
		}
	}
	
	 public synchronized void put(ArrayList<Floor.Rider> items) {
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
	
	
	public int getCurFloor() {
		return curFloor;
	}
	
	public int getGroundFloor() {
		return groundFloor;
	}
	
	public int getMaxFloor() {
		return maxFloor;
	}
	
	public int getFloorToGo() {
		return floorToGo;
	}

}
