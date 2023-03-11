package Scheduler;

import java.io.IOException;

/**
 * Idle state for the cheduler, which occurs when there are not requests to process, but more requests will be sent
 * @author Colin Mandeville
 */
public class Idle implements SchedulerState {
	
	private Scheduler s;
	
	public Idle(Scheduler s) {
		this.s = s;
	}

	/**
	 * Execute state, sleep, and then check if the state changes, repeatedly
	 */
	@Override
	public void executeState() {
		while(s.getState() == this) {	//Might not work***
			try {
				this.s.receiveAndSend();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.checkStateChange();
		}
	}

	/**
	 * Check state, may transfer to any state depending on circumstance
	 */
	@Override
	public void checkStateChange() {
		if (s.getIncomingQueueLength() > 0) {
			System.out.println("Idle -> ValidateEvents");
			s.setState(new ValidateEvents(s));
		} else if (s.getUpQueueLength() > 0 || s.getDownQueueLength() > 0) {
			System.out.println("Idle -> SendEvents");
			s.setState(new SendEvents(s));
		} else if (s.getResponseQueueLength() > 0) {
			System.out.println("Idle -> ReturnResponse");
			s.setState(new ReturnResponse(s));
		} else if (s.isEnd()) {
			System.out.println("Idle -> Exit");
			s.setState(new Exit());
		}
	}

}
