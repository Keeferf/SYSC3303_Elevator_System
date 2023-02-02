package Scheduler;

import java.util.EventObject;

import FloorSystem.Direction;

public class ButtonEvent extends EventObject {
	
	private final int startFloor;
	private final Direction direction;
	private final int destination;

	public ButtonEvent(Object source, String startFloor, Direction direction, String destination) {
		super(source);
		this.startFloor = Integer.parseInt(startFloor);
		this.direction = direction;
		this.destination = Integer.parseInt(destination);
	}
	
	public int getStartFloor() {
		return this.startFloor;
	}
	
	public Direction getDirection() {
		return this.direction;
	}
	
	public int getDestination() {
		return this.destination;
	}
	
}
