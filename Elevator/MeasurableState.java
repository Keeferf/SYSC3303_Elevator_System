package Elevator;

import java.util.Date;

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
