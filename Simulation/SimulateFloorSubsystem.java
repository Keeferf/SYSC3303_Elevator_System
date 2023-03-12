package Simulation;

import FloorSystem.FloorSubsystem;

/**
 * The simulate floor subsystem handles the initialization of the floor subsystem threads
 */
public class SimulateFloorSubsystem {
	public static void main(String[] args) {
		FloorSubsystem sub = new FloorSubsystem();
		Thread thread = new Thread(sub, "FloorSubsystem");
		thread.start();
	}
}
