package UDP;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import FloorSystem.ElevatorEvent;

/**
 * A class for doing UDP packet operations
 * @author Nicholas Rose - 101181935
 *
 */
public abstract class UDPBuilder {
	
	/**
	 * Serializes an elevator event and adds it into a UDP packet
	 * @param elevatorEvent ElevatorEvent
	 * @param location String
	 * @param port int
	 * @return DatagramPacket
	 */
	public static DatagramPacket newMessage(ElevatorEvent elevatorEvent, String location, int port) {
		
		ElevatorEvent e;
		try {
			e = (ElevatorEvent)elevatorEvent.clone();
			byte[] data = ElevatorEvent.serialize(e);
			
			return new DatagramPacket(data, data.length, InetAddress.getByName(location), port);
		} catch (CloneNotSupportedException exception) {
			System.out.println("Cloneing of elevator object failed: " + exception.getMessage());
			exception.printStackTrace();
			throw new RuntimeException(exception);
		} catch (UnknownHostException e1) {
			System.out.println("Retrieving of InetAddress failed: " + e1.getMessage());
			e1.printStackTrace();
			throw new RuntimeException(e1);
		}
		
	}
	
	/**
	 * Formulates an acknowledgement to send back to the sender.
	 * @param elevatorEvent ElevatorEvent
	 * @param location String
	 * @param port int
	 * @return DatagramPacket
	 */
	public static DatagramPacket acknowledge(ElevatorEvent elevatorEvent, String location, int port) {
		
		try {
			ElevatorEvent e = (ElevatorEvent) elevatorEvent.clone();
			
			e.setRequestStatus(RequestStatus.ACKNOWLEDGED);
			
			return UDPBuilder.newMessage(e,location,port);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Deserializes the payload of a particular packet
	 * @param p DatagramPacket
	 * @return ElevatorEvent
	 */
	public static ElevatorEvent getPayload(DatagramPacket p) {
		if(ElevatorEvent.deserialize(p.getData()) instanceof ElevatorEvent) {
			return ElevatorEvent.deserialize(p.getData());
		}
		throw new RuntimeException("Error: Payload is not instance of Elevator Event");
		
		
	}
}
