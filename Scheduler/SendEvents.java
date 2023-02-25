package Scheduler;

public class SendEvents implements SchedulerState {
	
	private Scheduler s;

	public SendEvents(Scheduler s) {
		this.s = s;
	}

	@Override
	public void executeState() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.s.notifyAll();
	}

	@Override
	public void checkStateChange() {
		if (s.getResponseQueueLength() == 0) {
			System.out.println("SendEvents -> Idle");
			s.setState(new Idle(s));
		} else if (s.getUpQueueLength() == 0 && s.getDownQueueLength() == 0) {
			System.out.println("SendEvents -> ReturnResponse");
			s.setState(new ReturnResponse(s));
		} else {
			this.s.notifyAll();
		}
	}
}
