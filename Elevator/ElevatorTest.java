package Elevator;

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;


class ElevatorTest{


	@Test
	void testUP() throws InterruptedException {
		Elevator elevator = new Elevator(10,0);
		elevator.setFloorToGo(5);
		elevator.pressButton();
		assertEquals(5,elevator.getCurFloor());
		
	}
	
	@Test
	void testDown() throws InterruptedException {
		Elevator elevator = new Elevator(10,0);
		elevator.setFloorToGo(10);
		elevator.pressButton();
		elevator.setFloorToGo(2);
		elevator.pressButton();
		assertEquals(2,elevator.getCurFloor());
		
	}
	
	@Test
	void testGetCurFloor() throws InterruptedException{
		Elevator elevator = new Elevator(10,0);
		elevator.setFloorToGo(3);
		elevator.pressButton();
		assertEquals(3,elevator.getCurFloor());
		
	}
	
	@Test
	void testSetCurFloor() throws InterruptedException{
		Elevator elevator = new Elevator(10,0);
		elevator.setCurFloor(4);
		assertEquals(4,elevator.getCurFloor());
		
	}
	
	@Test
	void testGetGroundFloor() throws InterruptedException{
		Elevator elevator = new Elevator(10,6);
		assertEquals(6,elevator.getGroundFloor());
		
	}
	
	@Test
	void testGetMaxFloor() throws InterruptedException{
		Elevator elevator = new Elevator(10,0);
		assertEquals(10,elevator.getMaxFloor());
		
	}
	@Test
	void testSetandGetFloorToGo() throws InterruptedException{
		Elevator elevator = new Elevator(10,0);
		elevator.setFloorToGo(7);
		assertEquals(7,elevator.getFloorToGo());
		
	}
	

}
