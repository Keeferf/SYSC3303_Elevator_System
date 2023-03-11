package UDP;

import java.net.SocketException;

import FloorSystem.FloorSubsystem;

public class SimulateFloorSubsystem {

	public static void main(String[] args) {
		
		try {
			FloorSubsystem sub = new FloorSubsystem(5);
			Thread thread = new Thread(sub, "FloorSubsystem");
			thread.start();
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
