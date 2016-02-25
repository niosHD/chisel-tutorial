package problems

import Chisel._
import Chisel.hwiotesters._

class Adder(val w: Int) extends Module {
  // fill in here
  val io = new Bundle {
    val in0 = UInt(INPUT,  1)
    val in1 = UInt(INPUT,  1)
    val out = UInt(OUTPUT, 1)
  }
  io.out := UInt(0)
}

class AdderTests(w: Int) extends SteppedHWIOTester {
  val device_under_test = Module(new Adder(w))
  val c = device_under_test
  for (i <- 0 until 10) {
    val in0 = rnd.nextInt(1 << c.w)
    val in1 = rnd.nextInt(1 << c.w)
    poke(c.io.in0, in0)
    poke(c.io.in1, in1)
    expect(c.io.out, (in0 + in1) & ((1 << c.w) - 1))
    step(1)
  }

}
