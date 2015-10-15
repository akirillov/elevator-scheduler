package io.datastrophic.elevator

import java.util

import scala.collection.immutable.SortedSet

trait PickupCostCalculator {

   def calculatePickupCost(elevator: Elevator, pickupFloor: Int, direction: Int): Int = {
      val upQueue = elevator.upQueue
      val downQueue = elevator.downQueue
      val mode = elevator.mode
      val currentFloor = elevator.currentFloor
      val top = getTopFloor(upQueue, downQueue)
      val bottom = getBottomFloor(upQueue, downQueue)

      if(mode == 0){ //elevator in idle mode at any floor
         Math.abs(currentFloor - pickupFloor)
      } else {
         //check for already scheduled pickups
         if(mode > 0 && upQueue.contains(pickupFloor)) 0
         else if(mode < 0 && downQueue.contains(pickupFloor)) 0
         else (pickupFloor >= currentFloor, direction > 0, mode > 0) match {
            //pickup on the elevator way, passenger goes up, elevator goes up
            case (true, true, true) =>
               pickupFloor - currentFloor + upQueue.size + downQueue.size

            //pickup on the elevator way, passenger goes down, elevator goes up
            case (true, false, true) =>
               //get to the top and back to pickup
               (top - currentFloor) + Math.abs(top - pickupFloor) + upQueue.size + downQueue.size

            //elevator passed pickup, passenger goes up, elevator goes down
            case (true, true, false) =>
               //get to the bottom and back to pickup
               if (pickupFloor > top) {
                  (currentFloor - bottom) + (pickupFloor - bottom) + upQueue.size + downQueue.size
               } else {
                  2 * (top - bottom) - (pickupFloor - currentFloor) + upQueue.size + downQueue.size
               }
               (currentFloor - downQueue.head) + (pickupFloor - downQueue.head) + upQueue.size + downQueue.size

            //elevator passed pickup, passenger goes down, elevator goes down
            case (true, false, false) =>
               if(pickupFloor == currentFloor) {
                  upQueue.size + downQueue.size
               } else {
                  if (pickupFloor > top) {
                     (currentFloor - bottom) + (pickupFloor - bottom) + upQueue.size + downQueue.size
                  } else {
                     2 * (top - bottom) - (pickupFloor - currentFloor) + upQueue.size + downQueue.size
                  }
               }

            //elevator passed the pickup floor, passenger goes up, elevator goes up
            case (false, true, true) =>
               if (pickupFloor < bottom) {
                  (top - currentFloor) + (top - pickupFloor) + upQueue.size + downQueue.size
               } else {
                  2 * (top - bottom) - (currentFloor - pickupFloor) + upQueue.size + downQueue.size
               }

            //elevator passed the pickup floor, passenger goes down, elevator goes up
            case (false, false, true) =>
               //to the top and back, continue to pickup
               2*( top - currentFloor) + (currentFloor - pickupFloor) + upQueue.size + downQueue.size

            //pickup on the elevator way, passenger goes up, elevator goes down
            case (false, true, false) =>
               //get to the bottom and back, continue to pickup
               if(pickupFloor < bottom){
                  (currentFloor - pickupFloor) + upQueue.size + downQueue.size
               } else {
                  (currentFloor - bottom) + (pickupFloor - bottom) + upQueue.size + downQueue.size
               }

            //pickup on the elevator way, passenger goes down, elevator goes down
            case (false, false, false) =>
               currentFloor - pickupFloor + upQueue.size + downQueue.size
         }
      }
   }

   def getTopFloor(upQ: SortedSet[Int], downQ: SortedSet[Int]): Int ={
      if(upQ.nonEmpty && downQ.nonEmpty){
         Math.max(upQ.last, downQ.last)
      } else if(upQ.nonEmpty){
         upQ.last
      } else if(downQ.nonEmpty){
         downQ.last
      } else 0
   }

   def getBottomFloor(upQ: SortedSet[Int], downQ: SortedSet[Int]): Int ={
      if(upQ.nonEmpty && downQ.nonEmpty){
         Math.min(upQ.head, downQ.head)
      } else if(upQ.nonEmpty){
         upQ.head
      } else if(downQ.nonEmpty){
         downQ.head
      } else 0
   }
}
