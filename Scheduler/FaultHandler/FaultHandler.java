package Scheduler.FaultHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.JFrame;

import Elevator.Elevator;
import Elevator.ElevatorStateEvent;
import FloorSystem.ElevatorEvent;
import FloorSystem.GUI;
import Scheduler.Scheduler;
import Scheduler.FaultHandler.GUI.FaultHandlerFrame;
import Util.Comms.Config;
import Util.Comms.RequestStatus;
import Util.Comms.UDPBuilder;
import Util.Timer.Timeable;
import Util.Timer.TimerN;

/**
 * A class for handling faults from the elevators
 * @author Nicholas Rose
 *
 */
public class FaultHandler implements Runnable, Timeable{
	
	private GUI gui;

	private DatagramSocket socket;
	
	private ArrayList<ArrayList<FaultState>> faultList;
	private ArrayList<ArrayList<TimerN>> timers;
	private ArrayList<Boolean> hasStarted;
	private ArrayList<ElevatorStateEvent> states;	//Current state of each elevator
	
	private FaultHandlerFrame faultHandlerFrame;
	public boolean isRunning = true;
	
	public FaultHandler() {
		
		this.gui = new GUI();

		faultList = new ArrayList<>();
		timers = new ArrayList<>();
		hasStarted = new ArrayList<>();
		states = new ArrayList<>();
		
		//Initialise inner lists
		for(int i = 0; i < Config.getElevatorPorts().length; i++) {
			faultList.add(new ArrayList<FaultState>());
			timers.add(new ArrayList<TimerN>());
			hasStarted.add(i, false);
			states.add(i,null);
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
		while(isRunning) {
			byte[] pack = new byte[Config.getMaxMessageSize()];
			DatagramPacket packet = new DatagramPacket(pack,pack.length);
			
			try {
				socket.receive(packet);
				processPacket(packet);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			
		}
		
	}
	
	/**
	 * Processes a particular packet for the fault handling system
	 * @param packet
	 */
	private void processPacket(DatagramPacket packet) {
		//Unpack the timing event
		Object o = UDPBuilder.getGenericPayload(packet);
		
		//If timing or state event, process it
		if(o instanceof ElevatorTimingEvent) {
			processTimingEvent((ElevatorTimingEvent) o);
		} else if(o instanceof ElevatorStateEvent) {
			processStateEvent((ElevatorStateEvent) o);
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
		
		//Starts are ignored
		if(e.getElevatorTimingState().equals(ElevatorTimingState.START)) {
			return;
		}
		
		System.out.println("Fault Timer Caught: " + e.toString() + " Fault State: " + e.getElevatorTimingState().toString() + " Elevator Num: " + e.getElevatorId());
		
		ArrayList<FaultState> faults = faultList.get(e.getElevatorId());
		
		FaultState faultState = faults.get(e.getElevatorTimingState().ordinal()-1);
		
		//System.out.println("Ordinal #: " + e.getElevatorTimingState().ordinal());
		//System.out.println("Current Fault State: " + faultState.toString());
		
		if(faultState.equals(FaultState.UNFULFILLED)) {
			if (!faults.contains(faultState.ERROR)) {
				faults.set(e.getElevatorTimingState().ordinal()-1, faultState.ERROR);
				try {
					this.socket.send(UDPBuilder.newMessage(new ElevatorEvent(this, RequestStatus.ERROR), Config.getElevatorip(), Config.getElevatorport(e.getElevatorId())));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}			
		} else if(faultState.equals(FaultState.ERROR)){
			System.out.println("SYSTEM ALREADY REPORTED ERROR");	//Should not proc
		} else {
			System.out.println("TIMER TRIGGERED EVENT THOUGH EVENT ALREADY COMPLETED");
			return;
		}
		faultList.set(e.getElevatorId(), faults);
		
		if(faultHandlerFrame != null) {
			faultHandlerFrame.update(faultList);
		}
		//Clear the list back to unfulfilled if its the last one
		if(e.getElevatorTimingState().equals(ElevatorTimingState.DOOR_OPEN)) {
			killAllTimers(e.getElevatorId());
			//clearList(e.getElevatorId());
			hasStarted.set(e.getElevatorId(), false);
		}
		
		if(faultHandlerFrame != null) {
			faultHandlerFrame.update(faultList);
		}
		
	}
	
	/**
	 * Processes a timing event sent from an elevator
	 * Used to ensure deadlines are met
	 * @param e ElevatorEvent
	 */
	public void processTimingEvent(ElevatorTimingEvent event) {
		
		
		//Start event
		if(event.getElevatorTimingState().equals(ElevatorTimingState.START)) {
			//System.out.println(hasStarted.get(event.getElevatorId()));
			hasStarted.set(event.getElevatorId(), true);
			//System.out.println(hasStarted.get(event.getElevatorId()));
			notify(event);
			return;
		}
		
		//Guard statement for premature timing events
		if(!hasStarted.get(event.getElevatorId())) {
			//System.out.println(hasStarted.get(event.getElevatorId()));
			//System.out.println("Prematurely Got sent");
			return;
		}
		
		System.out.println("Received Timing Packet: " + event.toString() + " Fault State: " + event.getElevatorTimingState().toString() + " Elevator Num: " + event.getElevatorId());

		
		ArrayList<FaultState> faults = faultList.get(event.getElevatorId());
		
		FaultState faultState = faults.get(event.getElevatorTimingState().ordinal()-1);
		
		//System.out.println("Ordinal #: " + event.getElevatorTimingState().ordinal());
		//System.out.println("Current Fault State: " + faultState.toString());
		
		if(faultState.equals(FaultState.UNFULFILLED)) {
			faults.set(event.getElevatorTimingState().ordinal()-1, faultState.COMPLETED);
		} else if(faultState.equals(FaultState.ERROR)){
			System.out.println("***********************************");
			System.out.println("SYSTEM REPORTED ERROR INCORRECTLY -> Received Response Packet after Timeout");
			System.out.println(event.toString());
			System.out.println("***********************************");
		} else {
			//Already completed
		}
		
		//End of cycle actions
		if(event.getElevatorTimingState().equals(ElevatorTimingState.DOOR_OPEN)) {
			killAllTimers(event.getElevatorId());
			clearList(event.getElevatorId());
			hasStarted.set(event.getElevatorId(), false);
		}
		
		timers.get(event.getElevatorId()).get(event.getElevatorTimingState().ordinal()-1).killTimer();	//Kill the timer associated with this packet
		//System.out.println("IsKilled: " + timers.get(event.getElevatorId()).get(event.getElevatorTimingState().ordinal()-1).isKilled());
		
		faultList.set(event.getElevatorId(), faults);
		
		if(faultHandlerFrame != null) {
			faultHandlerFrame.update(faultList);
		}
	}
	
	/**
	 * Processes an elevator state change event sent from an elevator
	 * Used to monitor the current state of an elevator
	 * @param e ElevatorStateEvent
	 */
	public void processStateEvent(ElevatorStateEvent e) {
		states.set(e.getElevatorNum(), e);	//Sets the list of the current states of all the elevators to the most up to date event received
		
		System.out.println(e);
		//Notify the GUI interface here using "states" list :D
		gui.setDirectionInfo(e.getElevatorNum());
		gui.setRequestInfo(e.getElevatorNum());
		gui.setFaultInfo(e.getElevatorNum());
	}
	
	/**
	 * Called from scheduler. Command has been sent for elevator to do
	 * Generates list of unfulfilled timer events
	 * 
	 * Elevator Event must have elevator num filled in
	 * @param e
	 * @param elevatorId
	 */
	public void notify(ElevatorTimingEvent e) {
		int elevatorNum = 0;
		if(e.getElevatorNum() == -1 && e.getElevatorId() == -1) {
			throw new RuntimeException("ElevatorNum has not been set.");
		} else if(e.getElevatorNum() == -1) {
			elevatorNum = e.getElevatorId();
		} else {
			elevatorNum = e.getElevatorNum();
		}
		e.setElevatorNum(e.getElevatorId());
		
		//Creates a new list with 4 unfulfilled events
		ArrayList<FaultState> faults = new ArrayList<>();
		
		ArrayList<TimerN> t = new ArrayList<>();
		
		ElevatorTimingState states[] = ElevatorTimingState.values();
		
		for(ElevatorTimingState s: states) {
			//Ignore starts
			if(s.equals(ElevatorTimingState.START)) continue;
			
			//add unfulfilled to the list of faults
			faults.add(FaultState.UNFULFILLED);
			
			//Dispatch a timer thread to keep track of the state
			//Conversion to int is negligble because of fudge factor
		
			ElevatorTimingEvent ete = new ElevatorTimingEvent(e,s);
			
			TimerN timer = new TimerN(computeStateTime(s,ete),ete,this);//TimerN.startTimer(computeStateTime(s,ete), ete,this);;
			
			//Add to list of active timers
			t.add(timer);
		}
		//System.out.println("ElevatorNum:" + e.getElevatorNum());
		faultList.set(elevatorNum, faults);
		
		timers.set(elevatorNum,t);
		
		if(faultHandlerFrame != null) {
			faultHandlerFrame.update(faultList);
		}
	}
	
	/**
	 * Sets the statuses of all enums in the state list
	 * back to Unfulfilled
	 */
	private void clearList(int elevatorNum) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<FaultState> faults = new ArrayList<>();
		
		ElevatorTimingState states[] = ElevatorTimingState.values();
		
		for(int i = 1;i < ElevatorTimingState.values().length; i++) {
			//add unfulfilled to the list of faults
			faults.add(FaultState.UNFULFILLED);
			
			//Dispatch a timer thread to keep track of the state
			//Conversion to int is negligble because of fudge factor
		}
		faultList.set(elevatorNum, faults);
		
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
	private void killAllTimers(int elevatorNum) {
		ArrayList<TimerN> ttimers = timers.get(elevatorNum);
		
		for(TimerN t: ttimers) {
			t.killTimer();
			//System.out.println("IsKilled: " + t.isKilled());
		}
		
		timers.set(elevatorNum,ttimers);
	}
	
	/**
	 * Computes the estimated time it will take for the elevator to get to
	 * its destination
	 * @param s
	 */
	public double computeStateTime(ElevatorTimingState s, ElevatorTimingEvent e) {
		//Note: Start is always at the curr floor in event. Does not include movement to start
		if(s.equals(ElevatorTimingState.START)) {return 0;}
		final double DOOR_CLOSE_TIME = 5;
		final double ACCEL_TIME_PER_FLOOR = 1.318;
		final double DECEL_TIME_PER_FLOOR = 4;
		final double DOOR_OPEN_TIME = 5;
		
		double time = 0;
		
		time += DOOR_CLOSE_TIME;
		if(s.equals(ElevatorTimingState.DOOR_CLOSED)) return time;
		
		time += ACCEL_TIME_PER_FLOOR * Math.abs(e.getCurrFloor() - e.getFloorToGo());
		if(s.equals(ElevatorTimingState.ACCELERATING)) return time;
		
		time += DECEL_TIME_PER_FLOOR;//Only decelerates 1 floor
		if(s.equals(ElevatorTimingState.DECELERATING)) return time;
		
		time += DOOR_OPEN_TIME;
		return time;	//Only other state is door_open
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
	
	public void toggleHasStarted(int elevatorNum, boolean b) {
		hasStarted.set(elevatorNum, b);
	}

	public void shutdown() {
		isRunning = false;
		try {
			socket.close();
		} catch(Throwable e) {
			
		}
	}
	
}
