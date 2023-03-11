package Simulation;
import Elevator.Elevator;
import FloorSystem.FloorSubsystem;
import Scheduler.Scheduler;

/**
 * Simulation Class which contains the main run method
 * @author Colin Mandeville
 */
public class Simulation {
	public static void main(String[] args) {
		FloorSubsystem f = new FloorSubsystem();
		Scheduler s = new Scheduler();
		
    	Thread sch = new Thread(s);
    	Thread el = new Thread(new Elevator());
    	Thread e2 = new Thread(new Elevator());
    	Thread e3 = new Thread(new Elevator());
    	Thread e4 = new Thread(new Elevator());
    	Thread fl = new Thread(f);

    	sch.start();
    	el.start();
    	e2.start();
    	e3.start();
    	e4.start();
    	fl.start();
    }
}
