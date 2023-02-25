package Scheduler;

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
		if (s.isEnd() && this.s.getIncomingQueueLength() == 0 && this.s.getUpQueueLength() == 0 && this.s.getDownQueueLength() == 0 && this.s.getResponseQueueLength() == 0) {
			System.out.println("ReturnResponse -> Exit");
			s.setState(new Exit());
		} else if (this.s.getResponseQueueLength() == 0) {
			System.out.println("ReturnResponse -> Idle");
			s.setState(new Idle(s));
		}
	}
}
