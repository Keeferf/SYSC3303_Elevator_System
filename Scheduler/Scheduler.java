package Scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import FloorSystem.FloorSubsystem;
import Util.Comms.Config;
import Util.Comms.RequestStatus;
import Util.Comms.UDPBuilder;
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
	

	private DatagramSocket socket;

	/**
	 * Scheduler constructor
	 * @param floors The FloorSubsystem instance executing as a Thread
	 */
    public Scheduler() {
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
		System.out.println("Scheduler Received Request");
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
		} else if (this.downRequests.isEmpty()) { // If there are down requests and no up requests return a down request
			System.out.println("Passing Up Request");
			return Optional.of(this.upRequests.remove(0));
    	} else if (this.upRequests.isEmpty()) { // If there are up requests and no down requests return a down request
    		System.out.println("Passing Down Request");
    		return Optional.of(this.downRequests.remove(0));
    	}
    	return Optional.empty();
    }
	
	/**
	 * Sends and receives messages to the Elevator and the FloorSubsystem 
	 * @throws IOException
	 */
	public void receiveAndSend() throws IOException {
		
		// Receives Packet from FloorSubSystem
		
		byte[] pack = new byte[Config.getMaxMessageSize()];
		DatagramPacket packet = new DatagramPacket(pack,pack.length);
		
		//Gets packet from floor subsystem or elevator
		socket.receive(packet);
		
		Config.printLine();
		
		ElevatorEvent e = UDPBuilder.getPayload(packet);

		System.out.println("Received Packet: " + e.toString());
		
		//Logic for acknowledge/fullfilled/New requests received
		if(e.getRequestStatus().equals(RequestStatus.NEW)) {
			// Send acknowledgement to sender that is has been received
			socket.send(UDPBuilder.acknowledge(e, packet.getAddress().getHostAddress(), packet.getPort()));
			System.out.println("New received. Acknowledgment sent back");	
			//Adds it to the state machine
			newRequest(e);
			Config.printLine();
		} else if(e.getRequestStatus().equals(RequestStatus.FULFILLED)) {
			// Send acknowledgement to sender that is has been received
			socket.send(UDPBuilder.acknowledge(e, packet.getAddress().getHostAddress(), packet.getPort()));
			System.out.println("Fulfilled Received. Acknowledgment sent back");	
			//Add it to responses to send back
			returnResponses.add(e);
			Config.printLine();
		} else if(e.getRequestStatus().equals(RequestStatus.ACKNOWLEDGED)) {
			System.out.println("Acknowledgment received for: " + e.toString());
			Config.printLine();
		} else {
			//Invalid response
			System.out.println("Invalid Request Data Received: " + e.getRequestStatus().toString());
			Config.printLine();
		}
	}
	
	
    
	/**
	 * When called it signifies that the final request has been sent, this is only for the sake of testing, in real execution, 
	 * the system will remain active until interrupted by the controller of the system.
	 */
	public void endRequests() {
		this.lastRequestPassed = true;
	}
	
	/**
	 * Getter method to determine if the final request has been received by the Scheduler
	 * @return Returns a boolean, true = last request has been sent, false = still expecting more requests
	 */
	public boolean isEnd() {
		return this.lastRequestPassed;
	}
	
	/**
	 * Method to pass completed requests from the elevator to the scheduler
	 * @param completedRequest The request completed by the elevator
	 */
	public synchronized void destinationReached(ElevatorEvent completedRequest) {
    	this.returnResponses.add(completedRequest);
    }
	
	/**
	 * Method to return responses to the floor subsystem if there are responses to return
	 */
    public synchronized void returnResponse() {
    	if (this.returnResponses.isEmpty()) {
    		this.state.checkStateChange();
    	} else {
    		//floors.alert(this.returnResponses.remove(0)); // Passes completed request event back to the Floor Subsystem //Depracated section
    		
    		//Responses should already have fulfilled status, so checking is redundant
    		try {
				socket.send(UDPBuilder.newMessage(returnResponses.remove(0), Config.getFloorsubsystemip(), Config.getFloorsubsystemport()));
			} catch (IOException e) {
				System.out.println("Failed to return response");
			}
    	}
    }
    
    /**
     * Run method for the Scheduler thread, it executes the initial Idle state of the Scheduler
     */
	@Override
	public void run() {
		try {
			this.socket = new DatagramSocket(Config.getSchedulerport());
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
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

	/**
	 * Sends the events currently in each of the up and down queues to the elevators via UDP
	 * @author Nicholas Rose - 101181935
	 */
	public synchronized void sendEvents() {
		if(!downRequests.isEmpty()) {
			for(ElevatorEvent e: downRequests) {
				
				try {
						socket.send(UDPBuilder.newMessage(e, Config.getElevatorip(), Config.getElevatorport()));
					
						System.out.println("Sent Message to Elevator: " + e.toString());
				} catch (IOException e1) {
					System.out.println("Failed to send Message" + e.toString());
				}
				Config.printLine();
			}
			downRequests = new ArrayList<>();
		}
		
		if(!upRequests.isEmpty()) {
			for(ElevatorEvent e: upRequests) {
				
				try {
	
					socket.send(UDPBuilder.newMessage(e, Config.getElevatorip(), Config.getElevatorport()));
					
					System.out.println("Sent Message to Elevator: " + e.toString());
				} catch (IOException e1) {
					System.out.println("Failed to send Message" + e.toString());
				}
				Config.printLine();
			}
			upRequests = new ArrayList<>();
		}
		

		
	}
}