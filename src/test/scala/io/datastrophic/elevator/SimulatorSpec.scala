package io.datastrophic.elevator

import org.scalatest.{Matchers, FunSpec}

import scala.collection.immutable.TreeSet

class SimulatorSpec extends FunSpec with Matchers{
   //todo: behaviour change for every case
 val simulator = new Simulator {}
 describe("Simulator") {
  it("should properly update the elevator status and queues when moving up") {
   val e = Elevator(id = 1, mode = 1, currentFloor = 0, upQueue = TreeSet(1, 2, 3))

   var updated = simulator.step(e)
   updated.upQueue should equal(TreeSet(2,3))
   updated.currentFloor should equal(1)

   updated = simulator.step(updated)
   updated.upQueue should equal(TreeSet(3))
   updated.currentFloor should equal(2)

   updated = simulator.step(updated)
   updated.upQueue should equal(TreeSet())
   updated.currentFloor should equal(3)
   updated.mode should equal(0)
  }

  it("should properly update the elevator status and queues when moving down") {
   val e = Elevator(id = 1, mode = -1, currentFloor = 4, downQueue = TreeSet(1, 2, 3))

   var updated = simulator.step(e)
   updated.downQueue should equal(TreeSet(1,2))
   updated.currentFloor should equal(3)

   updated = simulator.step(updated)
   updated.downQueue should equal(TreeSet(1))
   updated.currentFloor should equal(2)

   updated = simulator.step(updated)
   updated.downQueue should equal(TreeSet())
   updated.currentFloor should equal(1)
   updated.mode should equal(0)
  }

  it("should properly update the elevator status when direction is changed from up to down") {
   val e = Elevator(id = 1, mode = 1, currentFloor = 1, upQueue = TreeSet(2), downQueue = TreeSet(1))

   var updated = simulator.step(e)
   updated.upQueue should equal(TreeSet())
   updated.currentFloor should equal(2)
   updated.mode should equal(-1)

   updated = simulator.step(updated)
   updated.downQueue should equal(TreeSet())
   updated.currentFloor should equal(1)
   updated.mode should equal(0)
  }

  it("should properly update the elevator status when direction is changed from down to up") {
   val e = Elevator(id = 1, mode = -1, currentFloor = 2, upQueue = TreeSet(2), downQueue = TreeSet(1))

   var updated = simulator.step(e)
   updated.downQueue should equal(TreeSet())
   updated.currentFloor should equal(1)
   updated.mode should equal(1)

   updated = simulator.step(updated)
   updated.upQueue should equal(TreeSet())
   updated.currentFloor should equal(2)
   updated.mode should equal(0)
  }
 }
}
