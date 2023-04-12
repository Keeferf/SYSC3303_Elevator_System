package FloorSystem;

import javax.swing.JFrame;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.border.LineBorder;
import java.awt.Color;

import javax.swing.border.TitledBorder;

import Elevator.Elevator;
import Elevator.ElevatorStateEvent;
import Elevator.ErrorState;
import Scheduler.FaultHandler.FaultState;
import Scheduler.FaultHandler.GUI.FaultHandlerFrame;

import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.UIManager;


public class GUI {

	private int floorNum;
	private int elevatorNum; 
	//private Elevator elevator;
	private FaultState fault;
	private JLabel[] floorTitles;
	private JLabel[][] floors;
	private JPanel[] displays;
	private JFrame ElevatorFrame;
	private JLabel[][] elevInfo;
	private JPanel[] elevInfoPanels;
	private static final int DEFAULT_COLUMN_WIDTH = 65;
	private static final int DEFAULT_ROW_HEIGHT = 35;
	private static final int DEFAULT_FLOOR_ROW_HEIGHT = 50;
	private static final double DEFAULT_ROW_WEIGHT = 1.0;

	private FaultHandlerFrame FHF;

	public GUI() {
		this.floorNum = 22;
		this.elevatorNum = 4;
		//this.elevator = new Elevator();
		this.FHF = new FaultHandlerFrame();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		int rowHeight = 30 * floorNum;
		int GUIWidth = 50 + (50 * elevatorNum);
        GUIWidth += (elevatorNum / 2) * 350;

		ElevatorFrame = new JFrame("Elevator System");
		ElevatorFrame.getContentPane().setBackground(UIManager.getColor("Button.background"));
		ElevatorFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("Util\\Images\\CULogo.png"));
		GridBagLayout mainGBL = new GridBagLayout();
		mainGBL.columnWidths = new int[] {GUIWidth};
		mainGBL.rowHeights = new int[] {rowHeight};
		mainGBL.columnWeights = new double[]{1.0};
		mainGBL.rowWeights = new double[]{0.0};
		ElevatorFrame.getContentPane().setLayout(mainGBL);

		//Setup for the entire display area
		JPanel displayPanel = new JPanel();
		displayPanel.setBorder(new LineBorder(Color.BLACK));
		ElevatorFrame.getContentPane().add(displayPanel);
		GridBagLayout displayPanelGBL = new GridBagLayout();
		int columns = 1 + elevatorNum + 1; // adds the floor column, elevator columns, then the data column
		int[] columnWidths = new int[columns];
		for (int i = 0; i < columns; i++) { //loops over all the columns in the display
			if(i != 1 + elevatorNum) { //checks to make sure the column isnt the elevator info
				columnWidths[i] = DEFAULT_COLUMN_WIDTH;
			}else {
				columnWidths[i] = (elevatorNum / 2) * 350;
			}
		}

		displayPanelGBL.columnWidths = columnWidths;
		displayPanel.setLayout(displayPanelGBL);

		//Setup for the floor panel area
		JPanel floorPanel = new JPanel();
		floorPanel.setBackground(UIManager.getColor("Button.background"));
		floorPanel.setBorder(new TitledBorder(new LineBorder(null), "Floors", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		GridBagConstraints floorPanelGBC = new GridBagConstraints();
		floorPanelGBC.fill = GridBagConstraints.VERTICAL;
		floorPanelGBC.insets = new Insets(0, 0, 0, 5);
		displayPanel.add(floorPanel, floorPanelGBC);
		GridBagLayout floorPanelGBL = new GridBagLayout();
		floorPanelGBL.columnWidths = new int[] {DEFAULT_FLOOR_ROW_HEIGHT};
		int[] tempArr = new int[floorNum];
		//initialize the temp array
		for (int j = 0; j < floorNum; j++) {
			tempArr[j] = DEFAULT_ROW_HEIGHT;
		}
		floorPanelGBL.rowHeights = tempArr;
		floorPanelGBL.columnWeights = new double[]{0.0};
		double[] temp = new double[floorNum];
		for (int j = 0; j < floorNum; j++) {
			temp[j] = DEFAULT_ROW_WEIGHT;
		}
		floorPanelGBL.rowWeights = temp;
		floorPanel.setLayout(floorPanelGBL);

		//initialize floor titles
		floorTitles = new JLabel[floorNum];
		for(int i = 1; i <= floorNum; i++) {
			floorTitles[i - 1] = new JLabel(Integer.toString(floorNum - i + 1));
			floorTitles[i - 1].setHorizontalAlignment(SwingConstants.CENTER);
			GridBagConstraints floorTitle = new GridBagConstraints();
			floorTitle.gridy = i - 1;
			floorPanel.add(floorTitles[i - 1], floorTitle);
		}
		floors = new JLabel[elevatorNum][floorNum];

		//create the elevator displays
		displays = new JPanel[elevatorNum];
		for(int i = 1; i <= elevatorNum; i++) {
			displays[i - 1] = new JPanel();
			displays[i - 1].setBorder(new TitledBorder(new LineBorder(null), new String("Elevator " + Integer.toString(i - 1)), TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints elevatorDisplayGBC = new GridBagConstraints();
			elevatorDisplayGBC.insets = new Insets(0, 0, 0, 5);
			displayPanel.add(displays[i - 1], elevatorDisplayGBC);
			GridBagLayout gbl_elevatorDisplay = new GridBagLayout();
			gbl_elevatorDisplay.columnWidths = new int[] {DEFAULT_COLUMN_WIDTH};
			gbl_elevatorDisplay.rowHeights = tempArr;
			gbl_elevatorDisplay.columnWeights = new double[]{0.0};
			gbl_elevatorDisplay.rowWeights = temp;
			displays[i - 1].setLayout(gbl_elevatorDisplay);

			//create the floors for the elevator
			for (int j = 0; j < floorNum; j++) {
				floors[i-1][floorNum - 1 - j] = new JLabel("");
				floors[i-1][floorNum - 1 - j].setIcon(new ImageIcon("Util\\Images\\Closed.png"));
				floors[i-1][floorNum - 1 - j].setHorizontalAlignment(SwingConstants.CENTER);
				GridBagConstraints floorGBC = new GridBagConstraints();
				floorGBC.fill = GridBagConstraints.BOTH;
				floorGBC.insets = new Insets(0, 0, 5, 0);
				floorGBC.gridx = 0;
				floorGBC.gridy = j;
				displays[i - 1].add(floors[i-1][floorNum - 1 - j], floorGBC);
			}
			floors[i-1][0].setIcon(new ImageIcon("Util\\Images\\Moving.jpg"));
		}

		JPanel infoPanel = new JPanel();
		GridBagConstraints infoPanelGBC = new GridBagConstraints();
		infoPanelGBC.insets = new Insets(0, 0, 0, 5);
		infoPanelGBC.fill = GridBagConstraints.BOTH;
		infoPanelGBC.gridx = columns - 1;
		infoPanelGBC.gridy = 0;
		displayPanel.add(infoPanel, infoPanelGBC);

		infoPanel.setLayout(new GridLayout(2, 4, 0, 0));

		elevInfoPanels = new JPanel[elevatorNum];
		elevInfo = new JLabel[elevatorNum][4];
		for(int i = 0; i < elevatorNum; i++) {
			elevInfoPanels[i] = new JPanel();
			elevInfoPanels[i].setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)),new String( "Elevator "+ Integer.toString(i) +" Info"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			infoPanel.add(elevInfoPanels[i]);
			elevInfoPanels[i].setLayout(new GridLayout(0, 1, 0, 0));

			elevInfo[i][0] = new JLabel("Direction: ");
			elevInfo[i][0].setFont(new Font("Inter", Font.PLAIN, 20));
			elevInfoPanels[i].add(elevInfo[i][0]);

			elevInfo[i][1] = new JLabel("Request: ");			
			elevInfo[i][1].setFont(new Font("Inter", Font.PLAIN, 20));
			elevInfoPanels[i].add(elevInfo[i][1]);
			
			elevInfo[i][2] = new JLabel("Fault: ");
			elevInfo[i][2].setFont(new Font("Inter", Font.PLAIN, 20));
			elevInfoPanels[i].add(elevInfo[i][2]);
			
		}

		ElevatorFrame.setVisible(true);
		ElevatorFrame.setResizable(false);
        ElevatorFrame.pack();
		ElevatorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void setDirectionInfo(ElevatorStateEvent elevator){
		if(elevator.getCurrFloor() < elevator.getTargetFloor()) {
			elevInfo[elevator.getElevatorNum()][0].setText("Direction: UP");
		}else if (elevator.getCurrFloor() > elevator.getTargetFloor()) {
			elevInfo[elevator.getElevatorNum()][0].setText("Direction: DOWN");
		}else {
			elevInfo[elevator.getElevatorNum()][0].setText("Direction: ON CURRENT FLOOR");
		}
	}

	public void setRequestInfo(ElevatorStateEvent elevator){
		String tempRequests = "Target Floor: ";
		if(elevator.toString().isEmpty()){
			tempRequests += "No Requests";
		}
		else{
			tempRequests += elevator.getTargetFloor();
		}
		elevInfo[elevator.getElevatorNum()][1].setText(tempRequests);
	}

	public void setFaultInfo(ElevatorStateEvent elevator){
		if(elevator.getErrorState().equals(ErrorState.NO_ERROR)) {
			elevInfo[elevator.getElevatorNum()][2].setText("Fault: NO FAULT");
		} else {	//Might need to put conditional here
			elevInfo[elevator.getElevatorNum()][2].setText("Fault: " + elevator.getErrorState());
			for (int i = 0; i <floorNum; i++) {
				floors[elevator.getElevatorNum()][i].setIcon(new ImageIcon("Util\\Images\\Shutdown.png"));
			}
		}
	}
	
	public void update(ArrayList<ElevatorStateEvent> states) {
		for(ElevatorStateEvent e: states) {
			if(e != null) {
				setDirectionInfo(e);
				setRequestInfo(e);
				setFaultInfo(e);
			}
			
		}
		updateDisplay(states);
	}
	
	private void updateDisplay(ArrayList<ElevatorStateEvent> states) {
		for(ElevatorStateEvent e: states) {
			if(e == null) continue;
			
			for(int i = 0; i < floorNum; i++) {
				if(!(e.getErrorState() == ErrorState.NO_ERROR)) {
					//Process error
					floors[e.getElevatorNum()][i].setIcon(new ImageIcon("Util\\Images\\Shutdown.png"));
					
				} else {
					//Process regular change
					
					//check if its the current floor
					if(e.getCurrFloor() == i) {
						System.out.
							println(i);
						
						System.
							out.
								println
									(e.
											getCurrFloor
														()
														)
										;
						floors[e.getElevatorNum()][0].setIcon(new ImageIcon("Util\\Images\\Moving.jpg"));
					} else {
						//else make it gray
						floors[e.getElevatorNum()][floorNum - 1 - i].setIcon(new ImageIcon("Util\\Images\\Closed.png"));
					}
					
					
				}
			}
		}
	}

	public int getNumElevators() {
		return elevatorNum;
	}
	
	public int getNumFloors() {
		return floorNum;
	}
}
