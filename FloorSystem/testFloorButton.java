package FloorSystem;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class testFloorButton {

    private String time, floorNum;
    private Direction direction;
    FloorButton FB = new FloorButton(time, floorNum, direction, floorNum);

    @Test
    public void testGetTime(){
        FB.setTime("11:30");
        assertEquals("11:30", FB.getTime());
    }

    @Test
    public void testCurrFloorNum(){
        FB.setCurrFloorNum("5");
        assertEquals("5", FB.getCurrFloorNum());
    }

    @Test
    public void testDestinationFloor(){
        FB.setDestFloorNum("2");
        assertEquals("2", FB.getDestFloorNum());
    }

    @Test
    public void testDirection(){
        FB.setDirection(direction.DOWN);
        assertEquals("DOWN", FB.getDirection().toString());
    }
}