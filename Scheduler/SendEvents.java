package Scheduler;

/**
 * Scheduler State for sending events from the Scheduler to the Elevator
 * @author Colin Mandeville
 */
public class SendEvents implements SchedulerState {
	
	private Scheduler s;

	public SendEvents(Scheduler s) {
		this.s = s;
	}

	@Override
	public void executeState() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.s.notifyAll();
		this.checkStateChange();
	}

	@Override
	public void checkStateChange() {
		if (s.getResponseQueueLength() == 0 && s.getValidQueueLength() == 0) {
			System.out.println("Scheduler: SendEvents -> Idle\n");
			s.setState(new Idle(s));
		} else if (s.getValidQueueLength() == 0) {
			System.out.println("Scheduler: SendEvents -> ReturnResponse\n");
			s.setState(new ReturnResponse(s));
		} else {
			this.s.notifyAll();
		}
	}
}
