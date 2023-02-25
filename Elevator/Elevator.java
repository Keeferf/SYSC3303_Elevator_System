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
	
	private ArrayList<ElevatorLamp> lamps;
	private ArrayList<ElevatorButton> buttons;
	private ElevatorDoor door;
	private ElevatorMotor motor;

	
	public Elevator(int maxFloor, int groundFloor, Scheduler sc) {
		this.curFloor = groundFloor;
		this.maxFloor = maxFloor;
		this.groundFloor = groundFloor;
		this.schedule = sc;
		
		//Initialize the components
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
	 * @throws InterruptedException
	 */
	private void up() throws InterruptedException {
		
		if (curFloor == floorToGo) {
			System.out.print("Doors Open");
			door.open();
			Thread.sleep(1318);
			
			arrivedAtFloor(curFloor);
		}
		else{
			System.out.print("Doors Closed");
			door.close();
			Thread.sleep(1318);
			while(curFloor != floorToGo) {
				curFloor++;
				motor.activateUp();
			}
			motor.disableMotor();
			System.out.print("Doors Open");
			door.open();
			
			arrivedAtFloor(curFloor);
		}
	}
	
	/**
	 * Moves the Elevator down to the floor the passenger needs to go.
	 * 
	 * @throws InterruptedException
	 */
	private void down() throws InterruptedException {
		
		if (curFloor == floorToGo) {
			System.out.print("Doors Open");
			door.open();
			Thread.sleep(1318);
		}
		else{
			System.out.print("Doors Closed");
			door.close();
			Thread.sleep(1318);
			while(curFloor != floorToGo) {
				motor.activateDown();
				curFloor--;
			}
			motor.disableMotor();
			System.out.print("Doors Open");
			door.open();
		}
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
		}
		else {
			up();
		}
	}
	
	
	/**
	 * Gets the destination from Scheduler and then moves the elevator as 
	 * per the passenger's request. 
	 * 
	 * @return true or false If the request array is null or not
	 * @throws InterruptedException 
	 */
	private boolean elevatorActivated() throws InterruptedException {
		System.out.println("ELEVATOR GETTING REQUEST");
		Optional<ElevatorEvent> opt = schedule.getRequest(this.curFloor);
		System.out.println("ELEVATOR RUNNING");
		if (opt.isEmpty()) {
			return false;
		}
		ElevatorEvent req = opt.get();
		
        if (req.getDirection().equals(Direction.UP)) {
        	System.out.println("Recieved Up Request: " + req.toString());
        	this.curFloor = Math.max(this.curFloor, req.getFloorToGo());
        } else {
        	System.out.println("Recieved Down Request: " + req.toString());
        	this.curFloor = Math.min(this.curFloor, req.getFloorToGo());
        }
		schedule.destinationReached(req);
		Thread.sleep(500);
		System.out.println("ELEVATOR OFF");
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
		while (true) {
			try {
				elevatorActivated();
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}

