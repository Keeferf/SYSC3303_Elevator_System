package Util.Timer;

import java.lang.reflect.Method;

import Scheduler.FaultHandler.ElevatorTimingEvent;
import Scheduler.FaultHandler.FaultHandler;

/**
 * A class that executes methods/functions after a certain period of time
 * @author Nicholas Rose - 101181935
 *
 */
public class TimerN implements Runnable {
	
	/**
	 * Starts a timer that will execute after n milliseconds
	 * Return call will carry payload passed
	 * @param n - time in ms
	 * @param payload Object
	 * @param reference Timeable
	 */
	public synchronized static TimerN startTimer(int n, Object payload, Timeable reference) {
		
		//Creates a new timer to go off on its own
		TimerN timer = new TimerN(n,payload,reference);
		Thread thread = new Thread(timer);
		
		thread.start();
		
		return timer;
	}
	
	/**
	 * Starts a timer that will execute after n milliseconds
	 * Return call will carry payload of null
	 * @param n - time in ms
	 * @param reference Timeable
	 */
	public synchronized static TimerN startTimer(int n, Timeable reference) {
		
		//Creates a new timer to go off on its own
		TimerN timer = new TimerN(n, null, reference);
		Thread thread = new Thread(timer);
		
		thread.start();
		
		return timer;
	}
	
	/**
	 * Same as first, just takes double
	 * @param computeStateTime
	 * @param ete
	 * @param reference2
	 * @return
	 */
	public static TimerN startTimer(double n, Object payload, Timeable reference) {
		//Creates a new timer to go off on its own
		TimerN timer = new TimerN(n,payload,reference);
		Thread thread = new Thread(timer);
		
		thread.start();
		
		return timer;
	}
	
	//Creates new thread. Sleeps for time
	//After, notifys the calling object
	private double time;
	private Object payload;
	private Timeable reference;
	
	private boolean timerKilled;
	
	public TimerN(int n, Object payload, Timeable reference) {
		time = n;
		this.payload = payload;
		this.reference = reference;
		
		timerKilled = false;
	}
	

	public TimerN(double n, Object payload, Timeable reference) {
		time = n;
		this.payload = payload;
		this.reference = reference;
		
		timerKilled = false;
	}

	@Override
	public void run() {
		//System.out.println("Timer started for " + time + "s");
		try {
			Thread.sleep((int)(time * 1000));
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		synchronized (reference) {
			if(!timerKilled) {System.out.println("Timer killed: " + timerKilled);reference.timerFinished(payload);}
		}
		
		
		
	}
	
	/**
	 * Used to disable the timer mid execution
	 */
	public void killTimer() {
		synchronized (reference) {
			System.out.println("Killed Timer");
			timerKilled = true;
		}
	}
	
	public boolean isKilled() {
		return timerKilled;
		}

	
}