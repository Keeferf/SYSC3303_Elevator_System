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
