package Scheduler;

/**
 * Scheduler State where it returns the responses of the Elevator to the Floor Subsystem
 * @author Colin Mandeville
 */
public class ReturnResponse implements SchedulerState {
	
	private Scheduler s;
	
	public ReturnResponse(Scheduler s) {
		this.s = s;
	}
	
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
	
	@Override
	public void checkStateChange() {
		if (s.isEnd() && this.s.getIncomingQueueLength() == 0 && this.s.getValidQueueLength() == 0 && this.s.getResponseQueueLength() == 0) {
			System.out.println("Scheduler: ReturnResponse -> Exit\n");
			s.setState(new Exit());
		} else if (this.s.getResponseQueueLength() == 0) {
			System.out.println("Scheduler: ReturnResponse -> Idle\n");
			s.setState(new Idle(s));
		}
	}
}
