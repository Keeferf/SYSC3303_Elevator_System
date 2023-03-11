package Util.Comms;

/**
 * An enum for tracking the status of an Elevator Event
 * @author Nicholas Rose - 101181935
 *
 */
public enum RequestStatus {
	//NEW = Haven't been sent anywhere yet/ just arrived
	//ACKNOWLEDGED = A request has been acknowledged by the recipient and is being sent back
	//FULFILLED = For when a request has been completed by the elevator and is on its way back to floor
 NEW, ACKNOWLEDGED, FULFILLED
}
