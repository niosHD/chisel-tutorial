package examples

import Chisel._
import Chisel.testers._

object MemorySearchTestData {
  val element_list = Array(0, 4, 15,14, 2, 5, 13)

}
class MemorySearch extends Module {
  val io = new Bundle {
    val target  = UInt(INPUT,  4)
    val en      = Bool(INPUT)
    val done    = Bool(OUTPUT)
    val address = UInt(OUTPUT, 3)
  }
  val index = Reg(init = UInt(0, width = 3))
//  val elts  = Vec(UInt(0), UInt(4), UInt(15), UInt(14),
//                  UInt(2), UInt(5), UInt(13))
  val elts  = Vec(MemorySearchTestData.element_list.map(UInt(_)))
  val elt   = elts(index)
  val over  = !io.en && ((elt === io.target) || (index === UInt(7)))
  when (io.en) {
    index := UInt(0)
  } .elsewhen (!over) {
    index := index + UInt(1)
  }
  io.done    := over
  io.address := index
}

class MemorySearchUnitTester extends SteppedHWIOTester {
  val device_under_test = Module(new MemorySearch)

  val c = device_under_test

  testBlock {
    val list = MemorySearchTestData.element_list
    val n = 20
    val maxT = n * (list.length + 3)
    for (k <- 0 until n) {
      val target = rnd.nextInt(16)
      poke(c.io.en, 1)
      poke(c.io.target, target)
      step(1)
      poke(c.io.en, 0)
      //    when(c.io.done) {
      //      printf("Looking for 0x%x, found 0x%x\n", UInt(target), c.io.address)
      //      when(c.io.address < UInt(list.length)) {
      //        expect(c.elts(c.io.address), target)
      //      }
      //    }
      val expectedIndex = if (list.contains(target)) {
        list.indexOf(target)
      } else {
        list.length
      }
      step(expectedIndex)
      expect(c.io.done, 1)
      expect(c.io.address, expectedIndex)
      step(1)
    }
  }
}
