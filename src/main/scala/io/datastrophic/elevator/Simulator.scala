package io.datastrophic.elevator

trait Simulator {

   def step(elevator: Elevator): Elevator = {
      val upQueue = elevator.upQueue
      val downQueue = elevator.downQueue
      val mode = elevator.mode
      val currentFloor = elevator.currentFloor
      if(mode == 0){
         elevator
      } else if(mode > 0) {
         val newFloor = currentFloor + 1
         val newQueue = upQueue - newFloor
         if (newQueue.isEmpty) {
            if (downQueue.isEmpty) {
               elevator.copy(mode = 0, currentFloor = newFloor, upQueue = newQueue)
            } else {
               if(downQueue.last > currentFloor){
                  elevator.copy(currentFloor = newFloor, upQueue = newQueue)
               } else {
                  if(downQueue.last == currentFloor) {
                     elevator.copy(mode = -1, currentFloor = newFloor, upQueue = newQueue, downQueue = downQueue - currentFloor)
                  } else {
                     elevator.copy(mode = -1, currentFloor = newFloor, upQueue = newQueue)
                  }
               }
            }
         } else {
            elevator.copy(currentFloor = newFloor, upQueue = newQueue)
         }
      } else {
         val newFloor = currentFloor - 1
         val newQueue = downQueue - newFloor
         if (newQueue.isEmpty) {
            if (upQueue.isEmpty) {
               elevator.copy(mode = 0, currentFloor = newFloor, downQueue = newQueue)
            } else {
               if(upQueue.head < currentFloor){
                  elevator.copy(currentFloor = newFloor, downQueue = newQueue)
               } else {
                  if(upQueue.head == currentFloor) {
                     elevator.copy(mode = 1, currentFloor = newFloor, downQueue = newQueue, upQueue = upQueue - currentFloor)
                  } else {
                     elevator.copy(mode = 1, currentFloor = newFloor, downQueue = newQueue)
                  }
               }
            }
         } else {
            elevator.copy(currentFloor = newFloor, downQueue = newQueue)
         }
      }
   }

}
