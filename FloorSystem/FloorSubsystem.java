package FloorSystem;

import java.util.ArrayList;

import Elevator.Elevator;
import Scheduler.Scheduler;

public class FloorSubsystem implements Runnable{
	
	private ArrayList<ElevatorEvent> ee;
	private Scheduler sc;
	private ArrayList<Floor> floors;
	
	public FloorSubsystem(int n) {
		this.floors = new ArrayList<Floor>();
		for(int i = 0; i < n; i++) {
			this.floors.add(new Floor(i));
		}
	}
	
	/**
	 * Run method for the Floor Subsystem Thread
	 */
	@Override
	public void run() {
		try {
			EventParser ep = new EventParser();
			ee = ep.getEvents();
			for(ElevatorEvent e: ee) {
				System.out.println("Sending Request: " + e.toString() + "\n");
				sc.newRequest(e);
				Thread.sleep(3000);
			}
			sc.endRequests();
		}
		catch(Throwable e) {}
	}
	
	/**
	 * Setter method for the Scheduler of the Floor Subsystem.
	 * @param sc Scheduler instance
	 */
	public void setScheduler(Scheduler sc) {
		this.sc = sc;
	}
	
	/**
	 * Prints out the request which was completed, i.e. the passenger arrived at their destination floor
	 * @param completedRequest Passenger request which was completed
	 */
	public synchronized void alert(ElevatorEvent completedRequest) {
		System.out.println("Floor Subsystem: Received Response for Request: " + completedRequest.toString() + "\n");
	}
}
