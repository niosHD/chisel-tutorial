package solutions

import Chisel._
import Chisel.hwiotesters.SteppedHWIOTester

class MaxN(val n: Int, val w: Int) extends Module {

  private def Max2(x: UInt, y: UInt) = Mux(x > y, x, y)

  val io = new Bundle {
    val ins = Vec(n, UInt(INPUT, w))
    val out = UInt(OUTPUT, w)
  }
  io.out := io.ins.reduceLeft(Max2)
}

class MaxNTests(val n: Int, val w: Int) extends SteppedHWIOTester {
  val device_under_test = Module(new MaxN(n, w))
  val c = device_under_test
  enable_all_debug = true

  val ins = Array.fill(c.n) {
    0
  }
  for (i <- 0 until 10) {
    var mx = 0
    for (i <- 0 until c.n) {
      ins(i) = rnd.nextInt(1 << c.w)
      poke(c.io.ins(i), ins(i))
      mx = if (ins(i) > mx) ins(i) else mx;
    }
    expect(c.io.out, mx)
    step(1)
  }
}
