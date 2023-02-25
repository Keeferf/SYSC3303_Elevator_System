package Scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import FloorSystem.FloorSubsystem;
import FloorSystem.Direction;
import FloorSystem.ElevatorEvent;

public class Scheduler implements Runnable {
	
	private FloorSubsystem floors;
	private ArrayList<ElevatorEvent> incomingRequests;
	private ArrayList<ElevatorEvent> upRequests;
	private ArrayList<ElevatorEvent> downRequests;
	private ArrayList<ElevatorEvent> returnResponses;
	private SchedulerState state;
	private boolean lastRequestPassed;

	/**
	 * Scheduler constructor
	 * @param floors The FloorSubsystem instance executing as a Thread
	 */
    public Scheduler(FloorSubsystem floors) {
    	this.floors = floors;
    	this.incomingRequests = new ArrayList<>();
    	this.upRequests = new ArrayList<>();
        this.downRequests = new ArrayList<>();
        this.returnResponses = new ArrayList<>();
        this.state = new Idle(this);
        this.lastRequestPassed = false;
    }
    
    /**
     * Method used by Floor Subsystem to pass new elevator requests to scheduler
     * @param elevatorEvent Event signifying a passenger pressed an elevator 
     * 						request button
     */
    public void newRequest(ElevatorEvent elevatorEvent) {
		System.out.println("Scheduler Recieved Request");
    	this.incomingRequests.add(elevatorEvent);
    }
    
    /**
     * Method to validate requests passed to the Scheduler
     */
    protected synchronized void validateRequest() {
    	if (!this.incomingRequests.isEmpty()) {
    		ElevatorEvent e = this.incomingRequests.remove(0);
    		if (e.getDirection() == Direction.UP) {
        		System.out.println("Scheduler Validated Up Request");
        		this.upRequests.add(e);
        		Collections.sort(this.upRequests); // Sorts the List in ascending order by current floor
        		this.state.checkStateChange();
    		} else {
        		System.out.println("Scheduler Validated Down Request");
        		this.downRequests.add(e);
        		Collections.sort(this.downRequests); // Sorts the List in ascending order by current floor
        		Collections.reverse(this.downRequests); // Reverses the sorted list
        		this.state.checkStateChange();
    		}
    		this.notifyAll();
    	}
    }
    
    /**
     * getRequest method is used by the Elevator Thread to get the next  
     * elevator request to process.
     * @return Returns an Optional<ElevatorEvent> containing an elevator request
     */
	public synchronized Optional<ElevatorEvent> getRequest(int currFloor) {
		while ((this.upRequests.isEmpty() && this.downRequests.isEmpty())) {
            try { // Elevator will wait until at least one of the two ArrayLists contain a request
                this.state.checkStateChange();
            	wait(1000);
            } catch (InterruptedException e) {
            	this.state.checkStateChange();
                return Optional.empty();
            }
        }
		if (!this.upRequests.isEmpty() && !this.downRequests.isEmpty()) {
			if ((this.downRequests.get(0).getCurrFloor() - currFloor) > (currFloor - this.upRequests.get(0).getCurrFloor())) {
				System.out.println("Passing Up Request");
				return Optional.of(this.upRequests.remove(0));
			} else {
				System.out.println("Passing Down Request");
				return Optional.of(this.downRequests.remove(0));
			}
		} else if (this.downRequests.isEmpty()) { // If there are more up requests than down requests return the up requests
			System.out.println("Passing Up Request");
			return Optional.of(this.upRequests.remove(0));
    	} else if (this.upRequests.isEmpty()) {
    		System.out.println("Passing Down Request");
    		return Optional.of(this.downRequests.remove(0));
    	}
    	return Optional.empty();
    }
    
	public void endRequests() {
		this.lastRequestPassed = true;
	}
	
	public boolean isEnd() {
		return this.lastRequestPassed;
	}
	
	public void destinationReached(ElevatorEvent completedRequest) {
    	this.returnResponses.add(completedRequest);
    }
	
    public synchronized void returnResponse() {
    	if (this.returnResponses.isEmpty()) {
    		this.state.checkStateChange();
    	} else {
    		floors.alert(this.returnResponses.remove(0)); // Passes completed request event back to the Floor Subsystem
    	}
    }
    
	@Override
	public void run() {
		this.state.executeState();
	} 
	
	/**
	 * Test Method for the Scheduler class
	 * @return Returns the length of the ArrayList of Up Events
	 */
	protected int getUpQueueLength() {
		return this.upRequests.size();
	}
	
	/**
	 * Test Method for the Scheduler class
	 * @return Returns the length of the ArrayList of Down Events
	 */
	protected int getDownQueueLength() {
		return this.downRequests.size();
	}
	
	/**
	 * @return Returns the size of the ArrayList of incoming events
	 */
	protected int getIncomingQueueLength() {
		return this.incomingRequests.size();
	}
	
	/**
	 * @return Returns the size of the ArrayList of response events
	 */
	protected int getResponseQueueLength() {
		return this.returnResponses.size();
	}

	public void setState(SchedulerState state) {
		this.state = state;
		this.state.executeState();
	}

	public SchedulerState getState() {
		return this.state;
	}
}