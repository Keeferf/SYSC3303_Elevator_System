package Scheduler.FaultHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import FloorSystem.ElevatorEvent;
import Scheduler.Scheduler;
import Util.Comms.Config;
import Util.Comms.UDPBuilder;
import Util.Timer.Timeable;
import Util.Timer.TimerN;

/**
 * A class for handling faults from the elevators
 * @author Nicholas Rose
 *
 */
public class FaultHandler implements Runnable, Timeable{
	
	private Scheduler scheduler;
	
	private DatagramSocket socket;
	
	private ArrayList<ArrayList<FaultState>> faultList;
	private ArrayList<ArrayList<TimerN>> timers;
	
	public FaultHandler(Scheduler scheduler) {
		this.scheduler = scheduler;
		
		faultList = new ArrayList<>();
		timers = new ArrayList<>();
		
		try {
			socket = new DatagramSocket(Config.getFaultHandlerPort());
		} catch (SocketException e) {
			System.out.println("FAILED TO INITIALISE FAULT HANDLER");
			System.exit(1);
		}
		
		Thread thread = new Thread(this);
		
		thread.start();
	}

	@Override
	public void run() {
		//Continual loop of receive timing event and process
		while(true) {
			byte[] pack = new byte[Config.getMaxMessageSize()];
			DatagramPacket packet = new DatagramPacket(pack,pack.length);
			
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			processPacket(packet);
		}
		
	}
	
	/**
	 * Processes a particular packet for the fault handling system
	 * @param packet
	 */
	private void processPacket(DatagramPacket packet) {
		//Unpack the timing event
		ElevatorEvent e = UDPBuilder.getPayload(packet);
		if(!(e instanceof ElevatorEvent)) {
			return;
		}
		
		System.out.println("Recieved Timing Event: " + e.toString());
		
		ElevatorTimingEvent event = (ElevatorTimingEvent) UDPBuilder.getPayload(packet);
		
		ArrayList<FaultState> faults = faultList.get(event.getElevatorNum());
		
		FaultState faultState = faults.get(event.getElevatorTimingState().ordinal());
		
		if(faultState.equals(FaultState.UNFULFILLED)) {
			faults.set(event.getElevatorNum(), faultState.COMPLETED);
		} else if(faultState.equals(FaultState.ERROR)){
			System.out.println("SYSTEM REPORTED ERROR INCORRECTLY");
		} else {
			//Already completed
		}
		timers.get(event.getElevatorNum()).get(event.getElevatorTimingState().ordinal()).killTimer();	//Kill the timer associated with this packet
	}

	/**
	 * Method procced when timers finish. Checks if error or fulfilled event
	 */
	@Override
	public void timerFinished(Object payload) {
		if(!(payload instanceof ElevatorTimingEvent)) {
			System.out.println("Invalid payload type");
			return;
		}
		
		
		ElevatorTimingEvent e = (ElevatorTimingEvent) payload;
		
		System.out.println("Fault Timer Caught: " + e.toString() + " Fault State: " + e.getElevatorTimingState().toString());
		
		ArrayList<FaultState> faults = faultList.get(e.getElevatorNum());
		
		FaultState faultState = faults.get(e.getElevatorTimingState().ordinal());
		
		if(faultState.equals(FaultState.UNFULFILLED)) {
			faults.set(e.getElevatorNum(), faultState.ERROR);
		} else if(faultState.equals(FaultState.ERROR)){
			System.out.println("SYSTEM ALREADY REPORTED ERROR");
		} else {
			//Already completed, so should never get here, but can be ignored
		}
		
		
	}
	
	/**
	 * Called from scheduler. Command has been sent for elevator to do
	 * Generates list of unfulfilled timer events
	 * @param e
	 * @param elevatorId
	 */
	public void notify(ElevatorEvent e) {
		//Creates a new list with 4 unfulfilled events
		ArrayList<FaultState> faults = new ArrayList<>();
		
		ArrayList<TimerN> t = new ArrayList<>();
		
		ElevatorTimingState states[] = ElevatorTimingState.values();
		
		for(ElevatorTimingState s: states) {
			//add unfulfilled to the list of faults
			faults.add(FaultState.UNFULFILLED);
			
			//Dispatch a timer thread to keep track of the state
			//Conversion to int is negligble because of fudge factor
			
			ElevatorTimingEvent ete = new ElevatorTimingEvent(e,s);
			
			TimerN timer = TimerN.startTimer((int)s.time(), ete,this);
			
			//Add to list of active timers
			t.add(timer);
		}
		
		faultList.add(e.getElevatorNum(), new ArrayList<FaultState>());
		
		timers.add(e.getElevatorNum(),t);
		
		
	}
	
	/**
	 * Kills all the timers in the passed list.
	 * 
	 * Means that the last event has been hit
	 * Don't want the last timer to trigger in the next event sent
	 * 
	 * May or may not implement
	 * System should kill timers 1 by 1 as packets come in
	 * Or trigger 1 by 1 as errors are detected
	 */
	private void killAllTimers(ArrayList<TimerN> timers) {
		for(TimerN t: timers) {
			t.killTimer();
		}
	}
}
