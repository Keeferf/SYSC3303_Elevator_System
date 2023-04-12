package FloorSystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;

import Util.Comms.Config;
import Util.Comms.RequestStatus;
import Util.Comms.UDPBuilder; 
import Util.Timer.Timeable;
import Util.Timer.TimerN;

/**
 * The floor subsystem class handles the implementation of the floors
 */
public class FloorSubsystem implements Runnable, Timeable{
	
	private ArrayList<ElevatorEvent> ee;
	private ArrayList<Floor> floors;
	private DatagramSocket socket;
	private int reqTracker = 0;
	
	/**
	 * Constructor for FloorSubsystem instances
	 */
	public FloorSubsystem() {
		//Max and min floors
		int n = Config.getMaxFloor();
		int i = Config.getMinFloor();
		
		//Generates floors min-max
		this.floors = new ArrayList<Floor>();
		for(i = 0; i < n; i++) {
			this.floors.add(new Floor(i));
		}
	}
	
	/**
	 * Run method for the Floor Subsystem Thread
	 * 
	 * Loads all events into Timers which will dispatch at x time,
	 * then loops for responses from scheduler
	 */
	@Override
	public void run() {
		//Creation of socket
		Config.printLine();
		System.out.println("Starting up...");
		//new GUI();
		try {
			this.socket = new DatagramSocket(Config.getFloorsubsystemport());
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Socket Created.");
		
		//loading of events from parser
		try {
			EventParser ep = new EventParser();
			ee = ep.getEvents();
		} catch (Throwable e) {
			//Exit system
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Events loaded from \"Events.txt\"");
		
		Date startTime = new Date();
		
		int numValidReqs = 0;
		
		for(ElevatorEvent e: ee) {
			//Dispatch a new event that will send at x seconds, calling Timeable method below
			if (!e.getMotorFault()) {
				numValidReqs++;
			}
			TimerN.startTimer(e.getTimeAsSeconds(),  e, this);
		}
		System.out.println("Events loaded into timed execution objects");
		System.out.println("Startup Complete!");
		Config.printLine();
		
		//Loop to receive acknowledgements/fulfilled from system
		while(this.reqTracker < this.ee.size()) {
			byte[] d = new byte[Config.getMaxMessageSize()];
			DatagramPacket p = new DatagramPacket(d,d.length);
			try {
				socket.receive(p);
			} catch (IOException e1) {
				e1.printStackTrace();
				continue;
			}
			
			//Extract payload from the packet
			ElevatorEvent e;
			if(!(UDPBuilder.getEventPayload(p) instanceof ElevatorEvent)) {
				System.out.println("Bad Data");
				Config.printLine();
				continue;
			} else {
				e = (ElevatorEvent) UDPBuilder.getEventPayload(p);
			}
			
			//Logic for acknowledge/fulfilled requests received
			
			if(e.getRequestStatus().equals(RequestStatus.ACKNOWLEDGED)) {
				System.out.println("Scheduler Acknowledged Event: " + e.toString());
				Config.printLine();
			} else if(e.getRequestStatus().equals(RequestStatus.FULFILLED)) {
				System.out.println("Elevator Fulfilled Event: " + e.toString());
				Config.printLine();
				this.reqTracker++;
				Config.printLine();
				System.out.println("reqtracker = " + reqTracker + "\nee.size = " + ee.size() + "\nnumValidReqs = " + numValidReqs);
				if (e.getCurrFloor() == ee.get(ee.size() - 1).getCurrFloor() && e.getFloorToGo() == ee.get(ee.size() - 1).getFloorToGo() && e.getTimeAsSeconds() == ee.get(ee.size() - 1).getTimeAsSeconds()) {
					System.out.println("Floor Subsystem Exiting");
					break;
				}
				Config.printLine();
			} else {
				System.out.println("Invalid Request Data Received: " + e.getRequestStatus().toString() + " " + e.toString());
				Config.printLine();
				continue;
			}
		}
		Date exitTime = new Date();
		Config.printLine();
		System.out.println("Started Sending Events At " + startTime.getTime() + "ms\nExiting At " + exitTime.getTime() + "ms\nExpended Time Is " + (exitTime.getTime() - startTime.getTime()) + "ms");
		Config.printLine();
		//System.exit(0);
	}
	
	/**
	 * Prints out the request which was completed, i.e. the passenger arrived at their destination floor
	 * @param completedRequest Passenger request which was completed
	 * 
	 * @deprecated
	 */
	public synchronized void alert(ElevatorEvent completedRequest) {
		System.out.println("Floor Subsystem: Received Response for Request: " + completedRequest.toString() + "\n");
	}

	/**
	 * From Timeable Interface. Called when a timer has finished its sleep time
	 */
	@Override
	public void timerFinished(Object payload) {
		//Check for if is elevatorEvent
		
		ElevatorEvent e;
		if(!(payload instanceof ElevatorEvent)) {
			System.out.println("Error: ElevatorEvent expected");
			return;
		} else {
			e = (ElevatorEvent) payload;
		}
		System.out.println("Timer Finished: " + e.toString());
		
		//When the timer finishes, send via UDP to the scheduler
		
		try {
			socket.send(UDPBuilder.newMessage(e, Config.getSchedulerip(), Config.getSchedulerport()));
			
			System.out.println("Message sent to scheduler: " + e.toString());
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Config.printLine();
		
	}
}
