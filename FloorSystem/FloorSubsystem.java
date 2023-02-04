package FloorSystem;

public class FloorSubsystem implements Runnable{

	/**
	 * Constructor for the Floor Subsystem class
	 */
	public FloorSubsystem() {
		
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Prints out the request which was completed, i.e. the passenger arrived at their destination floor
	 * @param completedRequest Passenger request which was completed
	 */
	public void alert(ElevatorEvent completedRequest) {
		System.out.println(completedRequest.toString());
	}
}
