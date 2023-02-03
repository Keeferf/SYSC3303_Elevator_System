package Scheduler;

import java.util.ArrayList;
import java.util.Optional;

import FloorSystem.Floor;
import FloorSystem.FloorSubsystem;

public class Scheduler implements Runnable {
	
	private Floor floors;
	private ArrayList<String> upRequests;
	private ArrayList<String> downRequests;

    public Scheduler() {
    	this.upRequests = new ArrayList<String>();
        this.downRequests = new ArrayList<String>();
    }
    
    public synchronized void newRequest(String req) {
    	// TODO: Modify to accept serialized object
    	if (req.contains("up")) {
    		this.upRequests.add(req);
    	} else {
    		this.downRequests.add(req);
    	}
    }
    
    public synchronized Optional<ArrayList<String>> getRequest(int currFloor) {
    	if (this.upRequests.size() > this.downRequests.size()) {
    		//TODO: SORT BY STARTING FLOOR (Lowest to Highest)
        	return Optional.of(this.upRequests);
    	} else if (!this.downRequests.isEmpty()) {
    		//TODO: SORT BY STARTING FLOOR (Highest to Lowest)
    		return Optional.of(this.downRequests);
    	}
    	return Optional.empty();
    }
    
    public synchronized void destinationReached(String completedRequest) {
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
	
    public static void main(String[] args) {
    	Thread sch = new Thread(new Scheduler());
    	sch.start();
    	try {
    		long i = (long) (Math.random() * 50000);
    		System.out.println(i);
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
    	System.exit(0);
    }
}