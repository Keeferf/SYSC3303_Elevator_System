package Elevator;

import java.util.ArrayList;
import java.util.Optional;
import FloorSystem.ElevatorEvent;
import Scheduler.Scheduler;


public class Elevator implements Runnable{
	
	private static final int maxFloor = 22;
	private static final int groundFloor = 0;
	private final int id;
	private int currFloor;
	private int floorToGo;
	private final Scheduler schedule;
	private ElevatorState state;
	
	private final ArrayList<ElevatorLamp> lamps;
	private final ArrayList<ElevatorButton> buttons;
	private final ElevatorDoor door;
	private final ElevatorMotor motor;
	private ElevatorEvent req;
	
	private static int idCounter = 1;
	
	public Elevator(Scheduler sc) {
		this.id = Elevator.idCounter;
		idCounter++;
		this.currFloor = groundFloor;
		this.schedule = sc;
		this.state = new IdleState(this);
		
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
	 * 
	 * @throws InterruptedException
	 */
	private void up() throws InterruptedException {
		if (currFloor == floorToGo) {
			motor.activateDown();
			currFloor++;
			motor.disableMotor();
			door.open();
		}
		else{
			motor.activateDown();
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
			door.open();
		} else{
			motor.activateDown();
			currFloor--;
			motor.disableMotor();
			door.open();
		}
	}
	
	/**
	 * Moves the Elevator up or down depending on the floor the passenger's request.
	 * 
	 * @throws InterruptedException
	 */
	public void moveElevator() throws InterruptedException {
		for(ElevatorLamp l: lamps) {if(l.getFloorNum() == floorToGo) l.setState(true);}
		for(ElevatorButton b: buttons) {if(b.getFloorNum() == floorToGo) b.toggle(true);}
		
		if (floorToGo < currFloor) {
			down();
		} else if (floorToGo > currFloor){
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
	public void elevatorActivated() throws InterruptedException {
		Optional<ElevatorEvent> opt = schedule.getRequest(this.currFloor, this.id);
		if (opt.isPresent()) {
			this.req = opt.get();
			System.out.println("Elevator " + this.id + " Received Request: " + this.req.toString());
			
			System.out.println("Elevator " + this.id + " at floor " + this.currFloor + "\n");
			
			this.state.checkState();
			this.state.runState();
		}
	}
	
	/**
	 * Returns the Current floor that the Elevator is on.
	 * 
	 * @return currFloor
	 */
	public int getCurFloor() {
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
	
	///////////////////////////
	//Component Utility
	///////////////////////////
	public void arrivedAtFloor(int floorNum) {
		System.out.println("Elevator " + this.id + " arrived at " + this.currFloor + "\n");
		for(ElevatorLamp l: lamps) {if(l.getFloorNum() == floorNum) l.setState(false);}
		for(ElevatorButton b: buttons) {if(b.getFloorNum() == floorNum) b.toggle(false);}
		this.schedule.destinationReached(this.req);
	}
	
	/**
	 * Run method for the Thread.
	 */
	@Override
	public void run() {
		this.state.runState();
	}
}

