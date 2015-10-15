package io.datastrophic.elevator

import scala.io.StdIn._

object ElevatorControlSystemApp extends App {

   print("Welcome to the Elevator Control System\n\nPlease specify number of elevators to control:")

   val totalElevators = readInt()

   if(totalElevators < 1) {
      println("No elevators to control")
   } else {
      val helpMessage =
         """
           |status                 - prints status and queues of all elevators
           |update ID GFN          - passenger pushes the button of the goal floor (GFN, Int) inside elevator with (ID, Int). Example: update 1 12
           |pickup FLOOR DIRECTION - pickup request, FLOOR - floor number,  DIRECTION - positive or negative Int (positive - up, negative - down). Example: pickup 3 -1
           |step [N]               - [N] simulation steps when all the elevators update their states making one move (up or down), if N not specified, single step performed
           |:q                     - quit the system
         """.stripMargin

      val system = WaitTimeOptimizedControlSystem(totalElevators)

      println(s"System initialized with $totalElevators elevators. Usage:\n$helpMessage")

      var done = false
      while(!done){
         print(">")
         readLine().split(" ").toList match {
            case List(":q") => done = true
            case List("status") => println(system.status())
            case List("step") => system.step()
            case List("step", n) => (1 to n.toInt).foreach(t => system.step())
            case List("pickup", id, direction) => system.pickup(id.toInt, direction.toInt)
            case List("update", id, targetFloor) => system.update(id.toInt, targetFloor.toInt)
            case _ => println("Unknown command")
         }
      }

      println("Session is over. Bye.")
   }
}