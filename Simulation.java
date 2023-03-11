import Elevator.Elevator;
import FloorSystem.FloorSubsystem;
import Scheduler.Scheduler;

/**
 * Simulation Class which contains the main run method
 * @author Colin Mandeville
 */
public class Simulation {
	public static void main(String[] args) {
		FloorSubsystem f = new FloorSubsystem(3);
		Scheduler s = new Scheduler(f);
		
		f.setScheduler(s);
		
    	Thread sch = new Thread(s);
    	Thread el = new Thread(new Elevator(s));
    	Thread e2 = new Thread(new Elevator(s));
    	Thread e3 = new Thread(new Elevator(s));
    	Thread e4 = new Thread(new Elevator(s));
    	Thread fl = new Thread(f);

    	sch.start();
    	el.start();
    	e2.start();
    	e3.start();
    	e4.start();
    	fl.start();
    }
}
