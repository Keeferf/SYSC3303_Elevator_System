package Elevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import FloorSystem.Direction;
import FloorSystem.ElevatorEvent;
import Scheduler.Scheduler;

/**
 * Test class for the elevator class
 */
class testElevator{

	@Test
	void testUP() {
		Elevator elevator = new Elevator();
		elevator.setFloorToGo(5);
		try {
			elevator.moveElevator();
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals(1,elevator.getCurrFloor());
	}
	
	@Test
	void testDown()  {
		Elevator elevator = new Elevator();
		elevator.setFloorToGo(10);
		try {
			elevator.moveElevator();
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
		elevator.setFloorToGo(2);
		try {
			elevator.moveElevator();
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals(2,elevator.getCurrFloor());
	}
	
	@Test
	void testSetFloorToGo() {
		Elevator elevator = new Elevator();
		elevator.setFloorToGo(7);
		assertEquals(7,elevator.getFloorToGo());
	}
	@Test
	void testElevatorEvent() {
		ElevatorEvent event = new ElevatorEvent(this, "00:00:10", Direction.UP, 7, 5);
		Scheduler s = new Scheduler();
		s.newRequest(event);
		Elevator elevator = new Elevator();

		Thread sT = new Thread(s);
		Thread eT = new Thread(elevator);
		
		
		sT.start();
		
		IdleState i = new IdleState(elevator);
		
		eT.start();
			
		assertNotEquals(i, elevator.getState());
	}
	

}
