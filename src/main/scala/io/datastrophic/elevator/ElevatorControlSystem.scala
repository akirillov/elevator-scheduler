package io.datastrophic.elevator

trait ElevatorControlSystem {
   def status(): Seq[Elevator]
   def update(id: Int, targetFloor: Int)
   def pickup(floor: Int, direction: Int)
   def step()
}

