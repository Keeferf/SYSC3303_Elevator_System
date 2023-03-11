package Scheduler;

import static org.junit.jupiter.api.Assertions.*;

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
	void testAddUpEvents() {
		Scheduler s = new Scheduler();
		s.newRequest(new ElevatorEvent(this, "14:05:15", Direction.UP, 0, 2));
		assertEquals(1, s.getIncomingQueueLength());
		s.validateRequest();
		assertEquals(1, s.getValidQueueLength());
		s.newRequest(new ElevatorEvent(this, "18:38:21", Direction.UP, 1, 2));
		assertEquals(1, s.getIncomingQueueLength());
		s.validateRequest();
		assertEquals(2, s.getValidQueueLength());
		s.newRequest(new ElevatorEvent(this, "20:01:34", Direction.UP, 1, 3));
		assertEquals(1, s.getIncomingQueueLength());
		s.validateRequest();
		assertEquals(3, s.getValidQueueLength());
	}
	
	@Test
	void testAddDownEvents() {
		Scheduler s = new Scheduler();
		s.newRequest(new ElevatorEvent(this, "14:05:15", Direction.DOWN, 4, 2));
		assertEquals(1, s.getIncomingQueueLength());
		s.validateRequest();
		assertEquals(1, s.getValidQueueLength());
		s.newRequest(new ElevatorEvent(this, "18:38:21", Direction.DOWN, 3, 0));
		assertEquals(1, s.getIncomingQueueLength());
		s.validateRequest();
		assertEquals(2, s.getValidQueueLength());
		s.newRequest(new ElevatorEvent(this, "20:01:34", Direction.DOWN, 2, 1));
		assertEquals(1, s.getIncomingQueueLength());
		s.validateRequest();
		assertEquals(3, s.getValidQueueLength());
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
