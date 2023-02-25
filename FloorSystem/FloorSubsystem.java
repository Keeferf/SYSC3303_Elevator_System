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
	
	@Override
	public void run() {
		try {
			EventParser ep = new EventParser();
			ee = ep.getEvents();
			for(ElevatorEvent e: ee) {
				System.out.println("Sending Request: " + e.toString());
				sc.newRequest(e);
				Thread.sleep(100);
			}
			Thread.sleep(20000);
			for(ElevatorEvent e: ee) {
				System.out.println("Sending Request: " + e.toString());
				sc.newRequest(e);
				Thread.sleep(100);
			}
			Thread.sleep(20000);
			for(ElevatorEvent e: ee) {
				System.out.println("Sending Request: " + e.toString());
				sc.newRequest(e);
				Thread.sleep(100);
			}
			sc.endRequests();
		}
		catch(Throwable e) {}
	}
	
	public void setScheduler(Scheduler sc) {
		this.sc = sc;
	}
	
	/**
	 * Prints out the request which was completed, i.e. the passenger arrived at their destination floor
	 * @param completedRequest Passenger request which was completed
	 */
	public void alert(ElevatorEvent completedRequest) {
		System.out.println("Recieved Reponse for Request: " + completedRequest.toString());
	}
}
