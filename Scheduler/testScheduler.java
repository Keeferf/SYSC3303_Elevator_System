package Scheduler;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import Elevator.Elevator;
import FloorSystem.Direction;
import FloorSystem.ElevatorEvent;
import FloorSystem.FloorSubsystem;

/**
 * Test Class for the Scheduler class
 * @author Colin Mandeville 101140289
 */
class testScheduler {

	@Test
	public void testNewRequest() {
		Scheduler scheduler = new Scheduler();
		ElevatorEvent event = new ElevatorEvent(this, "14:05:15", Direction.UP, 0, 2);
		scheduler.newRequest(event);
		assertEquals(1, scheduler.getIncomingQueueLength());
	}
	
	@Test
	public void testSendResponse() {
		Scheduler scheduler = new Scheduler();
		ElevatorEvent event = new ElevatorEvent(this, "14:05:15", Direction.UP, 0, 2);
		scheduler.newRequest(event);
		assertEquals(0, scheduler.getValidQueueLength());
		assertEquals(0, scheduler.getResponseQueueLength());
	}

	@Test
	void testProcessEvents() {
		FloorSubsystem f = new FloorSubsystem();
		Scheduler s = new Scheduler();
		Elevator e = new Elevator();
		Thread sT = new Thread(s);
		Thread eT = new Thread(e);
		Thread fT = new Thread(f);
		sT.start();
		eT.start();
		fT.start();
		assertEquals(0, s.getIncomingQueueLength());
		assertEquals(0, s.getValidQueueLength());
		assertEquals(0, s.getValidQueueLength());
		assertEquals(0, s.getValidQueueLength());
		assertEquals(0, s.getResponseQueueLength());
	}
}
