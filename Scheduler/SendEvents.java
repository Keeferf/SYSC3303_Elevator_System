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

	@Override
	public void checkStateChange() {
		if (s.getResponseQueueLength() == 0 && s.getUpQueueLength() == 0 && s.getDownQueueLength() == 0) {
			System.out.println("SendEvents -> Idle");
			s.setState(new Idle(s));
		} else if (s.getUpQueueLength() == 0 && s.getDownQueueLength() == 0) {
			System.out.println("SendEvents -> ReturnResponse");
			s.setState(new ReturnResponse(s));
		} else {
			
		}
	}
}
