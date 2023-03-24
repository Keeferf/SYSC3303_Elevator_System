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

public class FaultHandlerFrame extends JFrame implements FaultHandlerView{
	
	private static final int HEIGHT = 500;
	private static final int WIDTH = 500;

	public FaultHandlerFrame() {
		super("Fault Handler 3000");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700,480);
		
		getContentPane().setLayout(
			    new BoxLayout(getContentPane(), BoxLayout.X_AXIS)
			);
		
		setLocationRelativeTo(null);	//centers
		
		pack();
		
		setVisible(true);
	}
	
	/**
	 * Adds the JPanels as needed to the frame
	 */
	@Override
	public void update(ArrayList<ArrayList<FaultState>> faultStates) {
		//remove current components on board
		for(Component c: getContentPane().getComponents()) {
			remove(c);
		}
		
		int elevatorNum = 0;
		//Add one by one
		for(ArrayList<FaultState> states: faultStates) {
			
			add(createPanel(states,elevatorNum));
			
			elevatorNum++;
		}
		
		pack();
		//setSize(WIDTH,HEIGHT);
	}
	
	private JPanel createPanel(ArrayList<FaultState> states, int elevatorNum) {
		JPanel panel = new JPanel();
		
//		BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
//		
//		setLayout(boxLayout);
		
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

}
