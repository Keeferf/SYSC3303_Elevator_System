package Scheduler;

import java.util.EventObject;

public class FloorButtonEvent extends EventObject {
	
	private final int startFloor;
	private final String direction;
	private final int destination;

	public FloorButtonEvent(Object source, int startFloor, String direction, int destination) {
		super(source);
		this.startFloor = startFloor;
		this.direction = direction;
		this.destination = destination;
	}
	
	public int getStartFloor() {
		return this.startFloor;
	}
	
	public String getDirection() {
		return this.direction;
	}
	
	public int getDestination() {
		return this.destination;
	}}
