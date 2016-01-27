package examples

import Chisel._
import Chisel.testers._

class Tbl extends Module {
  val io = new Bundle {
    val addr = UInt(INPUT,  8)
    val out  = UInt(OUTPUT, 8)
  }
  val r = Wire(init = Vec(Range(0, 256).map(UInt(_, width = 8))))
  io.out := r(io.addr)
}

class TblUnitTester extends SteppedHWIOTester {
  val device_under_test = Module(new Tbl)
  val c = device_under_test

  testBlock {
    for (t <- 0 until 16) {
      val addr = rnd.nextInt(256)
      poke(c.io.addr, addr)
      expect(c.io.out, addr)
      step(1)
    }
  }
}

