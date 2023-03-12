package Simulation;

import Scheduler.Scheduler;

/**
 * The simulate scheduler initializes scheduler threads
 */
public class SimulateScheduler {

	public static void main(String[] args) {
			Scheduler scheduler = new Scheduler();
			Thread thread = new Thread(scheduler, "Scheduler");
			thread.start();
	}
}
