package Scheduler;

import java.util.ArrayList;
import java.util.Optional;

import FloorSystem.Floor_Subsystem;
import FloorSystem.Direction;
import FloorSystem.ElevatorEvent;

public class Scheduler implements Runnable {
	
	private Floor_Subsystem floors;
	private ArrayList<ElevatorEvent> upRequests;
	private ArrayList<ElevatorEvent> downRequests;

    public Scheduler(Floor_Subsystem floor) {
    	this.floors = floor;
    	this.upRequests = new ArrayList<>();
        this.downRequests = new ArrayList<>();
    }
    
    public synchronized void newRequest(ElevatorEvent e) {
    	// TODO: Modify to accept serialized object
    	if (e.getDirection() == Direction.UP) {
    		this.upRequests.add(e);
    	} else {
    		this.downRequests.add(e);
    	}
    	notifyAll();
    }
    
    public synchronized Optional<ArrayList<ElevatorEvent>> getRequest(int currFloor) {
    	if (this.upRequests.size() > this.downRequests.size()) {
    		//TODO: SORT BY STARTING FLOOR (Lowest to Highest)
        	return Optional.of(this.upRequests);
    	} else if (!this.downRequests.isEmpty()) {
    		//TODO: SORT BY STARTING FLOOR (Highest to Lowest)
    		return Optional.of(this.downRequests);
    	}
    	return Optional.empty();
    }
    
    public synchronized void destinationReached(ElevatorEvent completedRequest) {
    	floors.alert(completedRequest);
    }
    
	@Override
	public void run() {
		while (true) {
			System.out.println("Scheduler Running");
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

}