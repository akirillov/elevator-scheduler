package io.datastrophic.elevator

trait PickupCostCalculator {

   def calculatePickupCost(elevator: Elevator, pickupFloor: Int, direction: Int): Int = {
      val upQueue = elevator.upQueue
      val downQueue = elevator.downQueue
      val mode = elevator.mode
      val currentFloor = elevator.currentFloor

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
               (upQueue.last - currentFloor) + Math.abs(upQueue.last - pickupFloor) + upQueue.size + downQueue.size

            //elevator passed pickup, passenger goes up, elevator goes down
            case (true, true, false) =>
               //get to the bottom and back to pickup
               (currentFloor - downQueue.head) + (pickupFloor - downQueue.head) + upQueue.size + downQueue.size

            //elevator passed pickup, passenger goes down, elevator goes down
            case (true, false, false) =>
               if(pickupFloor == currentFloor) {
                  upQueue.size + downQueue.size
               } else if(pickupFloor >= upQueue.last){
                  (currentFloor - downQueue.head) + (pickupFloor - downQueue.head) + upQueue.size + downQueue.size
               } else {
                  (currentFloor - downQueue.head) + (upQueue.last - downQueue.head) + (upQueue.last - pickupFloor) + upQueue.size + downQueue.size
               }

            //elevator passed the pickup floor, passenger goes up, elevator goes up
            case (false, true, true) =>
               if(pickupFloor <= downQueue.head){
                  (upQueue.last - currentFloor) + (upQueue.last - pickupFloor) + upQueue.size + downQueue.size
               } else {
                  (upQueue.last - currentFloor) + (upQueue.last - downQueue.head) + (pickupFloor - downQueue.head) + upQueue.size + downQueue.size
               }

            //elevator passed the pickup floor, passenger goes down, elevator goes up
            case (false, false, true) =>
               //to the top and back, continue to pickup
               (upQueue.last - currentFloor) + (upQueue.last - pickupFloor) + upQueue.size + downQueue.size

            //pickup on the elevator way, passenger goes up, elevator goes down
            case (false, true, false) =>
               //get to the bottom and back, continue to pickup
               if(pickupFloor <= downQueue.head){
                  (currentFloor - Math.min(downQueue.head, pickupFloor)) + upQueue.size + downQueue.size
               } else {
                  (currentFloor - downQueue.head) + (pickupFloor - downQueue.head) + upQueue.size + downQueue.size
               }

            //pickup on the elevator way, passenger goes down, elevator goes down
            case (false, false, false) =>
               currentFloor - pickupFloor + upQueue.size + downQueue.size
         }
      }
   }
}
