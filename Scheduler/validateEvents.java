package Scheduler;

public class validateEvents implements schedulerState {
	private Scheduler s;
	
	public validateEvents(Scheduler s) {
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
		if (s.getIncomingQueueLength() == 0) {
			System.out.println("Validate -> Send");
			s.setState(new sendEvents(s));
		}
	}
}
