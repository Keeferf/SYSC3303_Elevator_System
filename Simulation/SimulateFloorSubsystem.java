package Simulation;

import FloorSystem.FloorSubsystem;

public class SimulateFloorSubsystem {
	public static void main(String[] args) {
		FloorSubsystem sub = new FloorSubsystem();
		Thread thread = new Thread(sub, "FloorSubsystem");
		thread.start();
	}
}
