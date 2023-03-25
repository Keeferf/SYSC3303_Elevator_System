package testPackage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import FloorSystem.Direction;
import FloorSystem.ElevatorEvent;
import Util.Timer.Timeable;
import Util.Timer.TimerN;

class testTimerN implements Timeable{
	
	private boolean isFinished = false;

	@Test
	void test() {
		ElevatorEvent e = new ElevatorEvent(this, "00:00:15.0", Direction.UP, 3, 0);
		TimerN.startTimer(e.getTimeAsSeconds(), e, this);
		System.out.println("Timer 1 activated");
		
		try {
			Thread.sleep(16 * 1000);	
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			fail();
		}
		
		if(isFinished) {
			assertTrue(true);
		} else {
			fail();
		}
	}

	@Override
	public void timerFinished(Object payload) {
		System.out.println("Timer Finished");
		isFinished = true;
	}
	
	@Test
	void testTimerKill() {
		ElevatorEvent e = new ElevatorEvent(this, "00:00:15.0", Direction.UP, 3, 0);
		//TimerN timer = TimerN.startTimer(e.getTimeAsSeconds(), e, this);
		ArrayList<TimerN> timers = new ArrayList<>();
		
		TimerN timer = new TimerN(e.getTimeAsSeconds(),e,this);
		
		timers.add(timer);
		
		//System.out.println("Timer 1 activated");
		
		timers.get(0).killTimer();
		
		try {
			Thread.sleep(17 * 1000);	
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			fail();
		}
		
		if(!isFinished) {
			assertTrue(true);
		} else {
			fail();
		}
	}

}