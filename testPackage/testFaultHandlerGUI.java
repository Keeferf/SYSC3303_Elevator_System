package testPackage;

import java.util.ArrayList;

import Scheduler.FaultHandler.FaultState;
import Scheduler.FaultHandler.GUI.FaultHandlerFrame;

public class testFaultHandlerGUI {

	public static void main(String[] args) {
	    	FaultHandlerFrame frame = new FaultHandlerFrame();
	        
	        //Create and populate fake list to pass to GUI
	        ArrayList<ArrayList<FaultState>> faultList = new ArrayList<>();
	        
	        for(int i = 0; i < 4;i++) {//4 elevators
	        	ArrayList<FaultState> states = new ArrayList<>();
	        	for(int z = 0; z < 4;z++) {	//4 timing events to be caught
	        		states.add(FaultState.UNFULFILLED);
	        	}
	        	faultList.add(i, states);
	        }
	        
	        frame.update(faultList);
	        
	        try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        faultList.get(2).set(1, FaultState.COMPLETED);
	        faultList.get(1).set(3, FaultState.ERROR);
	        
	        frame.update(faultList);

	}

}
