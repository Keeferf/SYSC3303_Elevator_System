package Scheduler.FaultHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.JFrame;

import FloorSystem.ElevatorEvent;
import Scheduler.Scheduler;
import Scheduler.FaultHandler.GUI.FaultHandlerFrame;
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
	
	
	private DatagramSocket socket;
	
	private ArrayList<ArrayList<FaultState>> faultList;
	private ArrayList<ArrayList<TimerN>> timers;
	
	private FaultHandlerFrame faultHandlerFrame;
	
	public FaultHandler() {
		
		faultList = new ArrayList<>();
		timers = new ArrayList<>();
		
		//Initialise inner lists
		for(int i = 0; i < Config.getElevatorPorts().length; i++) {
			faultList.add(new ArrayList<FaultState>());
			timers.add(new ArrayList<TimerN>());
		}
		
		try {
			socket = new DatagramSocket(Config.getFaultHandlerPort());
		} catch (SocketException e) {
			System.out.println("FAILED TO INITIALISE FAULT HANDLER");
			e.printStackTrace();
			System.exit(1);
		}
		
		Thread thread = new Thread(this);
		
		thread.start();
	}

	@Override
	public void run() {
		//Startup the GUI!!!!
		faultHandlerFrame = new FaultHandlerFrame();
		
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
		
		ElevatorTimingEvent event = (ElevatorTimingEvent) UDPBuilder.getPayload(packet);
		
		//System.out.println("Fault Timer Caught: " + event.toString() + " Fault State: " + event.getElevatorTimingState().toString() + " Elevator Num: " + event.getElevatorId());

		
		ArrayList<FaultState> faults = faultList.get(event.getElevatorId());
		
		FaultState faultState = faults.get(event.getElevatorTimingState().ordinal());
		
		//System.out.println("Ordinal #: " + event.getElevatorTimingState().ordinal());
		//System.out.println("Current Fault State: " + faultState.toString());
		
		if(faultState.equals(FaultState.UNFULFILLED)) {
			faults.set(event.getElevatorTimingState().ordinal(), faultState.COMPLETED);
		} else if(faultState.equals(FaultState.ERROR)){
			System.out.println("SYSTEM REPORTED ERROR INCORRECTLY -> Received Response Packet after Timeout");
		} else {
			//Already completed
		}
		timers.get(event.getElevatorId()).get(event.getElevatorTimingState().ordinal()).killTimer();	//Kill the timer associated with this packet
		
		faultList.set(event.getElevatorId(), faults);
		
		if(faultHandlerFrame != null) {
			faultHandlerFrame.update(faultList);
		}
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
		
		//System.out.println("Fault Timer Caught: " + e.toString() + " Fault State: " + e.getElevatorTimingState().toString() + " Elevator Num: " + e.getElevatorId());
		
		ArrayList<FaultState> faults = faultList.get(e.getElevatorId());
		
		FaultState faultState = faults.get(e.getElevatorTimingState().ordinal());
		
//		System.out.println("Ordinal #: " + e.getElevatorTimingState().ordinal());
//		System.out.println("Current Fault State: " + faultState.toString());
		
		if(faultState.equals(FaultState.UNFULFILLED)) {
			faults.set(e.getElevatorTimingState().ordinal(), faultState.ERROR);
		} else if(faultState.equals(FaultState.ERROR)){
			System.out.println("SYSTEM ALREADY REPORTED ERROR");	//Should not proc
		} else {
			//Already completed, so should never get here, but can be ignored
		}
		faultList.set(e.getElevatorId(), faults);
		
		if(faultHandlerFrame != null) {
			faultHandlerFrame.update(faultList);
		}
	}
	
	/**
	 * Called from scheduler. Command has been sent for elevator to do
	 * Generates list of unfulfilled timer events
	 * 
	 * Elevator Event must have elevator num filled in
	 * @param e
	 * @param elevatorId
	 */
	public void notify(ElevatorEvent e) {
		if(e.getElevatorNum() == -1) {
			throw new RuntimeException("ElevatorNum has not been set.");
		}
		
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
			
			TimerN timer = TimerN.startTimer((int)s.time()+2, ete,this);
			
			//Add to list of active timers
			t.add(timer);
		}
		System.out.println("ElevatorNum:" + e.getElevatorNum());
		faultList.set(e.getElevatorNum(), faults);
		
		timers.set(e.getElevatorNum(),t);
		
		if(faultHandlerFrame != null) {
			faultHandlerFrame.update(faultList);
		}
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
	
	/////////////////////////////////
	//Getters - For testing purposes
	/////////////////////////////////
	public ArrayList<ArrayList<FaultState>> getFaultList() {
		return faultList;
	}
	
	public ArrayList<ArrayList<TimerN>> getTimerList() {
		return timers;
	}
	
	
}
