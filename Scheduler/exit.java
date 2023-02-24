package Scheduler;

public class exit implements schedulerState {
	
	@Override
	public void executeState() {
		this.checkStateChange();
	}

	@Override
	public void checkStateChange() {
		System.exit(0);
	}
}
