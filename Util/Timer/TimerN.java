package Util.Timer;

import java.lang.reflect.Method;

import Scheduler.FaultHandler.ElevatorTimingEvent;
import Scheduler.FaultHandler.FaultHandler;

/**
 * A class that executes methods/functions after a certain period of time
 * @author Nicholas Rose - 101181935
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
		
		return timer;
	}
	
	//Creates new thread. Sleeps for time
	//After, notifys the calling object
	private double time;
	private Object payload;
	private Timeable reference;
	
	private volatile boolean timerKilled;
	
	/**
	 * Method to start a new timer as a thread and start it
	 * @param n number of seconds to set the timer for, i.e. 3 indicates the timer will wait for 3 seconds before triggering
	 * @param payload Package to be passed to reference when the timer ends
	 * @param reference Object to pass the payload to when the timer ends
	 */
	public TimerN(int n, Object payload, Timeable reference) {
		time = n;
		this.payload = payload;
		this.reference = reference;
		
		timerKilled = false;
		
		Thread thread = new Thread(this);
		
		thread.start();
	}

	/**
	 * Method to start a new timer as a thread and start it
	 * @param n number of seconds to set the timer for, i.e. 3.543 indicates the timer will wait for 3.543 seconds before triggering
	 * @param payload Package to be passed to reference when the timer ends
	 * @param reference Object to pass the payload to when the timer ends
	 */
	public TimerN(double n, Object payload, Timeable reference) {
		time = n;
		this.payload = payload;
		this.reference = reference;
		
		timerKilled = false;
		
		Thread thread = new Thread(this);
		
		thread.start();
	}

	/**
	 * Run method for the timer, it will cause th ethread to sleep for the time duration, then pass the payload to the reference
	 */
	@Override
	public void run() {
		try {
			Thread.sleep((int)(time * 1000));
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		if(!timerKilled) {reference.timerFinished(payload);}
	}
	
	/**
	 * Used to disable the timer mid execution
	 */
	public void killTimer() {
		timerKilled = true;
	}
	
	/**
	 * Getter method for the state of the timer, if the timer is killed it will return true, otherwise return false
	 * @return Boolean representing if the timer has been killed or not
	 */
	public boolean isKilled() {
		return timerKilled;
	}
}