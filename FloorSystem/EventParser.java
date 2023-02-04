package FloorSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/**
 * A class for reading in from the events file for processing
 * @author Nicholas Rose - 101181935
 */
public class EventParser {
    private BufferedReader reader;

    private final String filename = "./FloorSystem/Events.txt";


    public EventParser() throws FileNotFoundException {
        FileReader fileReader = new FileReader(new File(filename));
        reader = new BufferedReader(fileReader);
    }

    /**
     * Reads the file and formulates a list of event
     * @return events ArrayList<ElevatorEvent>
     */
    public ArrayList<ElevatorEvent> getEvents() {
        ArrayList<ElevatorEvent> events = new ArrayList<>();

        String line;
        try {
            while((line = reader.readLine()) != null) {
                events.add(processLine(line.split(";")));
            }

            reader.close();
        } catch (Throwable e) {
            //Exit loop if interruped
            throw new RuntimeException(e);
        }

        return events;
    }

    /**
     * processLine
     * Takes an array of strings from the event file and converts it to an event object
     * @param line
     * @return ElevatorEvent
     * @throws IllegalArgumentException
     */
    private ElevatorEvent processLine(String[] line) throws IllegalArgumentException{
        //Processing direction from file
        Direction direction;
        if(line[2].equals("UP")) direction = Direction.UP;
        else if(line[2].equals("DOWN")) direction = Direction.DOWN;
        else throw new IllegalArgumentException(line[2] + ": Invalid direction value");
        
        //Process numbers
        int floorToGo = parseInt(line[3]);
        int currFloor = parseInt(line[1]);

        //Creates new event object
        return new ElevatorEvent(this, line[0],direction,floorToGo,currFloor);
    }
}
