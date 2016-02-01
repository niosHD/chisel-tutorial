package solutions

import Chisel._
import Chisel.testers._

class VecShiftRegisterParam(val n: Int, val w: Int) extends Module {
  val io = new Bundle {
    val in  = UInt(INPUT,  w)
    val out = UInt(OUTPUT, w)
  }
  // Note the proper way to declare and initialize a Ref of Vec of UInt
  val delays = Reg(init = Vec.fill(n){UInt(0, width = w)})
  for (i <- n-1 to 1 by -1)
    delays(i) := delays(i-1) 
  delays(0) := io.in
  io.out := delays(n-1)
}

class VecShiftRegisterParamTests(val n: Int, val w: Int) extends SteppedHWIOTester {
  val device_under_test = Module(new VecShiftRegisterParam(n, w))
  val c = device_under_test

  val reg = Array.fill(c.n) {
    0
  }
  for (t <- 0 until 16) {
    val in = rnd.nextInt(1 << c.w)
    poke(c.io.in, in)
    step(1)
    for (i <- c.n - 1 to 1 by -1)
      reg(i) = reg(i - 1)
    reg(0) = in
    expect(c.io.out, reg(c.n - 1))
  }
}
