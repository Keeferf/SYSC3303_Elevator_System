package Scheduler;

public class sendEvents implements schedulerState {
	
	private Scheduler s;

	public sendEvents(Scheduler s) {
		this.s = s;
	}

	@Override
	public void executeState() {
		this.s.notifyAll();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void checkStateChange() {
		if (s.getUpQueueLength() == 0 && s.getDownQueueLength() == 0) {
			System.out.println("Send -> Response");
			s.setState(new returnResponse(s));
		}
	}
}
