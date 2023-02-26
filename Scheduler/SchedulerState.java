package Scheduler;

/**
 * Interface for Scheduler states
 * @author Colin Mandeville
 */
public interface SchedulerState {
	public void executeState();
	public void checkStateChange();
}
