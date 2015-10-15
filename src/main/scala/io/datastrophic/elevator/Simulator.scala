package io.datastrophic.elevator

import scala.collection.immutable.SortedSet

trait Simulator {

   def step(elevator: Elevator): Elevator = {
      val upQueue = elevator.upQueue
      val downQueue = elevator.downQueue
      val mode = elevator.mode
      val currentFloor = elevator.currentFloor

      if(mode == 0){
         elevator
      } else if(mode > 0) {
         val candidate = currentFloor + 1

         if(isLast(upQueue, downQueue, candidate)){
            elevator.copy(currentFloor = candidate, mode = 0, upQueue = SortedSet.empty[Int], downQueue = SortedSet.empty[Int])
         } else {
            if(isLast(upQueue, candidate)){
               if(candidate < downQueue.last){
                  elevator.copy(currentFloor = candidate, upQueue = upQueue - candidate)
               } else if(downQueue.last == candidate){
                  elevator.copy(mode = -1, currentFloor = candidate, upQueue = upQueue - candidate, downQueue = downQueue - candidate)
               } else {
                  elevator.copy(mode = -1, currentFloor = candidate, upQueue = upQueue - candidate)
               }
            } else {
               if(upQueue.last > candidate) {
                  elevator.copy(currentFloor = candidate, upQueue = upQueue - candidate)
               } else {
                  elevator.copy(currentFloor = candidate, mode = -1, upQueue = upQueue - candidate)
               }
            }
         }
      } else {
         val candidate = currentFloor - 1

         if(isLast(upQueue, downQueue, candidate)){
            elevator.copy(currentFloor = candidate, mode = 0, upQueue = SortedSet.empty[Int], downQueue = SortedSet.empty[Int])
         } else {
            if(isLast(downQueue, candidate)){
               if(candidate > upQueue.head){
                  elevator.copy(currentFloor = candidate, downQueue = downQueue - candidate)
               } else if(upQueue.last == candidate){
                  elevator.copy(mode = 1, currentFloor = candidate, upQueue = upQueue - candidate, downQueue = downQueue - candidate)
               } else {
                  elevator.copy(mode = 1, currentFloor = candidate, downQueue = downQueue - candidate)
               }
            } else {
               if(downQueue.head < candidate) {
                  elevator.copy(currentFloor = candidate, downQueue = downQueue - candidate)
               } else {
                  elevator.copy(currentFloor = candidate, mode = 1, downQueue = downQueue - candidate)
               }
            }
         }
      }
   }

   def isLast(upQ: Set[Int], downQ: Set[Int], candidate: Int): Boolean = {
      (upQ - candidate).isEmpty && (downQ - candidate).isEmpty
   }

   def isLast(upQ: Set[Int], candidate: Int): Boolean = {
      (upQ - candidate).isEmpty
   }
}