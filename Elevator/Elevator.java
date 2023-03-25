package Elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import Elevator.Components.ElevatorArrivalSensor;
import Elevator.Components.ElevatorButton;
import Elevator.Components.ElevatorDoor;
import Elevator.Components.ElevatorLamp;
import Elevator.Components.ElevatorMotor;
import FloorSystem.ElevatorEvent;
import Scheduler.FaultHandler.ElevatorTimingEvent;
import Scheduler.FaultHandler.ElevatorTimingState;
import Util.Comms.Config;
import Util.Comms.RequestStatus;
import Util.Comms.UDPBuilder;

/**
 * Elevator class to handle all the elevator components
 */
public class Elevator implements Runnable{
	
	private static final int maxFloor = Config.getMaxFloor();
	private static final int groundFloor = Config.getMinFloor();
	private final int id;
	private int currFloor;
	private int floorToGo;
	private ElevatorState state;
	
	private boolean doorFault = false;
	private boolean motorFault = false;
	
	private final ArrayList<ElevatorLamp> lamps;
	private final ArrayList<ElevatorButton> buttons;
	private final ElevatorDoor door;
	private final ElevatorMotor motor;
	private ElevatorEvent req;
	
	private DatagramSocket socket;
	
	private static int idCounter = 0;
	
	private boolean timingHasStarted;
	private ArrayList<ElevatorTimingState> sent;

	/**
	 * Constructor for the elevator class
	 * @param sc: Scheduler
	 */
	public Elevator()  {
		this.currFloor = groundFloor;
	
		this.id = Elevator.idCounter;
		System.out.println("Elevator " + id + " created.");
		idCounter++;
		this.state = new IdleState(this);
	
		sent = new ArrayList<>();
		
		//Initialize the components
		lamps = new ArrayList<>();
		buttons = new ArrayList<>();
		
		door = new ElevatorDoor();
		motor = new ElevatorMotor();
		
		for(int i = groundFloor;i <= maxFloor;i++) {
			lamps.add(new ElevatorLamp(i));
			buttons.add(new ElevatorButton(i));
		}
		
		timingHasStarted = false;
	}
	
	/**
	 * Moves the Elevator up to the floor the passenger needs to go.
	 * 
	 * @throws InterruptedException
	 */
	private void up() throws InterruptedException {
		if (currFloor == floorToGo) {
			motor.activateUp();
			currFloor++;
			motor.disableMotor();
			door.open();
		}else{
			motor.activateUp();
			currFloor++;
			motor.disableMotor();
			door.open();
		}
	}
	
	/**
	 * Moves the Elevator down to the floor the passenger needs to go.
	 * 
	 * @throws InterruptedException
	 */
	public void down() throws InterruptedException {
		if (currFloor == floorToGo) {
			motor.activateDown();
			currFloor--;
			motor.disableMotor();
		}else{
			motor.activateDown();
			currFloor--;
			motor.disableMotor();
		}
	}
	
	/**
	 * Moves the Elevator up or down depending on the floor the passenger's request.
	 * 
	 * @throws InterruptedException
	 */
	public void moveElevator() throws InterruptedException {
		for(ElevatorLamp l: lamps) {if(l.getFloorNum() == currFloor) l.setState(true);}
		for(ElevatorButton b: buttons) {if(b.getFloorNum() == currFloor) b.toggle(true);}
		
		if (floorToGo < currFloor) {
			down();
		} else if (floorToGo > currFloor){
			up();
		} else {
			sendTimingEvent(ElevatorTimingState.ACCELERATING);	//Added to fix state skip
			sendTimingEvent(ElevatorTimingState.DECELERATING);
			this.setState(new DoorOpenState(this));
		}
	}
	
	
	/**
	 * Gets the destination from Scheduler and then moves the elevator as 
	 * per the passenger's request. 
	 * 
	 * @return true or false If the request array is null or not
	 * @throws InterruptedException 
	 */
	public void elevatorActivated() throws InterruptedException {
		//Retrieve UDP requests
		ElevatorEvent requestEvent = new ElevatorEvent(this);
		System.out.println("Elevator " + id + " sent request for work");
		try {
			socket.send(UDPBuilder.newMessage(requestEvent, Config.getSchedulerip(), Config.getSchedulerport()));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		//Might make a separate thread just for listening to requests for the future
		
		//Get the response back with data
		byte[] pack = new byte[Config.getMaxMessageSize()];
		DatagramPacket packet = new DatagramPacket(pack,pack.length);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			System.out.println("Failed to acquire packet!");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		//Get payload of response
		ElevatorEvent e = UDPBuilder.getPayload(packet);
		
		req = e;
		//req.setElevatorNum(getID());
		
		System.out.println("Elevator " + this.id + " Received Request: " + this.req.toString());
		
		System.out.println("Elevator " + this.id + " at floor " + this.currFloor + "\n");
		
		
		Thread.sleep(1000);
		
		this.state.checkState();
		this.state.runState();
	}
	
	/**
	 * Returns the Current floor that the Elevator is on.
	 * 
	 * @return currFloor
	 */
	public int getCurrFloor() {
		return currFloor;
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
	public void setFloorToGo(int newFloorNum) {
		this.floorToGo = newFloorNum;
	}
	
	/**
	 * Sets the state of the Elevator.
	 */
	public void setState(ElevatorState state) {
		this.state = state;
		this.state.runState();
	}
	 
	/**
	 * Returns the Elevator state.
	 * 
	 * @return state
	 */
	public ElevatorState getState() {
		return this.state;
	}

	/**
	 * Returns the full request of the passenger.
	 * 
	 * @return req
	 */
	public ElevatorEvent getRequest() {
		 return this.req;
	}
	 
	/**
	 * Getter method for the elevator id
	 * @return Returns the ID of the elevator instance
	 */
	public int getID() {
		return this.id;
	}
	
	/**
	 * Checks for
	 */
	public void checkDoorFault() {
		if(doorFault == true) {
			byte[] pack = new byte[Config.getMaxMessageSize()];
			DatagramPacket packet = new DatagramPacket(pack,pack.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * For when a car has arrived at a floor
	 * @param floorNum Floor Number arrived at
	 */
	protected void arrivedAtFloor(int floorNum) {
		System.out.println("Elevator " + this.id + " arrived at " + this.currFloor + "\n");
		for(ElevatorLamp l: lamps) {if(l.getFloorNum() == floorNum) l.setState(false);}
		for(ElevatorButton b: buttons) {if(b.getFloorNum() == floorNum) b.toggle(false);}
		
		ElevatorEvent e = req;
		e.setRequestStatus(RequestStatus.FULFILLED);
		
		try {
			socket.send(UDPBuilder.newMessage(e, Config.getSchedulerip(), Config.getSchedulerport()));
			byte[] pack = new byte[Config.getMaxMessageSize()];
			DatagramPacket packet = new DatagramPacket(pack,pack.length);
			
			socket.receive(packet);
			if(UDPBuilder.getPayload(packet).getRequestStatus().equals(RequestStatus.ACKNOWLEDGED)) {
				System.out.println("Scheduler Acknowledged the fulfilled request: " + e + " by elevator: " + id);
			}
		} catch (IOException e1) {
			System.out.println("Failed to send fulfilled message: " + e.toString());
			e1.printStackTrace();
		}
		
		try {
			elevatorActivated();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Sends a timing event to the fault handler.
	 * 
	 * + logic for 
	 * @param ets
	 */
	protected void sendTimingEvent(ElevatorTimingState ets) {
		if(timingHasStarted && ets.equals(ElevatorTimingState.START)) return;
		
		if(sent.contains(ets)) return;	//already been send
		sent.add(ets);
		
		if(ets.equals(ElevatorTimingState.START)) {
			timingHasStarted = true;
		} else if(ets.equals(ElevatorTimingState.DOOR_OPEN)) {
			timingHasStarted = false;
			sent.clear();
		}
		req.setElevatorNum(getID());
		
		
		ElevatorTimingEvent event = new ElevatorTimingEvent(req,ets);
		//event.setElevatorId
		//Send the timing event back to the fault handler
		try {
			socket.send(UDPBuilder.newMessage(event, Config.getFaultHandlerIp(), Config.getFaultHandlerPort()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Run method for the Thread.
	 */
	@Override
	public void run() {
		try {
			this.socket = new DatagramSocket(Config.getElevatorport(this.id));
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
		this.state.runState();
	}
}

