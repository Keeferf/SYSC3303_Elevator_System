package Util.Timer;

import java.lang.reflect.Method;

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
	
	//Creates new thread. Sleeps for time
	//After, notifys the calling object
	private int time;
	private Object payload;
	private Timeable reference;
	
	public TimerN(int n, Object payload, Timeable reference) {
		time = n;
		this.payload = payload;
		this.reference = reference;
	}
	

	@Override
	public void run() {
		try {
			Thread.sleep(time * 1000);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		if(timerKilled) return;
		
		reference.timerFinished(payload);
	}
	
	private boolean timerKilled = false;
	
	/**
	 * Used to disable the timer mid execution
	 */
	public void killTimer() {
		timerKilled = true;
	}
}