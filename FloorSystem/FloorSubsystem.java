package FloorSystem;

import java.util.ArrayList;

import Elevator.Elevator;
import Scheduler.Scheduler;

public class FloorSubsystem implements Runnable{
	
	private ArrayList<ElevatorEvent> ee;
	private Scheduler sc;
	private ArrayList<Floor> floors;
	private int numReqsProcessed;
	
	public FloorSubsystem(int n) {
		this.floors = new ArrayList<Floor>();
		for(int i = 0; i < n; i++) {
			this.floors.add(new Floor(i));
		}
		this.numReqsProcessed = 0;
	}
	
	/**
	 * Run method for the Floor Subsystem Thread
	 */
	@Override
	public void run() {
		try {
			EventParser ep = new EventParser();
			ee = ep.getEvents();
			System.out.println("Testing 0.1s delay between events\n");
			int i = 0;
			for(ElevatorEvent e: ee) {
				System.out.println("Sending Request: " + e.toString());
				sc.newRequest(e);
				i++;
				Thread.sleep(100);
			}
			while (i != this.numReqsProcessed) {
				Thread.sleep(1000);
			}
			System.out.println("\n\n\nTesting 1s delay between events\n");
			for(ElevatorEvent e: ee) {
				System.out.println("Sending Request: " + e.toString());
				sc.newRequest(e);
				i++;
				Thread.sleep(1000);
			}
			while (i != this.numReqsProcessed) {
				Thread.sleep(1000);
			}
			System.out.println("\n\n\nTesting 0.5s delay between events\n");
			for(ElevatorEvent e: ee) {
				System.out.println("Sending Request: " + e.toString());
				sc.newRequest(e);
				i++;
				Thread.sleep(500);
			}
			while (i != this.numReqsProcessed) {
				Thread.sleep(1000);
			}
			System.out.println("\n\n\nTesting 3s delay between events\n");
			for(ElevatorEvent e: ee) {
				System.out.println("Sending Request: " + e.toString());
				sc.newRequest(e);
				Thread.sleep(3000);
			}
			sc.endRequests();
		}
		catch(Throwable e) {}
	}
	
	/**
	 * Setter method for the Scheduler of the Floor Subsystem.
	 * @param sc
	 */
	public void setScheduler(Scheduler sc) {
		this.sc = sc;
	}
	
	/**
	 * Prints out the request which was completed, i.e. the passenger arrived at their destination floor
	 * @param completedRequest Passenger request which was completed
	 */
	public void alert(ElevatorEvent completedRequest) {
		System.out.println("Recieved Reponse for Request: " + completedRequest.toString());
		this.numReqsProcessed++;
	}
}
