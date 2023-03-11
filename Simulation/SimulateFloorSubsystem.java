package Simulation;

import java.net.SocketException;

import FloorSystem.FloorSubsystem;

public class SimulateFloorSubsystem {

	public static void main(String[] args) {
		
		FloorSubsystem sub = new FloorSubsystem(5);
		Thread thread = new Thread(sub, "FloorSubsystem");
		thread.start();
		

	}

}
