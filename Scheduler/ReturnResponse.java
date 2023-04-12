package Scheduler;

/**
 * Scheduler State where it returns the responses of the Elevator to the Floor Subsystem
 * @author Colin Mandeville
 */
public class ReturnResponse implements SchedulerState {
	
	private Scheduler s;
	
	/**
	 * Constructor for Return Response state
	 * @param s Scheduler instance
	 */
	public ReturnResponse(Scheduler s) {
		this.s = s;
	}
	
	/**
	 * execute state method makes the Scheduler return responses from the Elevators to the Floor Subsystem
	 */
	@Override
	public void executeState() {
		while(s.getState() == this) {
			this.s.returnResponse();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.checkStateChange();
		}
	}
	
	/**
	 * checkStateChange method causes the scheduler to exit or go idle if there are remaining requests
	 */
	@Override
	public void checkStateChange() {
		if (s.isEnd()) {
			System.out.println("Scheduler: ReturnResponse -> Exit\n");
			s.setState(new Exit(this.s));
		} else if (this.s.getResponseQueueLength() == 0) {
			System.out.println("Scheduler: ReturnResponse -> Idle\n");
			s.setState(new Idle(s));
		}
	}
}
