package FloorSystem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * A test class for the Floor class
 * @author Keefer Belanger 101152085
 */
public class FloorTest {

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
        Floor floor = new Floor(5);
        floor.setPeople(3);
        assertEquals(3, floor.getPeople());
    }

    @Test
    void testGetUpLamp() {
        Floor floor = new Floor(5);
        assertFalse(floor.getUpLamp());
    }

    @Test
    void testSetUpLamp() {
        Floor floor = new Floor(5);
        floor.setUpLamp(true);
        assertTrue(floor.getUpLamp());
    }

    @Test
    void testGetDownLamp() {
        Floor floor = new Floor(5);
        assertFalse(floor.getDownLamp());
    }

    @Test
    void testSetDownLamp() {
        Floor floor = new Floor(5);
        floor.setDownLamp(true);
        assertTrue(floor.getDownLamp());
    }
}

