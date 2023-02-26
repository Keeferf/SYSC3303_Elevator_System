package Elevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

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
		Scheduler s = new Scheduler(f);
		Elevator elevator = new Elevator(10, 0, s);
		IdleState idle = new IdleState(elevator);
		Thread sT = new Thread(s);
		Thread eT = new Thread(elevator);
		Thread fT = new Thread(f);
		//System.out.println(elevator.getState());
		//assertEquals(IdleState,elevator.getState());
		elevator.setFloorToGo(7);
		
		try {
			sT.start();
			eT.start();
			fT.start();
			elevator.pressButton();
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(7,elevator.getFloorToGo());
	
	}
	

}
