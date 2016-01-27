package problems

import Chisel._
import Chisel.testers._
import scala.collection.mutable.ArrayBuffer

class Mul extends Module {
  val io = new Bundle {
    val x   = UInt(INPUT,  4)
    val y   = UInt(INPUT,  4)
    val z   = UInt(OUTPUT, 8)
  }
  val muls = new ArrayBuffer[UInt]()

  // -------------------------------- \\
  // Calculate io.z = io.x * io.y by
  // building filling out muls
  // -------------------------------- \\


  // -------------------------------- \\
}

class MulTests extends SteppedHWIOTester {
  val c = Module(new Mul)
  val maxInt  = 1 << 4
  for (i <- 0 until 10) {
    val x = rnd.nextInt(maxInt)
    val y = rnd.nextInt(maxInt)
    poke(c.io.x, x)
    poke(c.io.y, y)
    expect(c.io.z, (x * y))
    step(1)
  }
  install(c)
}
