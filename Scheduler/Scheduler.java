package Scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import FloorSystem.FloorSubsystem;
import FloorSystem.Direction;
import FloorSystem.ElevatorEvent;

public class Scheduler implements Runnable {
	
	private FloorSubsystem floors;
	private ArrayList<ElevatorEvent> upRequests;
	private ArrayList<ElevatorEvent> downRequests;

	/**
	 * Scheduler constructor
	 * @param floors The FloorSubsystem instance executing as a Thread
	 */
    public Scheduler(FloorSubsystem floors) {
    	this.floors = floors;
    	this.upRequests = new ArrayList<>();
        this.downRequests = new ArrayList<>();
    }
    
    /**
     * Method used by Floor Subsystem to pass new elevator requests to scheduler
     * @param elevatorEvent Event signifying a passenger pressed an elevator 
     * 						request button
     */
    public synchronized void newRequest(ElevatorEvent elevatorEvent) {
    	// TODO: Modify to accept serialized object
    	if (elevatorEvent.getDirection() == Direction.UP) {
    		System.out.println("Scheduler Recieved Up Request");
    		this.upRequests.add(elevatorEvent);
    	} else {
    		System.out.println("Scheduler Recieved Down Request");
    		this.downRequests.add(elevatorEvent);
    	}
    	notifyAll();
    }
    
    /**
     * getRequest method is used by the Elevator Thread to get list of next  
     * elevator requests to process.
     * @return Returns an List<ElevatorEvent> containing a set of elevator  
     * 		   requests going in the same direction
     */
	@SuppressWarnings("unchecked") // Suppresses warning for casting cloned ArrayLists
	public synchronized List<ElevatorEvent> getRequest(int currFloor) {
    	while (this.upRequests.isEmpty() && this.downRequests.isEmpty()) {
            try { // Elevator will wait until at least one of the two ArrayLists contain a request
                wait();
            } catch (InterruptedException e) {
                return new ArrayList<ElevatorEvent>();
            }
        }
    	
    	ArrayList<ElevatorEvent> orderedList = (ArrayList<ElevatorEvent>) this.upRequests.clone();
    	Collections.sort(orderedList); // Sorts the List in ascending order by current floor
    	
    	ArrayList<ElevatorEvent> reverseOrderedList = (ArrayList<ElevatorEvent>) this.downRequests.clone();
		Collections.sort(reverseOrderedList); // Sorts the List in ascending order by current floor
		Collections.reverse(reverseOrderedList); // Reverses the sorted list
    	
		
    	if (this.upRequests.size() > 0) { // If there are more up requests than down requests return the up requests
    		if (reverseOrderedList.size() == 0 || (currFloor <= (reverseOrderedList.get(0).getCurrFloor() - orderedList.get(0).getCurrFloor())/2 || this.downRequests.isEmpty())) {
    			this.upRequests.clear(); // Clones then clears the list of up requests
    			System.out.println("Passing Batch of Up Requests");
    			return orderedList;
    		}
    	}
    	this.downRequests.clear(); // Clones then clears the list of down requests
    	System.out.println("Passing Batch of Down Requests");
    	return reverseOrderedList;
    }
    
    public synchronized void destinationReached(ElevatorEvent completedRequest) {
    	floors.alert(completedRequest); // Passes completed request event back to the Floor Subsystem
    }
    
	@Override
	public void run() {} // Empty run statement, will be used when Scheduler subsystem is defined
	
	/**
	 * Test Method for the Scheduler class
	 * @return Returns the ArrayList of Up Events
	 */
	protected ArrayList<ElevatorEvent> getUpEvents() {
		return this.upRequests;
	}
	
	/**
	 * Test Method for the Scheduler class
	 * @return Returns the ArrayList of Down Events
	 */
	protected ArrayList<ElevatorEvent> getDownEvents() {
		return this.downRequests;
	}
}