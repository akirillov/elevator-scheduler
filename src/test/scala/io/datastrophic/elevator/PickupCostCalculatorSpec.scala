package io.datastrophic.elevator

import org.scalatest.{Matchers, FunSpec}
import scala.collection.immutable.TreeSet

class PickupCostCalculatorSpec extends FunSpec with Matchers{
   val costCalculator = new PickupCostCalculator{}

   describe("Pickup Cost Calculator") {
      it("should handle case: pickup on the elevator way, passenger goes up, elevator goes up") {
         val e = Elevator(id = 1, mode = 1, currentFloor = 5, upQueue = TreeSet(7,9), downQueue = TreeSet(6,8))
         costCalculator.calculatePickupCost(e, 5, 1) should equal(4)
         costCalculator.calculatePickupCost(e, 8, 1) should equal((8-5) + 4)
         costCalculator.calculatePickupCost(e, 10, 1) should equal((10-5) + 4)
      }
      it("should handle case: pickup on the elevator way, passenger goes down, elevator goes up") {
         val e = Elevator(id = 1, mode = 1, currentFloor = 5, upQueue = TreeSet(7,9), downQueue = TreeSet(6,8))
         costCalculator.calculatePickupCost(e, 5, -1) should equal((9-5)*2 + 4)
         costCalculator.calculatePickupCost(e, 8, -1) should equal((9-5) + (9-8) + 4)
         costCalculator.calculatePickupCost(e, 10, -1) should equal((10-5) + 4)
      }
      it("should handle case: elevator passed pickup, passenger goes up, elevator goes down") {
         val e = Elevator(id = 1, mode = -1, currentFloor = 5, upQueue = TreeSet(7,9), downQueue = TreeSet(2, 3))
         costCalculator.calculatePickupCost(e, 5, 1) should equal((5-2)*2 + 4)
         costCalculator.calculatePickupCost(e, 8, 1) should equal((5-2) + (8-2) + 4)
         costCalculator.calculatePickupCost(e, 10, 1) should equal((5-2) + (10-2) + 4)
      }
      it("should handle case: elevator passed pickup, passenger goes down, elevator goes down") {
         val e = Elevator(id = 1, mode = -1, currentFloor = 10, upQueue = TreeSet(7,13), downQueue = TreeSet(2,3))
         costCalculator.calculatePickupCost(e, 10, -1) should equal(4)
         costCalculator.calculatePickupCost(e, 12, -1) should equal((10-2) + (13-2) + (13-12) + 4)
         costCalculator.calculatePickupCost(e, 15, -1) should equal((10-2) + (15-2) + 4)
      }
      it("should handle case: elevator passed pickup, passenger goes up, elevator goes up") {
         val e = Elevator(id = 1, mode = 1, currentFloor = 10, upQueue = TreeSet(7,13), downQueue = TreeSet(2,3))
         costCalculator.calculatePickupCost(e, 9, 1) should equal((13-10) + (13-2) + (9-2) + 4)
         costCalculator.calculatePickupCost(e, 1, 1) should equal((13-10) + (13-2) + (2-1) + 4)
      }
      it("should handle case: elevator passed the pickup floor, passenger goes down, elevator goes up") {
         val e = Elevator(id = 1, mode = 1, currentFloor = 10, upQueue = TreeSet(7,13), downQueue = TreeSet(2,3))
         costCalculator.calculatePickupCost(e, 9, -1) should equal((13-10) + (13-9) + 4)
         costCalculator.calculatePickupCost(e, 1, -1) should equal((13-10) + (13-1) + 4)
      }
      it("should handle case: pickup on the elevator way, passenger goes up, elevator goes down") {
         val e = Elevator(id = 1, mode = -1, currentFloor = 10, upQueue = TreeSet(7,13), downQueue = TreeSet(2,3))
         costCalculator.calculatePickupCost(e, 9, 1) should equal((10-2) + (9-2) + 4)
         costCalculator.calculatePickupCost(e, 1, 1) should equal((10-1) + 4)
      }
      it("should handle case: pickup on the elevator way, passenger goes down, elevator goes down") {
         val e = Elevator(id = 1, mode = -1, currentFloor = 10, upQueue = TreeSet(7,13), downQueue = TreeSet(2,3))
         costCalculator.calculatePickupCost(e, 9, -1) should equal((10-9) + 4)
         costCalculator.calculatePickupCost(e, 1, -1) should equal((10-1) + 4)
      }
      it("should properly calculate cost when elevator idles") {
         val e = Elevator(id = 1, mode = 0, currentFloor = 10, upQueue = TreeSet(7,13), downQueue = TreeSet(2,3))
         costCalculator.calculatePickupCost(e, 1, -1) should equal(10-1)
         costCalculator.calculatePickupCost(e, 15, 1) should equal(15-10)
      }
   }
}