package Elevator;


import Scheduler.FaultHandler.ElevatorTimingState;
import Util.Comms.Config;

import java.util.Date;

import Elevator.Components.ElevatorArrivalSensor;


/**
 * Door open state class to handle the doors of the elevator states
 */
public class DoorOpenState extends MeasurableState implements ElevatorState {

	private Elevator elevator;
	private ElevatorArrivalSensor sensor;
	
	/**
	 * Constructor for door open state class
	 * @param elevator
	 */
	public DoorOpenState(Elevator elevator) {
		super();
		this.elevator = elevator;
		this.sensor = new ElevatorArrivalSensor();
	}

	/**
	 * A method to run the elevator state(open doors)
	 */
	@Override
	public void runState() {
		System.out.println("Elevator " + this.elevator.getID() + " Arrived on floor " + this.elevator.getCurrFloor() + "\n");
		try {
			Thread.sleep(Config.getElevatorTimeDoorsOpen());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		elevator.sendTimingEvent(ElevatorTimingState.DOOR_OPEN);
		this.checkState();		
	}

	/**
	 * A method to check the state and activate the door open state
	 */
	@Override
	public void checkState() {
		Date exitTime = new Date();
		Config.printLine();
		System.out.println("Elevator " + this.elevator.getID() + "\nEntered Open Door State At " + super.startTime.getTime() + "ms\nExiting Open Door State At " + exitTime.getTime() + "ms\nExpended Time In Open Door State Is " + (exitTime.getTime() - super.startTime.getTime()) + "ms");
		Config.printLine();
		if(elevator.getCurrFloor() == this.elevator.getRequest().getFloorToGo()) {
			this.sensor.setHasArrived(true);
			System.out.println("Elevator " + this.elevator.getID() + " Door Open -> Door Closed\n");
			this.elevator.setState(new DoorClosedState(this.elevator));
		} else {
			this.sensor.setHasArrived(false);
			System.out.println("Elevator " + this.elevator.getID() + " Door Open -> Door Closed\n");
			this.elevator.setState(new DoorClosedState(this.elevator));
		}
	}
}