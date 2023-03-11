package Simulation;

import java.net.SocketException;

import FloorSystem.FloorSubsystem;
import Scheduler.Scheduler;

public class SimulateScheduler {

	public static void main(String[] args) {
		
			Scheduler scheduler = new Scheduler();
			Thread thread = new Thread(scheduler, "Scheduler");
			thread.start();

	}

}
