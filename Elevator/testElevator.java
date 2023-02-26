package Elevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import FloorSystem.Direction;
import FloorSystem.ElevatorEvent;
import FloorSystem.FloorSubsystem;
import Scheduler.Scheduler;


class testElevator{

	@Test
	void testUP() {
		Elevator elevator = new Elevator(10,0, null);
		elevator.setFloorToGo(5);
		try {
			elevator.pressButton();
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals(5,elevator.getCurFloor());
	}
	
	@Test
	void testDown()  {
		Elevator elevator = new Elevator(10,0, null);
		elevator.setFloorToGo(10);
		try {
			elevator.pressButton();
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
		elevator.setFloorToGo(2);
		try {
			elevator.pressButton();
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals(2,elevator.getCurFloor());
	}
	
	@Test
	void testSetCurFloor(){
		Elevator elevator = new Elevator(10,0, null);
		elevator.setCurFloor(4);
		assertEquals(4,elevator.getCurFloor());
	}
	
	@Test
	void testGetGroundFloor() {
		Elevator elevator = new Elevator(10,6, null);
		assertEquals(6,elevator.getGroundFloor());
	}
	
	@Test
	void testGetMaxFloor() {
		Elevator elevator = new Elevator(10,0, null);
		assertEquals(10,elevator.getMaxFloor());
	}
	
	@Test
	void testSetFloorToGo() {
		Elevator elevator = new Elevator(10,0, null);
		elevator.setFloorToGo(7);
		assertEquals(7,elevator.getFloorToGo());
	}
	@Test
	void testElevatorEvent() {
		FloorSubsystem f = new FloorSubsystem(11);
		ElevatorEvent event = new ElevatorEvent(this, "14:05:15", Direction.UP, 7, 5);
		Scheduler s = new Scheduler(f);
		s.newRequest(event);
		Elevator elevator = new Elevator(10, 0, s);

		Thread sT = new Thread(s);
		Thread eT = new Thread(elevator);
		
		
		sT.start();
		
		IdleState i = new IdleState(elevator);
		
		eT.start();
			
		assertNotEquals(i, elevator.getState());
	}
	

}
