package Util.Comms;

/**
 * A configuration class for all of the values that could change throughout the program.
 * @author Nicholas Rose - 101181935
 */
public class Config {
	
	private static final String FloorSubsystemIP = "192.168.0.15";
	private static final String SchedulerIP = "192.168.0.15";
	private static final String ElevatorIP = "192.168.0.15";
	private static final String FaultHandlerIP = "192.168.0.15";
	
	private static final int FloorSubsystemPort = 5040;
	private static final int SchedulerPort = 5041;
	private static final int[] ElevatorPort = {5043, 5044, 5045, 5046};
	private static final int FaultHandlerPort = 5066;
	
	private static final int MAXMESSAGESIZE = 1000;
	
	private static final int MAXFLOOR = 22;
	private static final int MINFLOOR = 0;
	
	private static final int TimeMovingBetweenFloors = 1318;
	private static final int TimeDoorsAreOpen = 5000;
	
	//Getters
	
	public static String getFloorsubsystemip() {
		return FloorSubsystemIP;
	}
	public static String getSchedulerip() {
		return SchedulerIP;
	}
	public static String getElevatorip() {
		return ElevatorIP;
	}
	public static int getFloorsubsystemport() {
		return FloorSubsystemPort;
	}
	public static int getSchedulerport() {
		return SchedulerPort;
	}
	public static int getElevatorport(int id) {
		return ElevatorPort[id];
	}
	public static int getElevatorTimeBetweenFloors() {
		return TimeMovingBetweenFloors;
	}
	public static int getElevatorTimeDoorsOpen() {
		return TimeDoorsAreOpen;
	}
	/**
	 * Returns the elevator id/num associated with the port number passed
	 * @param port
	 * @return
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
	public static String getFaultHandlerIp() {
		return FaultHandlerIP;
	}
	public static int getFaultHandlerPort() {
		return FaultHandlerPort;
	}
	public static int getMaxMessageSize() {
		return MAXMESSAGESIZE;
	}
	public static int getMaxFloor() {
		return MAXFLOOR;
	}
	public static int getMinFloor() {
		return MINFLOOR;
	}
	public static void printLine() {
		System.out.println("-----------------");
	}
	public static int[] getElevatorPorts() {
		return ElevatorPort;
	}
	
	
}
