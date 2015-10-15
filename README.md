# Elevator Control System
## Description

Elevator Control System (ECS) has next interface

```
trait ElevatorControlSystem {
   def status(): Seq[Elevator]
   def update(id: Int, targetFloor: Int)
   def pickup(floor: Int, direction: Int)
   def step()
}
```

## Scheduling algorithm
Minimizing passenger wait and travel time. Achieved by 
 - calculating distance for the elevator to pick up the passenger
 - elevators pick up passengers only when move in the same direction

Algorithm doesn't track elevator cabin capacity, so in case of full cabin the passenger should perform another call for elevator. 
Cost function takes into account the amount of stops in both direction queues at the moment of pickup request and does not recalculate this value 
on every update. The amount of stops in both queues is used to distinguish the load of the elevators with same distance to the target pickup.
The direction of route is not taken into account when using queues sizes for the sake of simplicity, but of course it could make the cost
 function more accurate.

## How to use
### Software requirements

* Scala 2.11
* SBT 0.13.8

### Running

      git clone git@github.com:akirillov/elevator-scheduler.git
      cd elevator-scheduler
      sbt run


### CLI
ECS provides a simple command-line interface to issue commands:
 
```
status                 - prints status and queues of all elevators
update ID GFN          - passenger pushes the button of the goal floor (GFN, Int) from inside of the elevator with (ID, Int). Example: update 1 12
pickup FLOOR DIRECTION - pickup request, FLOOR - floor number,  DIRECTION - positive or negative Int (positive - up, negative - down). Example: pickup 3 -1
step [N]               - [N] simulation steps when all the elevators update their states making one move (up, down or no move), if N not specified, single step performed
:q                     - quit the system
```

### Assumptions

- terminal input numbers are provided in proper format
- floors numeration is zero-based and there's no floors with negative numbers
