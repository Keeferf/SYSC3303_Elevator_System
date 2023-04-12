package Scheduler;

/**
 * Scheduler State to validate requests which are incoming to the Scheduler
 * @author Colin Mandeville
 */
public class ValidateEvents implements SchedulerState {
	private Scheduler s;
	
	/**
	 * Constructor for the Validate Events state
	 * @param s Scheduler instance
	 */
	public ValidateEvents(Scheduler s) {
		this.s = s;
	}

	/**
	 * execute state method causes the scheduler to validate requests sent by the floor subsystem
	 */
	@Override
	public void executeState() {
		while(s.getState() == this) {
			s.validateRequest();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * checkStateChange method causes the scheduler to change to Idle if there are no valid requests, or send events if there are
	 */
	@Override
	public void checkStateChange() {
		if (s.getValidQueueLength() == 0) {
			System.out.println("Scheduler: ValidateEvents -> Idle\n");
			s.setState(new Idle(s));
		} else if (s.getIncomingQueueLength() == 0) {
			System.out.println("Scheduler: ValidateEvents -> SendEvents\n");
			s.setState(new SendEvents(s));
		}
	}
}
