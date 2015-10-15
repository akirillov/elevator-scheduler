# Elevator Control System
## Description

Elevator Control System (ECS) has the following interface

      trait ElevatorControlSystem {
         def status(): Seq[Elevator]
         def update(id: Int, targetFloor: Int)
         def pickup(floor: Int, direction: Int)
         def step()
      }

* status    - querying the state of the elevators (what floor are they on and where they are going),
* update    - receiving an update about the status of an elevator (person inside an elevator hit the floor he wants to go), 
* pickup    - receiving a pickup request,
* step      - time-stepping the simulation.

######Clarification regarding `update` method:

Basic assumption about update method is that while it's nice to set up elevator status from control system, in reality it's 
hardly possible to assign a physical floor number to it (let's say change 5 to 10). So in terms of this implementation update method is considered as 
 a passenger action to specify the target floor where he wants to go (basically floor button press from the inside of elevator)

## Scheduling algorithm
Scheduling algorithm is targeted to minimizing passenger wait and travel time. This is achieved by two main parameters 
taken in consideration during load balancing pickup requests: 
 - the distance for the elevator to pick up the passenger
 - the amount of stops elevator is going to have
 
Algorithm features:
 
 * matching elevator is chosen based on the formula: distance from pickup floor + amount of already queued pickups/stops
 * if pickup call happens on the same floor in the same direction as it happened previously, it is assigned to the elevator 
 already having this stop in queue
 * elevator picks up the passengers only in the same direction which minimizes their travel time in case elevator is going 
 way too far in opposite direction
 * distance to the opposite direction call is computed as a whole travel path to one direction and then from the last stop to the pickup call,
    which provides means of wait time comparison between two missed elevators
 * amount of stops is used as well because stops are not free and take some time, so the same distance could take different time 
 depending on amount of stops (for the sake of simplicity current implementation uses total amount of stops, while picking up only
 those on a way of elevator to the pickup could improve the cost function)
   
######Free-form description:

Elevators pick up passengers only going in the same direction with elevator. Algorithm doesn't track elevator cabin capacity, so in case 
of full cabin the passenger should perform another call for elevator. Cost function takes into account the amount of stops in both 
direction queues at the moment of pickup request and does not recalculate this value on every update. The amount of stops in both queues is used to distinguish the load of the elevators with same distance to the target pickup.
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
 

      status                 - prints status and queues of all elevators
      update ID GFN          - passenger pushes the button of the goal floor (GFN, Int) from inside of the elevator with (ID, Int). Example: update 1 12
      pickup FLOOR DIRECTION - pickup request, FLOOR - floor number,  DIRECTION - positive or negative Int (positive - up, negative - down). Example: pickup 3 -1
      step [N]               - [N] simulation steps when all the elevators update their states making one move (up, down or no move), if N not specified, single step performed
      :q                     - quit the system


### Assumptions

- terminal input numbers are provided in proper format
- floors numeration is zero-based and there's no floors with negative numbers
