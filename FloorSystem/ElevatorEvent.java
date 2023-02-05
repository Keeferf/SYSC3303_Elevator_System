package FloorSystem;

import java.util.EventObject;

public class ElevatorEvent extends EventObject implements Comparable<ElevatorEvent> {

	private static final long serialVersionUID = 1L;
	private final String timestamp;
    private final Direction direction;
    private final int floorToGo;
    private final int currFloor;

    public ElevatorEvent(Object source, String timestamp, Direction direction, int floorToGo, int currFloor) {
        super(source);
        this.timestamp = timestamp;
        this.direction = direction;
        this.floorToGo = floorToGo;
        this.currFloor = currFloor;
    }

    //Getters
    public String getTimestamp() {
        return timestamp;
    }
    
    public Direction getDirection() {
        return direction;
    }

    public int getFloorToGo() {
        return floorToGo;
    }

    public int getCurrFloor() {
        return currFloor;
    }
    
    @Override
    public String toString() {
    	return timestamp + ";" + currFloor + ";" + direction.toString() + ";" + floorToGo;
    }

    @Override
	public int compareTo(ElevatorEvent e) {
		return this.currFloor - e.currFloor;
	}
}
