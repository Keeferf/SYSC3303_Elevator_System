package Elevator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import FloorSystem.Direction;
import FloorSystem.ElevatorEvent;

import Scheduler.Scheduler;


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
	

	
	public Elevator(int maxFloor, int groundFloor, Scheduler sc) {
		this.curFloor = groundFloor;
		this.maxFloor = maxFloor;
		this.groundFloor = groundFloor;
		this.schedule = sc;
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
		Optional<ElevatorEvent> opt = schedule.getRequest(this.curFloor);
		if (opt.isEmpty()) {
			return false;
		}
		this.req = opt.get();
		
        if (this.req.getDirection().equals(Direction.UP)) {
        	System.out.println("Recieved Up Request: " + this.req.toString());
        	this.curFloor = Math.max(this.curFloor, this.req.getFloorToGo());
        } else {
        	System.out.println("Recieved Down Request: " + this.req.toString());
        	this.curFloor = Math.min(this.curFloor, this.req.getFloorToGo());
        }
		schedule.destinationReached(this.req);
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
	
	///////////////////////////
	//Component Utility
	///////////////////////////
	private void arrivedAtFloor(int floorNum) {
		for(ElevatorLamp l: lamps) {if(l.getFloorNum() == floorNum) l.toggle(false);}
		for(ElevatorButton b: buttons) {if(b.getFloorNum() == floorNum) b.toggle(false);}
	}
	
	/**
	 * Run method for the Thread.
	 */
	@Override
	public void run() {
		this.state.runState();
	}
}

