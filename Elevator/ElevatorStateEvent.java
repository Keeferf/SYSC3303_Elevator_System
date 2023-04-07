package Elevator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Objects;

import FloorSystem.Direction;
import FloorSystem.ElevatorEvent;
import Scheduler.FaultHandler.ElevatorTimingState;

/*
 * A class for emcompassing all data for when a change occurs in an elevator
 * 
 * Nicholas Rose - 101181935
 */
public class ElevatorStateEvent implements Serializable, Cloneable{
	
	private final ElevatorEvent elevatorEvent;
	
	private final ElevatorTimingState elevatorState;
	private final int currFloor;
	private final int startFloor;
	private final int targetFloor;
	private final boolean hasPassenger;
	private final int elevatorNum;
	private ErrorState errorState;
	
	public ElevatorStateEvent(ElevatorEvent e, ElevatorTimingState state, int currFloor, boolean hasPassenger, int elevatorNum, ErrorState errorState) {
		elevatorEvent = e;
		
		this.hasPassenger = hasPassenger;
		elevatorState = state;
		this.currFloor = currFloor;
		startFloor = e.getCurrFloor();
		targetFloor = e.getFloorToGo();
		this.elevatorNum = elevatorNum;
		this.errorState = errorState;
	}
	
	public int getCurrFloor() { return currFloor;}
	public int getStartFloor() { return startFloor;}
	public int getTargetFloor() { return targetFloor;}
	public boolean hasPassenger() { return hasPassenger;}
	public ElevatorTimingState getElevatorState() { return elevatorState;}
	public int getElevatorNum()  {return elevatorNum;}
	public ElevatorEvent getElevatorRequest() { return elevatorEvent; }
	
	@Override
    public Object clone() throws CloneNotSupportedException {
    	return super.clone();
    }
	
	@Override
	public String toString() {
		String s = "";
		s += "=============\n";
		s += "State Update: Elevator " + elevatorNum + "\n";
		s += "Current Floor: " + currFloor + " | Passenger(s) : " + hasPassenger + " | Current State: " + elevatorState + " | Errors: " + errorState + "\n";
		s += "=============\n";
		
		return s;
	}

	
	
}
