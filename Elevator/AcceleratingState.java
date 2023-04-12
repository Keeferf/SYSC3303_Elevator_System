package Elevator;

import java.util.Date;

import Elevator.Components.ElevatorArrivalSensor;
import FloorSystem.Direction;
import FloorSystem.ElevatorEvent;
import Scheduler.FaultHandler.ElevatorTimingState;
import Util.Comms.Config;

/**
 * Acceleration state class to handle the acceleration of the elevator states
 */
public class AcceleratingState extends MeasurableState implements ElevatorState{
	
	private Elevator elevator;
	private ElevatorArrivalSensor sensor;
	
	/**
	 * Constructor for the acceleration state class
	 * @param elevator
	 */
	public AcceleratingState(Elevator elevator) {
		super();
		this.elevator = elevator;
		this.sensor = new ElevatorArrivalSensor();
	}

	/**
	 * A method to run the elevator state(acceleration)
	 */
	@Override
	public void runState() {
		while(elevator.getState() == this) {
			this.checkState();
			if (this.elevator.getState() == this) {
				try {
					this.elevator.moveElevator();
					this.sensor.setHasArrived(false);
					System.out.println("Elevator " + this.elevator.getID() + " passes floor " + this.elevator.getCurrFloor());
					Thread.sleep(1318);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			elevator.sendTimingEvent(ElevatorTimingState.ACCELERATING);
		}
	}

	/**
	 * A method to check the state and activate the acceleration state
	 */
	@Override
	public void checkState() {
		ElevatorEvent req = elevator.getRequest();
		Date exitTime = new Date();
		if (req.getFloorToGo() == elevator.getFloorToGo()) {
			if (req.getDirection() == Direction.UP) {
				Config.printLine();
				System.out.println("Elevator " + this.elevator.getID() + "\nEntered Accelerating State At " + super.startTime.getTime() + "ms\nExiting Accelerating State At " + exitTime.getTime() + "ms\nExpended Time In Accelerating State Is " + (exitTime.getTime() - super.startTime.getTime()) + "ms");
				Config.printLine();
				super.setStartTime();
				if(elevator.getCurrFloor() == elevator.getFloorToGo() - 1) {
					System.out.println("Elevator " + elevator.getID() + ": Accelerate -> Decelerate\n");
					//elevator.sendTimingEvent(ElevatorTimingState.ACCELERATING);
					elevator.setState(new DeceleratingState(elevator));
				}
			} else {
				if(elevator.getCurrFloor() == elevator.getFloorToGo() + 1) {
					System.out.println("Elevator " + elevator.getID() + ": Accelerate -> Decelerate\n");
					elevator.setState(new DeceleratingState(elevator));
				}
			}
		} else {
			Config.printLine();
			System.out.println("Elevator " + this.elevator.getID() + "\nEntered Accelerating State At " + super.startTime.getTime() + "ms\nExiting Accelerating State At " + exitTime.getTime() + "ms\nExpended Time In Accelerating State Is " + (exitTime.getTime() - super.startTime.getTime()) + "ms");
			Config.printLine();
			super.setStartTime();
			if(elevator.getCurrFloor() >= elevator.getFloorToGo() - 1 && elevator.getCurrFloor() <= elevator.getFloorToGo() + 1) {
				System.out.println("Elevator " + elevator.getID() + ": Accelerate -> Decelerate\n");
				//elevator.sendTimingEvent(ElevatorTimingState.ACCELERATING);
				elevator.setState(new DeceleratingState(elevator));
			}
		}
		
	}
}