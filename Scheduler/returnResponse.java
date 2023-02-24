package Scheduler;

public class returnResponse implements schedulerState {
	
	private Scheduler s;
	
	public returnResponse(Scheduler s) {
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
			System.out.println("Return -> Exit");
			s.setState(new exit());
		} else if (this.s.getIncomingQueueLength() == 0 && this.s.getUpQueueLength() == 0 && this.s.getDownQueueLength() == 0 && this.s.getResponseQueueLength() == 0) {
			System.out.println("Return -> Idle");
			s.setState(new Idle(s));
		}
	}
}
