package FloorSystem;

import java.util.ArrayList;

import Elevator.Elevator;
import Scheduler.Scheduler;

public class Floor_Subsystem implements Runnable{
	
	private ArrayList<ElevatorEvent> ee;
	private Scheduler sc;
	private ArrayList<Floor> floors;
	private int numRequests;
	
	public Floor_Subsystem(int n) {
		this.floors = new ArrayList<Floor>();
		for(int i = 0; i < n; i++) {
			this.floors.add(new Floor(i));
		}
		this.numRequests = 0;
	}
	
	@Override
	public void run() {
		try {
			EventParser ep = new EventParser();
			ee = ep.getEvents();
			for(ElevatorEvent e: ee) {
				numRequests++;
				sc.newRequest(e);
			}
		}
		catch(Throwable e) {
		}
		while(true) {}
	}
	
	public void setScheduler(Scheduler sc) {
		this.sc = sc;
	}
	
	/**
	 * Prints out the request which was completed, i.e. the passenger arrived at their destination floor
	 * @param completedRequest Passenger request which was completed
	 */
	public void alert(ElevatorEvent completedRequest) {
		System.out.println(completedRequest.toString());
		numRequests--;
		if(numRequests == 0){
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		Floor_Subsystem f = new Floor_Subsystem(3);
		Scheduler s = new Scheduler(f);
		
		f.setScheduler(s);
		
    	Thread sch = new Thread(s);
    	Thread el = new Thread(new Elevator(2, 0, s));
    	Thread fl = new Thread(f);

    	sch.start();
    	el.start();
    	fl.start();
    }
}
