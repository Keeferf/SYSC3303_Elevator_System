package Elevator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import Elevator.ElevatorMotor.Status;
import junit.framework.TestCase;

/**
 * Testing each of the elevator components
 * @author Nicholas Rose - 101181935
 *
 */
class testElevatorComponents extends TestCase{
	
	private final int MAXFLOOR = 5;
	private final int GROUNDFLOOR = 0;
	
	private ArrayList<ElevatorLamp> lamps;
	private ArrayList<ElevatorButton> buttons;
	
	private ElevatorMotor motor;
	private ElevatorDoor door;

	@Override
	protected void setUp() throws Exception {
		lamps = new ArrayList<>();
		buttons = new ArrayList<>();
		
		for(int i = GROUNDFLOOR; i <= MAXFLOOR; i++) {
			lamps.add(new ElevatorLamp(i));
			buttons.add(new ElevatorButton(i));
		}
		
		motor = new ElevatorMotor();
		door = new ElevatorDoor();
	}
	
	@Test
	public void testElevatorDoor() {
		try {
			setUp();
		} catch (Exception e) {
			fail();
			return;
		}
		//Test beginning state
		assertFalse("Testing Initial Door State", door.getDoorState());
		//Test open and close
		door.open();
		assertTrue("Testing Open Door State", door.getDoorState());
		door.close();
		assertFalse("Testing Close Door State", door.getDoorState());
	}
	
	
	@Test
	public void testButtonPress() {
		try {
			setUp();
		} catch (Exception e) {
			fail();
			return;
		}
		
		int floor = 5;
		for(ElevatorButton b: buttons) {
			if(b.getFloorNum() == floor) {
				assertFalse("Button is not pressed yet.", b.getState());
				b.toggle(true);
				assertTrue("Button has been pressed.", b.getState());
				
				assertEquals("Checking floor num",5,b.getFloorNum());
				
				return;
			}
		}
		fail();
	}
	
	@Test
	public void testLamps() {
		try {
			setUp();
		} catch (Exception e) {
			fail();
			return;
		}
		
		int floor = 5;
		for(ElevatorLamp l: lamps) {
			if(l.getFloorNum() == floor) {
				assertFalse("Lamp is not on yet.", l.getState());
				l.toggle(true);
				assertTrue("Lamp is on.", l.getState());
				
				assertEquals("Checking floor num",5,l.getFloorNum());
				
				return;
			}
		}
		fail();
	}
	
	@Test
	public void testMotor() {
		motor = new ElevatorMotor();
		
		assertFalse(motor.isActive());
		assertEquals(motor.getStatus(), Status.STOPPED);
		
		motor.activateUp();
		assertTrue(motor.isActive());
		assertEquals(motor.getStatus(), Status.UP);
		
		motor.activateDown();
		assertTrue(motor.isActive());
		assertEquals(motor.getStatus(), Status.DOWN);
		
		motor.disableMotor();
		assertFalse(motor.isActive());
		assertEquals(motor.getStatus(), Status.STOPPED);
	}

}
