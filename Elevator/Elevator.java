package Elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Elevator.Components.ElevatorButton;
import Elevator.Components.ElevatorDoor;
import Elevator.Components.ElevatorLamp;
import Elevator.Components.ElevatorMotor;
import FloorSystem.Direction;
import FloorSystem.ElevatorEvent;

import Scheduler.Scheduler;
import Util.Comms.Config;
import Util.Comms.RequestStatus;
import Util.Comms.UDPBuilder;


public class Elevator implements Runnable{
	
	private int maxFloor;
	private int groundFloor;
	private int curFloor;
	private int floorToGo;
	private Scheduler schedule;
	private ElevatorState state;
	
	private ArrayList<ElevatorLamp> lamps;
	private ArrayList<ElevatorButton> buttons;
	private ElevatorDoor door;
	private ElevatorMotor motor;
	private ElevatorEvent req ;
	
	private DatagramSocket socket;
	

	
	public Elevator()  {
		this.curFloor = Config.getMinFloor();
		this.maxFloor = Config.getMaxFloor();
		this.groundFloor = curFloor;
		this.state = new IdleState(this);
	
		
		//Initialise the components
		lamps = new ArrayList<>();
		buttons = new ArrayList<>();
		
		door = new ElevatorDoor();
		motor = new ElevatorMotor();
		
		for(int i = groundFloor;i <= maxFloor;i++) {
			lamps.add(new ElevatorLamp(i));
			buttons.add(new ElevatorButton(i));
		}
	}
	
	/**
	 * Moves the Elevator up to the floor the passenger needs to go.
	 * 
	 * @throws InterruptedException
	 */
	private void up() throws InterruptedException {
		
		if (curFloor == floorToGo) {
			//System.out.print("Doors Open");
			door.open();
			//this.state.checkState();
			Thread.sleep(1318);
			
			arrivedAtFloor(curFloor);
		}
		else{
			//System.out.print("Doors Closed");
			door.close();
			//this.state.checkState();
			Thread.sleep(1318);
			int temp = (this.getFloorToGo()) - 1;
			while(curFloor != this.getFloorToGo()) {
				curFloor++;
				motor.activateUp();
			}
			motor.disableMotor();
			//System.out.print("Doors Open");
			door.open();
			//this.state.checkState();
			arrivedAtFloor(curFloor);
		}
		//this.state.checkState();
	}
	
	/**
	 * Moves the Elevator down to the floor the passenger needs to go.
	 * 
	 * @throws InterruptedException
	 */
	public void down() throws InterruptedException {
		
		if (curFloor == floorToGo) {
			//System.out.print("Doors Open");
			door.open();
			//this.state.checkState();
			Thread.sleep(1318);
			arrivedAtFloor(curFloor);
		}
		else{
			//System.out.print("Doors Closed");
			door.close();
			//this.state.checkState();
			Thread.sleep(1318);
			//int temp = (this.getFloorToGo()) + 1;
			while(curFloor != this.floorToGo) {
				motor.activateDown();
				curFloor--;
			}
			motor.disableMotor();
			//System.out.print("Doors Open");
			door.open();
			//this.state.checkState();
			arrivedAtFloor(curFloor);
		}
		//this.state.checkState();
	}
	
	/**
	 * Moves the Elevator up or down depending on the floor the passenger's request.
	 * 
	 * @throws InterruptedException
	 */
	public void pressButton() throws InterruptedException {
		
		for(ElevatorLamp l: lamps) {if(l.getFloorNum() == floorToGo) l.toggle(true);}
		for(ElevatorButton b: buttons) {if(b.getFloorNum() == floorToGo) b.toggle(true);}
		
		if (floorToGo < curFloor) {
			down();
			//this.state.checkState();
		}
		else if (curFloor < floorToGo){
			up();
			//this.state.checkState();
		}
		
	}
	
	
	/**
	 * Gets the destination from Scheduler and then moves the elevator as 
	 * per the passenger's request. 
	 * 
	 * @return true or false If the request array is null or not
	 * @throws InterruptedException 
	 */
	public boolean elevatorActivated() throws InterruptedException {
		//Retrieve UDP requests
		
		//Might make a separate thread just for listening to requests for the future
		byte[] pack = new byte[Config.getMaxMessageSize()];
		DatagramPacket packet = new DatagramPacket(pack,pack.length);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			System.out.println("Failed to acquire packet!");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		//Check if payload is valid
		ElevatorEvent e = UDPBuilder.getPayload(packet);
		
		req = e;
		
        if (e.getDirection().equals(Direction.UP)) {
        	System.out.println("Recieved Up Request: " + e.toString());
        	this.curFloor = Math.max(this.curFloor, e.getFloorToGo());
        } else {
        	System.out.println("Recieved Down Request: " + e.toString());
        	this.curFloor = Math.min(this.curFloor, e.getFloorToGo());
        }
		
        //Send an acknowledge reply back to scheduler
        try {
			socket.send(UDPBuilder.acknowledge(e, Config.getSchedulerip(), Config.getSchedulerport()));
		} catch (IOException e1) {
			System.out.println("Failed to send Packet!");
			e1.printStackTrace();
			return false;
		}
		Thread.sleep(1000);
		return true;
	}
	
	/**
	 * Returns the Current floor that the Elevator is on.
	 * 
	 * @return curFloor
	 */
	public int getCurFloor() {
		return curFloor;
	}
	
	/**
	 * Returns true if the current floor of the Elevator is set.
	 * 
	 * @return true
	 */
	public boolean setCurFloor(int newCurFloor) {
		this.curFloor = newCurFloor;
		return true;
		
	}
	/**
	 * Returns the ground floor of the Elevator.
	 * 
	 * @return groundFloor
	 */
	public int getGroundFloor() {
		return groundFloor;
	}
	
	/**
	 * Returns the maximum floor of the Elevator.
	 * 
	 * @return maxFloor
	 */
	public int getMaxFloor() {
		return maxFloor;
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
	public boolean setFloorToGo(int newFloorNum) {
		this.floorToGo = newFloorNum;
		return true;
		
	}
	
	/**
	 * Sets the state of the Elevator.
	 * 
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
	  * Returns the current floor of the passenger.
	  * 
	  * @return req.getCurrFloor()
	  */
	 public int getRequest() {
		   return this.req.getCurrFloor();
	   }
	 
	 /**
	  * Returns the full request of the passenger.
	  * 
	  * @return req
	  */
	 public ElevatorEvent getFullRequest() {
		   return this.req;
	   }
	
	/**
	 * For when a car has arrived at a floor
	 * @param floorNum
	 */
	private void arrivedAtFloor(int floorNum) {
		for(ElevatorLamp l: lamps) {if(l.getFloorNum() == floorNum) l.toggle(false);}
		for(ElevatorButton b: buttons) {if(b.getFloorNum() == floorNum) b.toggle(false);}
		
		ElevatorEvent e = req;
		e.setRequestStatus(RequestStatus.FULFILLED);
		
		try {
			socket.send(UDPBuilder.newMessage(e, Config.getSchedulerip(), Config.getSchedulerport()));
			
			byte[] pack = new byte[Config.getMaxMessageSize()];
			DatagramPacket packet = new DatagramPacket(pack,pack.length);
			
			socket.receive(packet);
			
			if(UDPBuilder.getPayload(packet).getRequestStatus().equals(RequestStatus.ACKNOWLEDGED)) {
				System.out.println("Scheduler Acknowledged the fulfilled request: " + e);
			}
		} catch (IOException e1) {
			System.out.println("Failed to send fulfilled message: " + e.toString());
			e1.printStackTrace();
		}
	}
	
	/**
	 * Run method for the Thread.
	 */
	@Override
	public void run() {
		try {
			this.socket = new DatagramSocket(Config.getElevatorport());
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
		this.state.runState();
	}
}

