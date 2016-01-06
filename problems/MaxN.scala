package problems

import Chisel._ 
import Chisel.testers._

class MaxN(val n: Int, val w: Int) extends Module {

  private def Max2(x: UInt, y: UInt) = Mux(x > y, x, y)

  val io = new Bundle {
    val ins = Vec(n, UInt(INPUT, w))
    val out = UInt(OUTPUT, w)
  }
  io.out := io.ins.reduceLeft(Max2)
}

class MaxNTests(val n: Int, val w: Int) extends UnitTester {
  val c = Module(new MaxN(n, w))
  val ins = Array.fill(c.n){ 0 }
  for (i <- 0 until 10) {
    var mx = 0
    for (i <- 0 until c.n) {
      // FILL THIS IN HERE
      poke(c.io.ins(0), 0)
    }
    // FILL THIS IN HERE
    expect(c.io.out, 1)
    step(1)
  }
  install(c)
}
