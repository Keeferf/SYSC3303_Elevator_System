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
    	while (this.upRequests.isEmpty() && this.downRequests.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                return Optional.empty();
            }
        }
    	if (this.upRequests.size() > this.downRequests.size()) {
    		//TODO: SORT BY STARTING FLOOR (Lowest to Highest)
    		ArrayList<ElevatorEvent> al = (ArrayList<ElevatorEvent>) this.upRequests.clone();
    		this.upRequests.clear();
        	return Optional.of(al);
    	} else if (!this.downRequests.isEmpty()) {
    		//TODO: SORT BY STARTING FLOOR (Highest to Lowest)
    		ArrayList<ElevatorEvent> al = (ArrayList<ElevatorEvent>) this.downRequests.clone();
    		this.downRequests.clear();
        	return Optional.of(al);
    	}
    	return Optional.empty();
    }
    
    public synchronized void destinationReached(ElevatorEvent completedRequest) {
    	floors.alert(completedRequest);
    }
    
	@Override
	public void run() {
		while (true) {}
	}

}