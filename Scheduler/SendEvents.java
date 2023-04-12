package Scheduler;

/**
 * Scheduler State for sending events from the Scheduler to the Elevator
 * @author Colin Mandeville
 */
public class SendEvents implements SchedulerState {
	
	private Scheduler s;

	/**
	 * Constructor for the Send Events state
	 * @param s Scheduler instance
	 */
	public SendEvents(Scheduler s) {
		this.s = s;
	}

	/**
	 * execute state method allows the scheduler to dispatch events to the scheduler
	 */
	@Override
	public void executeState() {

		while(s.getState() == this) {	
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//send events here
			s.sendEvents();
			this.checkStateChange();
		}
	}

	/**
	 * checkStateChange mathod changes the scheduler state to Idle if there are no responses to send and no more events to dispatch, 
	 * or return response state if there are no more events to dispatch.
	 */
	@Override
	public void checkStateChange() {
		if (s.getResponseQueueLength() == 0 && s.getValidQueueLength() == 0) {
			System.out.println("Scheduler: SendEvents -> Idle\n");
			s.setState(new Idle(s));
		} else if (s.getValidQueueLength() == 0) {
			System.out.println("Scheduler: SendEvents -> ReturnResponse\n");
			s.setState(new ReturnResponse(s));
		}
	}
}
