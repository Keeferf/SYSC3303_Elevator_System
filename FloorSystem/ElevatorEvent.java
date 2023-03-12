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
public class ElevatorEvent extends EventObject implements Comparable<ElevatorEvent>, Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	private final String timestamp;
    private final Direction direction;
    private final int floorToGo;
    private final int currFloor;
    
    private int seconds;
    
    private RequestStatus requestStatus;

    /**
     * Constructor for the elevator event class
     * @param source: Object
     * @param timestamp: String
     * @param direction: Direction
     * @param floorToGo: Int
     * @param currFloor: Int
     */
    public ElevatorEvent(Object source, String timestamp, Direction direction, int floorToGo, int currFloor) {
        super(source);
        this.timestamp = timestamp;
        this.direction = direction;
        this.floorToGo = floorToGo;
        this.currFloor = currFloor;
        
        seconds = convertToSeconds(timestamp);
        
        //New elevator events always start off at "NEW" status
        
        requestStatus = RequestStatus.NEW;
    }
    
    public ElevatorEvent(Object source) {
    	super(source);
    	this.timestamp = "";
		this.direction = null;
		this.floorToGo = 0;
		this.currFloor = 0;
		
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
     * Serialization methods provided by SerializationUtils
     * 
     * https://github.com/apache/commons-lang/blob/master/src/main/java/org/apache/commons/lang3/SerializationUtils.java
     */
    public static byte[] serialize(final Serializable obj) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        serialize(obj, baos);
        return baos.toByteArray();
    }
    
    @SuppressWarnings("resource") // outputStream is managed by the caller
    public static void serialize(final Serializable obj, final OutputStream outputStream) {
        Objects.requireNonNull(outputStream, "outputStream");
        try (ObjectOutputStream out = new ObjectOutputStream(outputStream)) {
            out.writeObject(obj);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static <T> T deserialize(final byte[] objectData) {
        Objects.requireNonNull(objectData, "objectData");
        return deserialize(new ByteArrayInputStream(objectData));
    }
    
    @SuppressWarnings("resource") // inputStream is managed by the caller
    public static <T> T deserialize(final InputStream inputStream) {
        Objects.requireNonNull(inputStream, "inputStream");
        try (ObjectInputStream in = new ObjectInputStream(inputStream)) {
            @SuppressWarnings("unchecked")
            final T obj = (T) in.readObject();
            return obj;
        } catch (final ClassNotFoundException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    
}
