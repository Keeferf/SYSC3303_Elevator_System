package Elevator;

import java.util.Date;

/**
 * Superclass containing methods to track the timing of a state
 * @author Colin Mandeville - 101140289
 */
public class MeasurableState {
	protected Date startTime;
	
	protected MeasurableState() {
		this.startTime = new Date();
	}
	
	protected Date getStartTime() {
		return this.startTime;
	}
	
	protected void setStartTime() {
		this.startTime = new Date();
	}
}
