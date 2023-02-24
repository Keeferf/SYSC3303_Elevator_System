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
	void testAddUpEvents() {
		Scheduler s = new Scheduler(new FloorSubsystem(5));
		s.newRequest(new ElevatorEvent(this, "14:05:15", Direction.UP, 0, 2));
		assertEquals(1, s.getIncomingQueueLength());
		s.validateRequest();
		assertEquals(1, s.getUpQueueLength());
		s.newRequest(new ElevatorEvent(this, "18:38:21", Direction.UP, 1, 2));
		assertEquals(1, s.getIncomingQueueLength());
		s.validateRequest();
		assertEquals(2, s.getUpQueueLength());
		s.newRequest(new ElevatorEvent(this, "20:01:34", Direction.UP, 1, 3));
		assertEquals(1, s.getIncomingQueueLength());
		s.validateRequest();
		assertEquals(3, s.getUpQueueLength());
	}
	
	@Test
	void testAddDownEvents() {
		Scheduler s = new Scheduler(new FloorSubsystem(5));
		s.newRequest(new ElevatorEvent(this, "14:05:15", Direction.DOWN, 4, 2));
		assertEquals(1, s.getIncomingQueueLength());
		s.validateRequest();
		assertEquals(1, s.getDownQueueLength());
		s.newRequest(new ElevatorEvent(this, "18:38:21", Direction.DOWN, 3, 0));
		assertEquals(1, s.getIncomingQueueLength());
		s.validateRequest();
		assertEquals(2, s.getDownQueueLength());
		s.newRequest(new ElevatorEvent(this, "20:01:34", Direction.DOWN, 2, 1));
		assertEquals(1, s.getIncomingQueueLength());
		s.validateRequest();
		assertEquals(3, s.getDownQueueLength());
	}

	public class TestFloorSubsystem extends FloorSubsystem {
		
		private int numRequestsProcessed;
		
		public TestFloorSubsystem(int n) {
			super(n);
			this.numRequestsProcessed = 0;
		}

		@Override
		public void alert(ElevatorEvent e) {
			this.numRequestsProcessed++;
		}
		
		public int getNumRequests() {
			return super.numRequests;
		}
		
		public int getNumRequestsProcessed() {
			return this.numRequestsProcessed;
		}
	}
	
	@Test
	void testProcessEvents() {
		TestFloorSubsystem f = new TestFloorSubsystem(5);
		Scheduler s = new Scheduler(f);
		Elevator e = new Elevator(0, 4, s);
		Thread sT = new Thread(s);
		Thread eT = new Thread(e);
		Thread fT = new Thread(f);
		sT.start();
		eT.start();
		fT.start();
		assertEquals(f.getNumRequests(), f.getNumRequestsProcessed());
		assertEquals(0, s.getIncomingQueueLength());
		assertEquals(0, s.getUpQueueLength());
		assertEquals(0, s.getDownQueueLength());
		assertEquals(0, s.getUpQueueLength());
		assertEquals(0, s.getResponseQueueLength());
	}
}
