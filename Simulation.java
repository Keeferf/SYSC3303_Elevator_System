import Elevator.Elevator;
import FloorSystem.FloorSubsystem;
import Scheduler.Scheduler;

/**
 * Main Simulation class for Elevator Scheduling System
 * @author Colin Mandeville
 */
public class Simulation {
	public static void main(String[] args) {
		FloorSubsystem f = new FloorSubsystem(3);
		Scheduler s = new Scheduler(f);
		
		f.setScheduler(s);
		
    	Thread sch = new Thread(s);
    	Thread el = new Thread(new Elevator(22, 0, s));
    	Thread fl = new Thread(f);

    	sch.start();
    	el.start();
    	fl.start();
    }
}
