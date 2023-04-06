package FloorSystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.EventObject;
import java.util.Objects;

import Util.Comms.RequestStatus;

/**
 * Elevator event handles the input of events
 */
public class ElevatorEvent implements Comparable<ElevatorEvent>, Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	private final String timestamp;
    private final Direction direction;
    private final int floorToGo;
    private final int currFloor;
    
    private int elevatorNum;
    
    private int seconds;
    
    private RequestStatus requestStatus;
	private boolean doorFault;
	private boolean motorFault;

    /**
     * Constructor for the elevator event classes with errors
     * 
     * Recently updated to not extend event object.
     * Was throwing errors since event received via UDP and cannot find source
     * Source wasn't used in any client files
     * @param source: Object
     * @param timestamp: String
     * @param direction: Direction
     * @param floorToGo: Int
     * @param currFloor: Int
     */
    public ElevatorEvent(Object source, String timestamp, Direction direction, int floorToGo, int currFloor, boolean doorFault, boolean motorFault) {
        //super(source);
        this.timestamp = timestamp;
        this.direction = direction;
        this.floorToGo = floorToGo;
        this.currFloor = currFloor;
        this.doorFault = doorFault;
		this.motorFault = motorFault;
        
        seconds = convertToSeconds(timestamp);
        
        elevatorNum = -1;
        
        //New elevator events always start off at "NEW" status
        
        requestStatus = RequestStatus.NEW;
    }
    
    /**
     * Constructor for the elevator event class, calls primary constructor with no errors
     * 
     * @param source: Object
     * @param timestamp: String
     * @param direction: Direction
     * @param floorToGo: Int
     * @param currFloor: Int
     */
    public ElevatorEvent(Object source, String timestamp, Direction direction, int floorToGo, int currFloor) {
    	this(source, timestamp, direction, floorToGo, currFloor, false, false);
    }
    
    /**
     * Custom Constructor for making null elevator requests for work
     */
    public ElevatorEvent(Object source, RequestStatus r) {
//    	this(source, "", null, 0, 0, false, false);
    	//super(source);
    	this.timestamp = "";
		this.direction = null;
		this.floorToGo = 0;
		this.currFloor = 0;
		
		elevatorNum = -1;
		
		requestStatus = r;
	}
    
    /**
     * Custom Constructor for making null elevator requests for work
     */
    public ElevatorEvent(Object source) {
//    	this(source, "", null, 0, 0, false, false);
    	//super(source);
    	this.timestamp = "";
		this.direction = null;
		this.floorToGo = 0;
		this.currFloor = 0;
		
		elevatorNum = -1;
		
		requestStatus = RequestStatus.REQUEST;
	}

	/**
     * Converts the given string timestamp from events.txt into a value of seconds
     * @param timestamp
     * @return
     */
    private int convertToSeconds(String timestamp) {
    	int secs = 0;
    	String[] vals = timestamp.split(":");
    	secs += (Integer.parseInt(vals[0]) * 60 * 60 );//hours
    	secs += (Integer.parseInt(vals[1]) * 60);//minutes
    	secs += (Integer.parseInt(vals[2].substring(0, 2)));//seconds
    	
    	return secs;
    }

    /**
     * Getter for the timestampe of events
     * @return timestamp: String
     */
    public String getTimestamp() {
        return timestamp;
    }
    
    /**
     * Getter for the direction of events
     * @return direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Getter for the destination floor
     * @return floorToGo: Int
     */
    public int getFloorToGo() {
        return floorToGo;
    }

    /**
     * Getter for the current floor
     * @return currFloor: Int
     */
    public int getCurrFloor() {
        return currFloor;
    }
    
    /**
     * A method to convert events to a string
     */
    @Override
    public String toString() {
    	if(direction == null) {
    		return "REQUEST";
    	}
    	else if(doorFault == true) {
    		return timestamp + ";" + currFloor + ";" + direction.toString() + ";" + floorToGo + ";" + "Door Fault: " + doorFault;
    	}
    	else if(motorFault == true) {
    		return timestamp + ";" + currFloor + ";" + direction.toString() + ";" + floorToGo + ";" + "Motor Fault: " + motorFault;
    	}
    	return timestamp + ";" + currFloor + ";" + direction.toString() + ";" + floorToGo;
    }

    /**
     * A method to comapre the current floor with the event floor
     */
    @Override
	public int compareTo(ElevatorEvent e) {
		return this.currFloor - e.currFloor;
	}
    
    @Override
    public Object clone() throws CloneNotSupportedException {
    	return super.clone();
    }
    
    public RequestStatus getRequestStatus() {
    	return requestStatus;
    }
    
    public void setRequestStatus(RequestStatus rs) {
    	requestStatus = rs;
    }
    
    public int getTimeAsSeconds() {
    	return seconds;
    }
    
    /**
     * Getter for the door fault boolean
     * @return doorFault: boolean
     */
    public boolean getDoorFault() {
        return doorFault;
    }
    
    /**
     * Getter for the motor fault boolean
     * @return motorFault: boolean
     */
    public boolean getMotorFault() {
		return motorFault;
	}
    
    /**
     * Sets the elevator that this event is dispatched to
     * @param n
     */
    public void setElevatorNum(int n) {
    	elevatorNum = n;
    }
    
    /**
     * Gets the elevator id that this event was dispatched to
     * @return
     */
    public int getElevatorNum() {
    	return elevatorNum;
    }

	
    
    
}
