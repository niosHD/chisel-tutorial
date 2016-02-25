package examples

import Chisel._
import Chisel.hwiotesters._

object VecSearchTest {
  val pattern = Array(0, 4, 15, 14, 2, 5, 13)
}

class VecSearch extends Module {
  val io = new Bundle {
    val out = UInt(OUTPUT,  4)
  }
  val index = Reg(init = UInt(0, width = 4))
  val elts  = Wire(init = Vec(VecSearchTest.pattern.map(UInt(_, 4))))
  index := index + UInt(1)
  io.out := elts(index)
}

class VecSearchUnitTester extends SteppedHWIOTester {
  val device_under_test = Module(new VecSearch)
  val c = device_under_test

  val list = VecSearchTest.pattern
  for (elt <- list) {
    expect(c.io.out, elt)
    step(1)
  }
}
