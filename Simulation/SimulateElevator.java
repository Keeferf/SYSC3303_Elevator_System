package Simulation;

import java.net.SocketException;

import Elevator.Elevator;
import FloorSystem.FloorSubsystem;
import Scheduler.Scheduler;

public class SimulateElevator {

	public static void main(String[] args) {
		

		Scheduler s = new Scheduler();
		Elevator elevator = new Elevator();
		Thread thread = new Thread(elevator, "Elevator");
		thread.start();

	}

}
