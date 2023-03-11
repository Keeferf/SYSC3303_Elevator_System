package Scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import FloorSystem.FloorSubsystem;
import FloorSystem.ElevatorEvent;

public class Scheduler implements Runnable {
	
	private FloorSubsystem floors;
	private ArrayList<ElevatorEvent> incomingRequests;
	private ArrayList<ElevatorEvent> validRequests;
	private ArrayList<ElevatorEvent> returnResponses;
	private SchedulerState state;
	private boolean lastRequestPassed;
	private HashMap<Integer, Integer> throughput;
	private int numRequests;
	private int numResponses;

	/**
	 * Scheduler constructor
	 * @param floors The FloorSubsystem instance executing as a Thread
	 */
    public Scheduler(FloorSubsystem floors) {
    	this.floors = floors;
    	this.incomingRequests = new ArrayList<>();
        this.validRequests = new ArrayList<>();
        this.returnResponses = new ArrayList<>();
        this.state = new Idle(this);
        this.lastRequestPassed = false;
        this.throughput = new HashMap<Integer, Integer>();
        this.numRequests = 0;
        this.numResponses = 0;
    }
    
    /**
     * Method used by Floor Subsystem to pass new elevator requests to scheduler
     * @param elevatorEvent Event signifying a passenger pressed an elevator 
     * 						request button
     */
    public void newRequest(ElevatorEvent elevatorEvent) {
		System.out.println("Scheduler Recieved Request\n");
    	this.incomingRequests.add(elevatorEvent);
    	this.numRequests++;
    }
    
    /**
     * Method to validate requests passed to the Scheduler
     */
    protected synchronized void validateRequest() {
    	if (!this.incomingRequests.isEmpty()) {
    		ElevatorEvent e = this.incomingRequests.remove(0);
    		System.out.println("Scheduler Validated Request\n");
    		this.validRequests.add(e);
    	}
    	this.state.checkStateChange();
    }
    
    /**
     * getRequest method is used by the Elevator Thread to get the next  
     * elevator request to process.
     * @return Returns an Optional<ElevatorEvent> containing an elevator request
     */
	public synchronized Optional<ElevatorEvent> getRequest(int currFloor, int id) {
		while ((this.validRequests.isEmpty())) {
            try { // Elevator will wait until at least one of the two ArrayLists contain a request
                this.state.checkStateChange();
            	wait(1000);
            } catch (InterruptedException e) {
            	this.state.checkStateChange();
                return Optional.empty();
            }
        }
		this.state.checkStateChange();
		return Optional.of(this.validRequests.remove(0));
    }
    
	/**
	 * When called it signifies that the final request has been sent, this is only for the sake of testing, in real execution, 
	 * the system will remain active until interrupted by the controller of the system.
	 */
	public void endRequests() {
		System.out.println("Scheduler: LAST RESPONSE HAS BEEN RECEIVED\n");
		this.lastRequestPassed = true;
	}
	
	/**
	 * Getter method to determine if the final request has been received by the Scheduler
	 * @return Returns a boolean, true = last request has been sent, false = still expecting more requests
	 */
	public boolean isEnd() {
		return this.lastRequestPassed && (this.numRequests == this.numResponses);
	}
	
	/**
	 * Method to pass completed requests from the elevator to the scheduler
	 * @param completedRequest The request completed by the elevator
	 */
	public void destinationReached(ElevatorEvent completedRequest, int id) {
		System.out.println("Scheduler: Received Response\n");
		System.out.print(this.numResponses);
		this.numResponses++;
		System.out.println(" " + this.numResponses + "\n");
		this.throughput.put(id, this.throughput.getOrDefault(id, 0) + 1);
    	this.returnResponses.add(completedRequest);
    }
	
	/**
	 * Method to return responses to the floor subsystem if there are responses to return
	 */
    public synchronized void returnResponse() {
    	if (this.returnResponses.isEmpty()) {
    		this.state.checkStateChange();
    	} else {
    		floors.alert(this.returnResponses.remove(0)); // Passes completed request event back to the Floor Subsystem
    	}
    }
    
    /**
     * Run method for the Scheduler thread, it executes the initial Idle state of the Scheduler
     */
	@Override
	public void run() {
		this.state.executeState();
	} 
	
	/**
	 * Test Method for the Scheduler class
	 * @return Returns the length of the ArrayList of Validated Events
	 */
	protected int getValidQueueLength() {
		return this.validRequests.size();
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

	/**
	 * State setter method that starts the next state
	 * @param state The next state of the Scheduler
	 */
	public void setState(SchedulerState state) {
		this.state = state;
		this.state.executeState();
	}

	/**
	 * Getter method for the State of the Scheduler
	 * @return Returns the current state object
	 */
	public SchedulerState getState() {
		return this.state;
	}
	
	public void printThroughput() {
		for(int i : this.throughput.keySet()) {
			System.out.println("Elevator " + i + " processed " + this.throughput.get(i) + " requests");
		}
	}
}