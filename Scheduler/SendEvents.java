package Scheduler;

public class SendEvents implements SchedulerState {
	
	private Scheduler s;

	public SendEvents(Scheduler s) {
		this.s = s;
	}

	@Override
	public void executeState() {
		this.s.notifyAll();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void checkStateChange() {
		if (s.getResponseQueueLength() == 0) {
			System.out.println("SendEvents -> Idle");
			s.setState(new Idle(s));
		} else if (s.getUpQueueLength() == 0 && s.getDownQueueLength() == 0) {
			System.out.println("SendEvents -> ReturnResponse");
			s.setState(new ReturnResponse(s));
		}
	}
}
