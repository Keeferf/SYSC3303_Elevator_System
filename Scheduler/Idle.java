package Scheduler;

import java.io.IOException;

/**
 * Idle state for the scheduler, which occurs when there are not requests to process, but more requests will be sent
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
		while(s.getState() == this) {	
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
			System.out.println("Scheduler: Idle -> ValidateEvents\n");
			s.setState(new ValidateEvents(s));
		} else if (s.getValidQueueLength() > 00) {
			System.out.println("Scheduler: Idle -> SendEvents\n");
			s.setState(new SendEvents(s));
		} else if (s.getResponseQueueLength() > 0) {
			System.out.println("Scheduler: Idle -> ReturnResponse\n");
			s.setState(new ReturnResponse(s));
		} else if (s.isEnd()) {
			System.out.println("Scheduler: Idle -> Exit\n");
			s.setState(new Exit(this.s));
		}
	}

}
