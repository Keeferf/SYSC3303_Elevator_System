package testPackage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import Elevator.Elevator;
import FloorSystem.Direction;
import FloorSystem.ElevatorEvent;
import Scheduler.FaultHandler.ElevatorTimingEvent;
import Scheduler.FaultHandler.ElevatorTimingState;
import Scheduler.FaultHandler.FaultHandler;
import Scheduler.FaultHandler.FaultState;
import Util.Comms.Config;
import Util.Comms.UDPBuilder;



/**
 * RUN TESTS ONE AT A TIME
 * @author Nicholas Rose - 1011819135
 *
 */
public class testFaultHandler {
	
	private FaultHandler fHandler;
	
	private int elevatorNum;
	
	/**
	 * Takes aprox 30 seconds to execute
	 */
	@Test
	void testNotify() {
		//Should create a fault list with 4 unfulfilled states
		//Same with 4 timers of times
		fHandler = new FaultHandler();
		
		try {
			//Source no longer needed*
			ElevatorEvent e = new ElevatorEvent(null, "00:00:05.0",Direction.UP,4,2);
			
			int elevatorNum = 3;
			
			e.setElevatorNum(elevatorNum);
			
			fHandler.notify(new ElevatorTimingEvent(e,ElevatorTimingState.START));	//pass in the test object
			
			ArrayList<ArrayList<FaultState>> faultList = fHandler.getFaultList();
			
			//Test to ensure list is initialised correctly
			ArrayList<FaultState> fList = faultList.get(elevatorNum);
			
			assertEquals("1st Element: ", FaultState.UNFULFILLED,fList.get(0));
			assertEquals("2nd Element: ", FaultState.UNFULFILLED,fList.get(1));
			assertEquals("3rd Element: ", FaultState.UNFULFILLED,fList.get(2));
			assertEquals("4th Element: ", FaultState.UNFULFILLED,fList.get(3));
		} catch (Throwable e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		fHandler.shutdown();
	}
	
	@Test
	void testTimerFinished() {
			fHandler = new FaultHandler();
			
			//Source no longer needed*
			ElevatorEvent e = new ElevatorEvent(null, "00:00:05.0",Direction.UP,4,2);
			
			int elevatorNum = 3;
			
			e.setElevatorNum(elevatorNum);
			
			fHandler.notify(new ElevatorTimingEvent(e,ElevatorTimingState.START));	//pass in the test object
		
			try {
				Thread.sleep((5 + 4 + 4 + 5) * 1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		
		ArrayList<ArrayList<FaultState>> faultList = fHandler.getFaultList();
		
		//Test to ensure list is initialised correctly
		ArrayList<FaultState> fList = faultList.get(elevatorNum);
		
		assertEquals("1st Element: ", FaultState.ERROR,fList.get(0));
		assertEquals("2nd Element: ", FaultState.ERROR,fList.get(1));
		assertEquals("3rd Element: ", FaultState.ERROR,fList.get(2));
		assertEquals("4th Element: ", FaultState.ERROR,fList.get(3));
		try {
			fHandler.shutdown();
		} catch(Throwable ex) {
			
		}
		
	}
	
	@Test
	void testProcessPacket() {
		try {
			fHandler = new FaultHandler();
			
			//Source no longer needed*
			ElevatorEvent e = new ElevatorEvent(null, "00:00:05.0",Direction.UP,4,2);
			
			int elevatorNum = 3;
			
			e.setElevatorNum(elevatorNum);
			
			fHandler.notify(new ElevatorTimingEvent(e,ElevatorTimingState.START));	//pass in the test object
		
			DatagramSocket socket = new DatagramSocket();
			ElevatorTimingEvent event = new ElevatorTimingEvent(e, ElevatorTimingState.START);
			socket.send(UDPBuilder.newMessage(event, Config.getFaultHandlerIp(), Config.getFaultHandlerPort()));
			
			//Shoot off 4 packets very fast
			
			Thread.sleep(4000);
			event = new ElevatorTimingEvent(e, ElevatorTimingState.DOOR_CLOSED);
			socket.send(UDPBuilder.newMessage(event, Config.getFaultHandlerIp(), Config.getFaultHandlerPort()));
			
			Thread.sleep(2000);
			event = new ElevatorTimingEvent(e, ElevatorTimingState.ACCELERATING);
			socket.send(UDPBuilder.newMessage(event, Config.getFaultHandlerIp(), Config.getFaultHandlerPort()));
			
			Thread.sleep(2000);
			event = new ElevatorTimingEvent(e, ElevatorTimingState.DECELERATING);
			socket.send(UDPBuilder.newMessage(event, Config.getFaultHandlerIp(), Config.getFaultHandlerPort()));
			
			Thread.sleep(4000);
			event = new ElevatorTimingEvent(e, ElevatorTimingState.DOOR_OPEN);
			socket.send(UDPBuilder.newMessage(event, Config.getFaultHandlerIp(), Config.getFaultHandlerPort()));
		
		
			ArrayList<ArrayList<FaultState>> faultList = fHandler.getFaultList();
			
			//Test to ensure list is initialised correctly
			ArrayList<FaultState> fList = faultList.get(elevatorNum);
			//Thread.sleep(30 * 1000);//enough to wait for all timers
			Thread.sleep(10 * 1000);
			
			assertEquals("1st Element: ", FaultState.COMPLETED,fList.get(0));
			assertEquals("2nd Element: ", FaultState.COMPLETED,fList.get(1));
			assertEquals("3rd Element: ", FaultState.COMPLETED,fList.get(2));
			assertEquals("4th Element: ", FaultState.COMPLETED,fList.get(3));
			
			
		
		} catch(Throwable ee) {
			fHandler.shutdown();
			ee.printStackTrace();
			fail();
		}
		
		fHandler.shutdown();
	}
	
	
}
