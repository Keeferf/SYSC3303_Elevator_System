package Util.Comms;

/**
 * A configuration class for all of the values that could change throughout the program.
 * @author Nicholas Rose - 101181935
 */
public class Config {
	
	private static final String FloorSubsystemIP = "10.0.0.225";
	private static final String SchedulerIP = "10.0.0.225";
	private static final String ElevatorIP = "10.0.0.225";
	private static final String FaultHandlerIP = "10.0.0.225";
	
	private static final int FloorSubsystemPort = 5040;
	private static final int SchedulerPort = 5041;
	private static final int[] ElevatorPort = {5043, 5044, 5045, 5046};
	private static final int FaultHandlerPort = 5066;
	
	private static final int MAXMESSAGESIZE = 1000;
	
	private static final int MAXFLOOR = 22;
	private static final int MINFLOOR = 0;
	
	private static final int TimeMovingBetweenFloors = 1318;
	private static final int TimeDoorsAreOpen = 5000;
	
	/**
	 * Getter for the IP to be used by the Floor Subsystem
	 * @return Returns the value stored in FloorSubsystemIP
	 */
	public static String getFloorsubsystemip() {
		return FloorSubsystemIP;
	}
	
	/**
	 * Getter for the IP to be used by the Scheduler Subsystem
	 * @return Returns the value stored in SchedulerIP
	 */
	public static String getSchedulerip() {
		return SchedulerIP;
	}
	
	/**
	 * Getter for the IP to be used by the Elevator Subsystem
	 * @return Returns the value stored in ElevatorIP
	 */
	public static String getElevatorip() {
		return ElevatorIP;
	}
	
	/**
	 * Getter for the Port to be used by the Floor Subsystem
	 * @return Returns the value stored in FloorSubsystemPort
	 */
	public static int getFloorsubsystemport() {
		return FloorSubsystemPort;
	}
	
	/**
	 * Getter for the Port to be used by the Scheduler Subsystem
	 * @return Returns the value stored in SchedulerPort
	 */
	public static int getSchedulerport() {
		return SchedulerPort;
	}
	
	/**
	 * Getter for one of the ports an Elevator will use
	 * @param id The ID of the Elevator, acts as an index to select which port will be used
	 * @return Returns the port stored in ElevatorPort[id]
	 */
	public static int getElevatorport(int id) {
		return ElevatorPort[id];
	}
	
	/**
	 * Getter method for the time delay for elevators going between floors
	 * @return Returns the time delay in ms
	 */
	public static int getElevatorTimeBetweenFloors() {
		return TimeMovingBetweenFloors;
	}
	
	/**
	 * Getter method for the time elevators will keep their doors open for
	 * @return Returns the time delay in ms
	 */
	public static int getElevatorTimeDoorsOpen() {
		return TimeDoorsAreOpen;
	}
	
	/**
	 * Returns the elevator id/num associated with the port number passed
	 * @param port The port at which the elevator is receiving messages
	 * @return Returns the id of the Elevator listening at port
	 */
	public static int getElevatorNumber(int port) {
		int counter = 0;
		for(int i: ElevatorPort) {
			if(i == port) {
				return counter;
			}
				counter++;
		}
		return (Integer) null;
	}
	
	/**
	 * Getter for the ip used by the FaultHandler
	 * @return Returns the ip address stored in FaultHandlerIP
	 */
	public static String getFaultHandlerIp() {
		return FaultHandlerIP;
	}
	
	/**
	 * Getter for the Port used by the FaultHandler
	 * @return Returns the port stored in FaultHandlerPort
	 */
	public static int getFaultHandlerPort() {
		return FaultHandlerPort;
	}
	
	/**
	 * Getter method for the maximum message size
	 * @return Returns the constant stored in MAXMESSAGESIZE
	 */
	public static int getMaxMessageSize() {
		return MAXMESSAGESIZE;
	}
	
	/**
	 * Getter method for the number of the top floor
	 * @return Returns the constant stored in MAXFLOOR
	 */
	public static int getMaxFloor() {
		return MAXFLOOR;
	}
	
	/**
	 * Getter method for the number of the bottom floor
	 * @return Returns the constant stored in MINFLOOR
	 */
	public static int getMinFloor() {
		return MINFLOOR;
	}
	
	/**
	 * Prints a line to divide sections of the terminal output
	 */
	public static void printLine() {
		System.out.println("-----------------");
	}
	
	/**
	 * Getter method for the array of elevator ports
	 * @return Returns the array stored in ElevatorPort
	 */
	public static int[] getElevatorPorts() {
		return ElevatorPort;
	}
	
	
}
