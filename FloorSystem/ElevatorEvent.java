package FloorSystem;

import java.util.EventObject;

/**
 * Elevator event handles the input of events
 */
public class ElevatorEvent extends EventObject implements Comparable<ElevatorEvent> {

	private static final long serialVersionUID = 1L;
	private final String timestamp;
    private final Direction direction;
    private final int floorToGo;
    private final int currFloor;

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
    	return timestamp + ";" + currFloor + ";" + direction.toString() + ";" + floorToGo;
    }

    /**
     * A method to comapre the current floor with the event floor
     */
    @Override
	public int compareTo(ElevatorEvent e) {
		return this.currFloor - e.currFloor;
	}
}
