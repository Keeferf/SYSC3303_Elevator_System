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
		FloorSubsystem f = new FloorSubsystem(5);
		Scheduler s = new Scheduler(f);
		ElevatorEvent e1 = new ElevatorEvent(this, "14:05:15", Direction.UP, 0, 2);
		ElevatorEvent e2 = new ElevatorEvent(this, "18:38:21", Direction.UP, 1, 2);
		ElevatorEvent e3 = new ElevatorEvent(this, "20:01:34", Direction.UP, 1, 3);
		s.newRequest(e1);
		assertEquals(e1, s.getUpEvents().get(0));
		s.newRequest(e2);
		assertEquals(e2, s.getUpEvents().get(1));
		s.newRequest(e3);
		assertEquals(e3, s.getUpEvents().get(2));
	}
	
	@Test
	void testAddDownEvents() {
		FloorSubsystem f = new FloorSubsystem(5);
		Scheduler s = new Scheduler(f);
		ElevatorEvent e1 = new ElevatorEvent(this, "14:05:15", Direction.DOWN, 4, 2);
		ElevatorEvent e2 = new ElevatorEvent(this, "18:38:21", Direction.DOWN, 3, 0);
		ElevatorEvent e3 = new ElevatorEvent(this, "20:01:34", Direction.DOWN, 2, 1);
		s.newRequest(e1);
		assertEquals(e1, s.getDownEvents().get(0));
		s.newRequest(e2);
		assertEquals(e2, s.getDownEvents().get(1));
		s.newRequest(e3);
		assertEquals(e3, s.getDownEvents().get(2));
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
		assertEquals(0, s.getUpEvents().size());
		assertEquals(0, s.getDownEvents().size());
	}
}
