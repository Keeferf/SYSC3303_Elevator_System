package UDP;

import java.net.SocketException;

import Elevator.Elevator;
import FloorSystem.FloorSubsystem;
import Scheduler.Scheduler;

public class SimulateElevator {

	public static void main(String[] args) {
		
		try {
			Scheduler s = new Scheduler(new FloorSubsystem(5));
			Elevator elevator = new Elevator(10, 0, s);
			Thread thread = new Thread(elevator, "Elevator");
			thread.start();
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
