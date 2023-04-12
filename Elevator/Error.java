package Elevator;

import java.net.DatagramPacket;

import FloorSystem.ElevatorEvent;
import Util.Comms.Config;

public class Error implements ElevatorState {
	
	private boolean doorError;
	private Elevator elevator;
	
	public Error(Elevator source, boolean doorError) {
		this.elevator = source;
		this.doorError = doorError;
	}

	@Override
	public void runState() {
		if (this.doorError) {
			Config.printLine();
			System.out.println("Elevator " + this.elevator.getID() + " attempting to close door again");
			Config.printLine();
			elevator.errorState = ErrorState.DOOR_ERROR;
			elevator.sendStateEvent();
			this.checkState();
		} else {
			this.exit();
		}
	}

	@Override
	public void checkState() {
		// This method exists since we can't use IRL Hardware to determine the reason for the door fault
		// Ideally it would verify if the issue is with the hardware, in which case the elevator would 
		// require maintenance, or due to a non system issue such as a passenger blocking the door, 
		// in which case the elevator would just try to close the door again
		this.elevator.handleDoorFault();
		//Will always be handled
		elevator.errorState = ErrorState.NO_ERROR;
		elevator.sendStateEvent();
		this.elevator.setState(new DoorOpenState(this.elevator));
	}
	
	private void exit() {
		elevator.errorState = ErrorState.MOTOR_ERROR;
		elevator.sendStateEvent();
		Config.printLine();
		System.out.println("Elevator Motor Experiencing Difficulties, Elevator Process Ending, Awaiting Maintenance");
		Config.printLine();
		this.elevator.exit();
	}

}
