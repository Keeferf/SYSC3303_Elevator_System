package Elevator;

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;


class ElevatorTest{

	@Test
	void testUP() {
		Elevator elevator = new Elevator(10,0);
		elevator.setFloorToGo(5);
		try {
			elevator.pressButton();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(5,elevator.getCurFloor());
	}
	
	@Test
	void testDown()  {
		Elevator elevator = new Elevator(10,0);
		elevator.setFloorToGo(10);
		try {
			elevator.pressButton();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		elevator.setFloorToGo(2);
		try {
			elevator.pressButton();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(2,elevator.getCurFloor());
	}
	
	@Test
	void testSetCurFloor(){
		Elevator elevator = new Elevator(10,0);
		elevator.setCurFloor(4);
		assertEquals(4,elevator.getCurFloor());
	}
	
	@Test
	void testGetGroundFloor() {
		Elevator elevator = new Elevator(10,6);
		assertEquals(6,elevator.getGroundFloor());
	}
	
	@Test
	void testGetMaxFloor() {
		Elevator elevator = new Elevator(10,0);
		assertEquals(10,elevator.getMaxFloor());
	}
	
	@Test
	void testSetFloorToGo() {
		Elevator elevator = new Elevator(10,0);
		elevator.setFloorToGo(7);
		assertEquals(7,elevator.getFloorToGo());
	}
	

}
