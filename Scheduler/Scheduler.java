package Scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import Util.Comms.Config;
import Util.Comms.RequestStatus;
import Util.Comms.UDPBuilder;
import FloorSystem.ElevatorEvent;
import Scheduler.FaultHandler.FaultHandler;

public class Scheduler implements Runnable {
	
	private ArrayList<ElevatorEvent> incomingRequests;
	private ArrayList<ElevatorEvent> validRequests;
	private ArrayList<ElevatorEvent> returnResponses;
	private ArrayList<Integer> workPorts;
	private SchedulerState state;
	private boolean lastRequestPassed;
	private DatagramSocket socket;
	private HashMap<Integer, Integer> throughput;
	private int numRequests;
	private int numResponses;
	
	private FaultHandler faultHandler;

	/**
	 * Scheduler constructor
	 * @param floors The FloorSubsystem instance executing as a Thread
	 */
    public Scheduler() {
    	this.incomingRequests = new ArrayList<>();
        this.validRequests = new ArrayList<>();
        this.returnResponses = new ArrayList<>();
        this.workPorts = new ArrayList<>();
        this.state = new Idle(this);
        this.lastRequestPassed = false;
        this.throughput = new HashMap<Integer, Integer>();
        this.numRequests = 0;
        this.numResponses = 0;
        
        faultHandler = new FaultHandler();
    }
    
    /**
	 * Scheduler test constructor
	 * @param floors The FloorSubsystem instance executing as a Thread
	 */
    public Scheduler(boolean isTest) {
    	this.incomingRequests = new ArrayList<>();
        this.validRequests = new ArrayList<>();
        this.returnResponses = new ArrayList<>();
        this.workPorts = new ArrayList<>();
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
		System.out.println("Scheduler Received Request");
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
	 * Sends and receives messages to the Elevator and the FloorSubsystem 
	 * @throws IOException
	 */
	public synchronized void receiveAndSend() throws IOException {
		
		// Receives Packet from FloorSubSystem
		
		byte[] pack = new byte[Config.getMaxMessageSize()];
		DatagramPacket packet = new DatagramPacket(pack,pack.length);
		
		//Gets packet from floor subsystem or elevator
		socket.receive(packet);
		
		Config.printLine();
		
		ElevatorEvent e = UDPBuilder.getEventPayload(packet);

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
			
		} else if(e.getRequestStatus().equals(RequestStatus.REQUEST)) {
			if (!this.throughput.keySet().contains(packet.getPort())) {
				this.throughput.put(packet.getPort(), 0);
			}
			//Adds the packet to the queue for when an event needs to be sent
			System.out.println("Added to work queue: " + e.toString());
			workPorts.add(packet.getPort());
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
			//e.printStackTrace();
			System.exit(1);
		}
		this.state.executeState();
	} 
	
	/**
	 * Test Method for the Scheduler class
	 * @return Returns the length of the ArrayList of Validated Events
	 */
	public int getValidQueueLength() {
		return this.validRequests.size();
	}
	
	/**
	 * @return Returns the size of the ArrayList of incoming events
	 */
	public int getIncomingQueueLength() {
		return this.incomingRequests.size();
	}
	
	/**
	 * @return Returns the size of the ArrayList of response events
	 */
	public int getResponseQueueLength() {
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
		if(!validRequests.isEmpty() && !workPorts.isEmpty()) {
			ElevatorEvent e = this.validRequests.remove(0);
			int port = this.getPrioritizedPort();
			e.setElevatorNum(Config.getElevatorNumber(port));
			try {
				socket.send(UDPBuilder.newMessage(e, Config.getElevatorip(), port));
				System.out.println("Sent Message to Elevator at port " + port + ": " + e.toString());
			} catch (IOException e1) {
				System.out.println("Failed to send Message" + e.toString());
			}
			
			//Start a fault handler for the sent event
			//faultHandler.notify(e);
			Config.printLine();
		} else {
			try {
				receiveAndSend(); //See if there is something to accept (work perchance?)
			} catch (IOException e) {
				//Do nothing
			}
		}
	}
	
	/**
	 * Find the next elevator port to send a request to based on equalizing requests being assigned to valid elevators
	 * @return Returns the port to which the next request should be sent
	 */
	private int getPrioritizedPort() {
		int nextPort = -1;
		int min = -1;
		for (int key : this.throughput.keySet()) {
			Config.printLine();
			System.out.println("Elevator @ Port " + key + " has handled " + this.throughput.get(key) + " requests");
			if ((this.throughput.get(key) < min || min == -1) && this.workPorts.contains(key)) {
				min = this.throughput.get(key);
				nextPort = key;
			}
		}
		System.out.println("Elevator @ Port " + nextPort + " assigned next request");
		this.throughput.put(nextPort, this.throughput.getOrDefault(nextPort, 0) + 1);
		this.workPorts.remove(this.workPorts.indexOf(nextPort));
		return nextPort;
	}

	/**
	 * Prints the current throughput of elevators to the console
	 */
	public void printThroughput() {
		for(int i : this.throughput.keySet()) {
			System.out.println("Elevator " + i + " processed " + this.throughput.get(i) + " requests");
		}
	}
}