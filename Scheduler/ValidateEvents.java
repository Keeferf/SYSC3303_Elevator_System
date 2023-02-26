package Scheduler;

/**
 * Scheduler State to validate requests which are incoming to the Scheduler
 * @author Colin Mandeville
 */
public class ValidateEvents implements SchedulerState {
	private Scheduler s;
	
	public ValidateEvents(Scheduler s) {
		this.s = s;
	}

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

	@Override
	public void checkStateChange() {
		if (s.getUpQueueLength() == 0 && s.getDownQueueLength() == 0) {
			System.out.println("ValidateEvents -> Idle");
			s.setState(new Idle(s));
		} else if (s.getIncomingQueueLength() == 0) {
			System.out.println("ValidateEvents -> SendEvents");
			s.setState(new SendEvents(s));
		}
	}
}
