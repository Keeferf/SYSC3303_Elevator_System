package testPackage;

import org.junit.jupiter.api.Test;

import FloorSystem.Floor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A test class for the Floor class
 * @author Keefer Belanger 101152085
 */
public class testFloor {

    @Test
    void testGetFloorNum() {
        Floor floor = new Floor(5);
        assertEquals(5, floor.getFloorNum());
    }

    @Test
    void testSetFloorNum() {
        Floor floor = new Floor(5);
        floor.setFloorNum(7);
        assertEquals(7, floor.getFloorNum());
    }

    @Test
    void testGetPeople() {
        Floor floor = new Floor(5);
        assertEquals(0, floor.getPeople());
    }

    @Test
    void testSetPeople() {
        Floor floor = new Floor(1);
        int people = floor.setPeople();
        assertTrue(people >= 0 && people <= 5, "The number of people should be between 0 and 5.");
    }

    @Test
    void testGetUpLamp() {
        Floor floor = new Floor(5);
        assertFalse(floor.getUpLamp());
    }

    @Test
	public void testGetDownLamp() {
		Floor floor = new Floor(0);
		assertFalse(floor.getDownLamp());
	}

	@Test
	public void testIsOn() {
		Floor floor = new Floor(0);
		assertFalse(floor.isOn());
	}

	@Test
	public void testToggle() {
		Floor floor = new Floor(0);
		assertFalse(floor.isOn());
		floor.toggle();
		assertTrue(floor.isOn());
		floor.toggle();
		assertFalse(floor.isOn());
	}
}

