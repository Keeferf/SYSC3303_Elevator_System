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
5. In Config.java, change the FloorSubsystem, Scheduler, Elevator, and FaultHandler IP Addresses
    - Note: Scheduler and FaultHandler IPs must match, as they are meant to be run on the same device
6. Navigate to "Simulation" package\
7. From there, you will find the three files "SimulationScheduler.java", "SimulationElevator.java", "SimulationFloorSubsystem.java".\
8. Run the main method in those files in the following order
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
Implements Fault Detection
Implements Fault Handler Classes
Implements Fault Handler Tests + GUI
UML Diagrams
Documentation

Colin Mandeville - 101140289
Extend Elevator State Machine
Implements Fault Handling
UML Diagrams, Elevator State diagram, Timing Diagram
Documentation

Keefer Belanger - 101152085
Implements System Reading Faults from Input File
Extend Elevator Event Class
UML Diagrams, Timing Diagrams
Documentation

Joshua Noronha - 101194076
Implement Arrival Sensor
UML Diagrams, Sequence Diagram
Documentation
