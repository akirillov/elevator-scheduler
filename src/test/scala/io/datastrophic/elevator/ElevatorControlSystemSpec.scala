package io.datastrophic.elevator

import org.scalatest.{Matchers, FunSpec}

import scala.collection.immutable.TreeSet
import scala.collection.mutable

class ElevatorControlSystemSpec extends FunSpec with Matchers{

 describe("Elevator Control System") {
  it("should evenly balance the load across the elevators") {
   val els = mutable.Map(1 -> Elevator(1), 2 -> Elevator(2), 3 -> Elevator(3))
   val ecs = new WaitTimeOptimizedControlSystem(els)

   ecs.pickup(2, 1)
   ecs.pickup(3, 1)
   ecs.pickup(4, 1)

   val even = ecs.status().forall(e => e.upQueue.size == 1)
   even should be(true)

  }
  it("should respect already scheduled pickups") {
   val els = mutable.Map(1 -> Elevator(1), 2 -> Elevator(2), 3 -> Elevator(3))
   val ecs = new WaitTimeOptimizedControlSystem(els)

   ecs.pickup(3, 1)
   ecs.pickup(3, 1)
   ecs.pickup(3, 1)

   val res = ecs.status().filter(e => e.upQueue.size == 1)
   res.size should equal(1)
  }
  it("should pick up the passenger from top floor and bring it to the bottom one"){
   val els = mutable.Map(1 -> Elevator(1))
   val ecs = new WaitTimeOptimizedControlSystem(els)

   def el = ecs.status().head

   ecs.pickup(5, -1)
   el.downQueue should equal(TreeSet(5))
   (el.mode > 0) should be(true)

   (1 to 5) foreach {_ => ecs.step()}
   ecs.update(1, 1)
   el.downQueue should equal(TreeSet(1))

   (1 to 5) foreach {_ => ecs.step()}
   el.upQueue.isEmpty should be(true)
   el.downQueue.isEmpty should be(true)
   el.currentFloor should equal(1)
   el.mode should equal(0)
  }
  it("should pick up the passenger from the bottom floor and bring him to the top one"){
   val els = mutable.Map(1 -> Elevator(id = 1, currentFloor = 5))
   val ecs = new WaitTimeOptimizedControlSystem(els)

   def el = ecs.status().head

   ecs.pickup(1, 1)
   el.upQueue should equal(TreeSet(1))
   (el.mode < 0) should be(true)

   (1 to 5) foreach {_ => ecs.step()}
   ecs.update(1, 7)
   el.upQueue should equal(TreeSet(7))

   (1 to 7) foreach {_ => ecs.step()}
   el.upQueue.isEmpty should be(true)
   el.downQueue.isEmpty should be(true)
   el.currentFloor should equal(7)
   el.mode should equal(0)
  }
 }

}
