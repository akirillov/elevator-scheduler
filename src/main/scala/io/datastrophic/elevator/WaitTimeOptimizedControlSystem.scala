package io.datastrophic.elevator

import scala.collection.mutable

class WaitTimeOptimizedControlSystem(val elevators: mutable.Map[Int, Elevator])
   extends ElevatorControlSystem with PickupCostCalculator with Simulator{

   override def status(): Seq[Elevator] = elevators.values.toList

   override def update(id: Int, targetFloor: Int): Unit = {
      elevators.update(id, elevators(id).enqueueUpdate(targetFloor))
   }

   override def pickup(floor: Int, direction: Int): Unit = {
      if(direction != 0) {
         val candidate = getCandidate(elevators.values.toList, floor, direction)
         elevators.update(candidate.id, candidate.enqueuePickup(floor, direction))
      }
   }

   override def step(): Unit = {
      elevators.values map step foreach {e => elevators.update(e.id, e)}
   }

   def getCandidate(elevators: Seq[Elevator], floor: Int, direction: Int): Elevator ={
      elevators.map(e => (calculatePickupCost(e, floor, direction), e)).toList.sortWith(_._1 < _._1).head._2
   }
}

object WaitTimeOptimizedControlSystem {
   def apply(numberOfElevators: Int): WaitTimeOptimizedControlSystem ={
      val elevatorsMap = new mutable.HashMap[Int, Elevator]()
      (1 to numberOfElevators).foreach(n => elevatorsMap.put(n, Elevator(n)))
      new WaitTimeOptimizedControlSystem(elevatorsMap)
   }
}
