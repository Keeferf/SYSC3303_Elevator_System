package Scheduler.FaultHandler.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import Scheduler.FaultHandler.FaultState;

public class FaultHandlerFrame implements FaultHandlerView{
	
	private static final int HEIGHT = 500;
	private static final int WIDTH = 500;

	private JPanel allFaultHandlerPanel;

	/**
	 * Constructor for FaultHandlerFrame
	 */
	public FaultHandlerFrame() {
		allFaultHandlerPanel = new JPanel();
		
		allFaultHandlerPanel.setLayout(new BoxLayout(allFaultHandlerPanel, BoxLayout.X_AXIS));
			
		JLabel title = new JLabel("<html><b>Starting Up...</u></html>", JLabel.CENTER);
		title.setSize(WIDTH/4,HEIGHT);
		
		title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		allFaultHandlerPanel.add(title);
	}
	
	/**
	 * Adds the JPanels as needed to the frame
	 */
	@Override
	public void update(ArrayList<ArrayList<FaultState>> faultStates) {
		//remove current components on board
		allFaultHandlerPanel.removeAll();
		
		int elevatorNum = 0;
		//Add one by one
		for(ArrayList<FaultState> states: faultStates) {
			allFaultHandlerPanel.add(createPanel(states, elevatorNum));
			elevatorNum++;
		}
		allFaultHandlerPanel.revalidate();
		allFaultHandlerPanel.repaint();
	}
	
	/**
	 * Initiate the JPanel used by the FaultHandler
	 * @param states The set of fault states for the elevator
	 * @param elevatorNum The id of the elevator
	 * @return Returns the panel created containing the elevator states
	 */
	private JPanel createPanel(ArrayList<FaultState> states, int elevatorNum) {
		JPanel panel = new JPanel();
	
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		panel.setSize(WIDTH/4,HEIGHT);
		
		//Add title panel
		JLabel title = new JLabel("<html><u>Elevator " + elevatorNum + "</u></html>", JLabel.CENTER);
		title.setSize(WIDTH/4,HEIGHT);
		
		title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		panel.add(title);
		
		for(FaultState f: states) {
			JLabel label = new JLabel(f.toString());
			if(f.equals(FaultState.COMPLETED)) {
				label.setForeground(Color.GREEN);
			} else if(f.equals(FaultState.ERROR)) {
				label.setForeground(Color.RED);
			}
			panel.add(label);
		}
		panel.setBorder(BorderFactory.createLineBorder(Color.black));
		return panel;
	}

	/**
	 * Getter for the panel of the FaultHandlerFrame
	 * @return Returns the JPanel representing the frame of the FaultHandlerFrame
	 */
	public JPanel getPanel(){
		return allFaultHandlerPanel;
	}
}
