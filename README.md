# SYSC3303_Elevator_System

## SYSC 3303 Project - Group 5

## Project Authors

* Joshua Noronha : 101194076
* Colin Mandeville : 101140289
* Nicholas Rose : 101181935
* Keefer Belanger : 101152085

## Naming of Files
Camelcase no spaces/underscores.\
Must accurately describe the goal of the file/class.

## Setup Instructions
1. Unzip the Elevator System project onto your local machine.\
2. Open the unzipped folder in eclipse\
3. Navigate to the "Util.Comms" package\
4. From there you can see the Config.java file. Open it.\
5. Open up a command prompt and type "ipconfig". Copy paste the IpV4 address into the config.
6. In Config.java, change the FloorSubsystem, Scheduler, Elevator, and FaultHandler IP Addresses
    - Note: Scheduler and FaultHandler IPs must match, as they are meant to be run on the same device
7. Navigate to "Simulation" package\
8. From there, you will find the three files "SimulationScheduler.java", "SimulationElevator.java", "SimulationFloorSubsystem.java".\
9. Run the main method in those files in the following order
    1) SimulationScheduler.java
    2) SimulationElevator.java
    3) SimulationFloorSubsyste.java

## Testing Instructions
1. Unzip the Elevator System project onto your local machine.\
2. Open the unzipped folder in eclipse\
3. Navigate to the folder that will offer the test you wish to execute. All unit test files start with lowercase "test".\
4. Pressing run while within the class will execute the tests.

Executing the main within the Simulation class will read in the test events for the program from Events.txt

## Test files used
Included within the zipped eclipse project is a text file named <b>Events.txt</b>. This file includs the formatted events\
which are read into the program at the start. It is already in the correct location for testing purposes.

## Project Iteration 4 - Breakdown of Responsibilities

Nicholas Rose - 101181935
- Elevator State events getting sent to GUI
- General bug fixing for GUI
- Diagrams + Documentation


Colin Mandeville - 101140289
- Implemented Timing/Measurement
- Updated Javadocs for all methods
- Updated Diagrams
- Fixed Test Cases


Keefer Belanger - 101152085
- Implemented GUI
- General bug fixing
- Diagrams & Documentation


Joshua Noronha - 101194076
- GUI implementation
- UML Diagrams, Sequence Diagram
- Documentation

