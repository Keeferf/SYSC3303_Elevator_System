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
	
	/**
	 *  Constructor for Elevator State Events
	 * @param e Elevator event
	 * @param state Elevator Timing State
	 * @param currFloor Current floor the elevator is on, integer
	 * @param hasPassenger boolean for if the elevator has passengers or not
	 * @param elevatorNum ID of the elevator, integer
	 * @param errorState ErrorState object
	 */
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
	
	/**
	 * Getter for the current floor of the elevator
	 * @return Returns the current floor as an integer
	 */
	public int getCurrFloor() { return currFloor;}
	
	/**
	 * Getter for the starting floor for the elevator request aka the floor the passenger is on
	 * @return Returns the passenger's floor as an integer
	 */
	public int getStartFloor() { return startFloor;}
	
	/**
	 * Getter for the target floor aka the floor the passenger wants to go to
	 * @return Returns the target floor as an integer
	 */
	public int getTargetFloor() { return targetFloor;}
	
	/**
	 * Getter for the boolean of if the elevator has a passenger inside it
	 * @return Boolean, true = Passenger is in the elevator, false = No passenger is in the elevator
	 */
	public boolean hasPassenger() { return hasPassenger;}
	
	/**
	 * Getter for the elevator timing state
	 * @return Elevator Timing State
	 */
	public ElevatorTimingState getElevatorState() { return elevatorState;}
	
	/**
	 * Getter for the elevator id
	 * @return Returns the elevator id as an integer
	 */
	public int getElevatorNum()  {return elevatorNum;}
	
	/**
	 * Getter for the request being handled by the elevator
	 * @return ElevatorEvent being handled
	 */
	public ElevatorEvent getElevatorRequest() { return elevatorEvent; }
	
	/**
	 * Getter for the Error State
	 * @return ErrorState object
	 */
	public ErrorState getErrorState() {return errorState; }
	
	/**
	 * clone method which duplicates the object, and then returning that clone
	 */
	@Override
    public Object clone() throws CloneNotSupportedException {
    	return super.clone();
    }
	
	/**
	 * Convert the object to a string to be output to the console
	 */
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
