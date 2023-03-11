package Util.Comms;

/**
 * A configuration class for all of the values that could change throughout the program.
 * @author Nicholas Rose - 101181935
 */
public class Config {
	
	private static final String FloorSubsystemIP = "192.168.56.1";
	private static final String SchedulerIP = "192.168.56.1";
	private static final String ElevatorIP = "192.168.56.1";
	
	private static final int FloorSubsystemPort = 5040;
	private static final int SchedulerPort = 5041;
	private static final int ElevatorPort = 5043;
	
	private static final int MAXMESSAGESIZE = 1000;
	
	private static final int MAXFLOOR = 5;
	private static final int MINFLOOR = 0;
	
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
	public static int getElevatorport() {
		return ElevatorPort;
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
	
	
}
