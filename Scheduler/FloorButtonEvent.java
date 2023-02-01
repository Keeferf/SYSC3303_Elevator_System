package Scheduler;

import java.util.EventObject;

public class FloorButtonEvent extends EventObject {
	
	private final String startFloor;
	private final String direction;
	private final String destination;

	public FloorButtonEvent(Object source, String startFloor, String direction, String destination) {
		super(source);
		this.startFloor = startFloor;
		this.direction = direction;
		this.destination = destination;
	}
	
	public String getStartFloor() {
		return this.startFloor;
	}
	
	public String getDirection() {
		return this.direction;
	}
	
	public String getDestination() {
		return this.destination;
	}}
