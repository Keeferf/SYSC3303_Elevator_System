package Scheduler.FaultHandler.GUI;

import java.util.ArrayList;

import Scheduler.FaultHandler.FaultState;

public interface FaultHandlerView {

	public void update(ArrayList<ArrayList<FaultState>> faultsStates);
}
