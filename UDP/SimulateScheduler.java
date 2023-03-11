package UDP;

import java.net.SocketException;

import FloorSystem.FloorSubsystem;
import Scheduler.Scheduler;

public class SimulateScheduler {

	public static void main(String[] args) {
		try {
			
			Scheduler scheduler = new Scheduler(new FloorSubsystem(5));
			Thread thread = new Thread(scheduler, "Scheduler");
			thread.start();
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 

	}

}
