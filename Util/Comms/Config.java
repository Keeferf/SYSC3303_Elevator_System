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
	private static final int ElevatorPort = 5042;
	
	private static final int MAXMESSAGESIZE = 1000;
	
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
	
	
}
