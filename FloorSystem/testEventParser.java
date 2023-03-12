package FloorSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

//import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

/**
 * Test class for the Event Parser Class
 */
public class testEventParser {

    private EventParser parser;

    @Test
    public void testSetup() {

        try {
            parser = new EventParser();

            assertTrue(true);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    public void testReadIn() {
        testSetup();

        try {
            parser.getEvents();
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    public void testEventContents() {
        testSetup();

        ArrayList<ElevatorEvent> events = new ArrayList<>();

        try {
            events.addAll(parser.getEvents());
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            fail();
        }

        //If test file changes, change this section as well
        assertEquals("00:00:05.0", events.get(0).getTimestamp(),"Timestamp Test");
        assertEquals(Direction.UP, events.get(0).getDirection(), "Direction Test");
        assertEquals(2, events.get(0).getCurrFloor(),"Current Floor Test");
        assertEquals(4, events.get(0).getFloorToGo(),"Destination Floor Test");
    }
}
