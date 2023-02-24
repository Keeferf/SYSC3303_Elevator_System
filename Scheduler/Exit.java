package Scheduler;

public class Exit implements SchedulerState {
	
	public void executeState() {
		this.checkStateChange();
	}

	public void checkStateChange() {
		System.exit(0);
	}
}
