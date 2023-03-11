package Simulation;

import Elevator.Elevator;

/**
 * The simulate elevator handles the creation and exucution of threads
 */
public class SimulateElevator {
	public static void main(String[] args) {
		Elevator elevator1 = new Elevator();
		Elevator elevator2 = new Elevator();
		Elevator elevator3 = new Elevator();
		Elevator elevator4 = new Elevator();
		Thread thread1 = new Thread(elevator1, "Elevator");
		Thread thread2 = new Thread(elevator2, "Elevator");
		Thread thread3 = new Thread(elevator3, "Elevator");
		Thread thread4 = new Thread(elevator4, "Elevator");
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();
	}
}
