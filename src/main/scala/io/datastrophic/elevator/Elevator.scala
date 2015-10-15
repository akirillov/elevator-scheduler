package io.datastrophic.elevator

import scala.collection.immutable.SortedSet

case class Elevator(id: Int,
                    currentFloor: Int = 0,
                    mode: Int = 0,
                    upQueue: Set[Int] = SortedSet.empty[Int],
                    downQueue: Set[Int] = SortedSet.empty[Int]) {


   def enqueuePickup(pickupFloor: Int, direction: Int): Elevator = {
      if(direction > 0) this.copy(mode = getNewMode(pickupFloor), upQueue = upQueue + pickupFloor)
      else this.copy(mode = getNewMode(pickupFloor), downQueue = downQueue + pickupFloor)
   }

   def enqueueUpdate(targetFloor: Int): Elevator = {
      if(targetFloor > currentFloor) this.copy(mode = getNewMode(targetFloor), upQueue = upQueue + targetFloor)
      else this.copy(mode = getNewMode(targetFloor), downQueue = downQueue + targetFloor)
   }

   def getNewMode(floor: Int) = {
      if (mode == 0) {
         floor - currentFloor
      } else mode
   }
}