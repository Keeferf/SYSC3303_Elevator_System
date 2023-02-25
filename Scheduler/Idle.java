package Scheduler;

public class Idle implements SchedulerState {
	
	private Scheduler s;
	
	public Idle(Scheduler s) {
		this.s = s;
	}

	@Override
	public void executeState() {
		while(s.getState() == this) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.checkStateChange();
		}
	}

	@Override
	public void checkStateChange() {
		if (s.getIncomingQueueLength() > 0) {
			System.out.println("Idle -> ValidateEvents");
			s.setState(new ValidateEvents(s));
		} else if (s.getResponseQueueLength() > 0) {
			System.out.println("Idle -> ReturnResponse");
			s.setState(new ReturnResponse(s));
		}
	}

}
