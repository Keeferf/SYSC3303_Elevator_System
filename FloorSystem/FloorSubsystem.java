package FloorSystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import Elevator.Elevator;
import Scheduler.Scheduler;
import Util.Comms.Config;
import Util.Comms.RequestStatus;
import Util.Comms.UDPBuilder;
import Util.Timer.Timeable;
import Util.Timer.TimerN;

public class FloorSubsystem implements Runnable, Timeable{
	
	private ArrayList<ElevatorEvent> ee;
	private Scheduler sc;
	private ArrayList<Floor> floors;
	private int numReqsProcessed;
	private DatagramSocket socket;
	
	public FloorSubsystem(int n) {
		try {
			this.socket = new DatagramSocket(Config.getSchedulerport());
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
		this.floors = new ArrayList<Floor>();
		for(int i = 0; i < n; i++) {
			this.floors.add(new Floor(i));
		}
		this.numReqsProcessed = 0;
	}
	
	/**
	 * Run method for the Floor Subsystem Thread
	 * 
	 * Loads all events into Timers which will dispatch at x time,
	 * then loops for responses from scheduler
	 */
	@Override
	public void run() {
		try {
			EventParser ep = new EventParser();
			ee = ep.getEvents();
		} catch (Throwable e) {
			//Exit system
			e.printStackTrace();
			System.exit(1);
		}
			
		for(ElevatorEvent e: ee) {
			//Dispatch a new event that will send at x seconds, calling Timeable method below
			TimerN.startTimer(e.getTimeAsSeconds(),  e, this);
		}
			
		//Loop to receive acknowledgements/fulfilled from system
		while(true) {
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
			if(!(UDPBuilder.getPayload(p) instanceof ElevatorEvent)) {
				System.out.println("Bad Data");
				continue;
			} else {
				e = (ElevatorEvent) UDPBuilder.getPayload(p);
			}
			
			//Logic for acknowledge/fullfilled requests received
			
			switch(e.getRequestStatus()) {
			case ACKNOWLEDGED :
				System.out.println("Scheduler Acknowledged Event: " + e.toString());
			case FULFILLED :
				System.out.println("Elevator Fulfilled Event: " + e.toString());
			default:
				System.out.println("Invalid Request Data Received: " + e.getRequestStatus().toString());
				continue;
			}
		}
	
	}
	
	/**
	 * Setter method for the Scheduler of the Floor Subsystem.
	 * @param sc
	 */
	public void setScheduler(Scheduler sc) {
		this.sc = sc;
	}
	
	/**
	 * Prints out the request which was completed, i.e. the passenger arrived at their destination floor
	 * @param completedRequest Passenger request which was completed
	 */
	public void alert(ElevatorEvent completedRequest) {
		System.out.println("Recieved Reponse for Request: " + completedRequest.toString());
		this.numReqsProcessed++;
	}

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
		
		
		//When the timer finishes, send via UDP to the scheduler
		
		try {
			socket.send(UDPBuilder.newMessage(e, Config.getSchedulerip(), Config.getSchedulerport()));
			
			System.out.println("Message sent to scheduler: " + e.toString());
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
	}
}
