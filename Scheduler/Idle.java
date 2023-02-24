package Scheduler;

public class Idle implements schedulerState {
	
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
			System.out.println("Idle -> Validate");
			s.setState(new validateEvents(s));
		} else if (s.getUpQueueLength() > 0 || s.getUpQueueLength() > 0) {
			System.out.println("Idle -> Send");
			s.setState(new sendEvents(s));
			s.getState().executeState();
		} else if (s.getResponseQueueLength() > 0) {
			System.out.println("Idle -> Response");
			s.setState(new returnResponse(s));
			s.getState().executeState();
		}
	}

}
