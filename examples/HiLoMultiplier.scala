package examples

import Chisel._
import Chisel.testers._

//A 4-bit adder with carry in and carry out
class HiLoMultiplier() extends Module {
  val io = new Bundle {
    val A  = UInt(INPUT, 16)
    val B  = UInt(INPUT, 16)
    val Hi = UInt(OUTPUT, 16)
    val Lo = UInt(OUTPUT, 16)
  }
  val mult = io.A * io.B
  io.Lo := mult(15, 0)
  io.Hi := mult(31, 16)
}

class HiLoMultiplierUnitTester extends SteppedHWIOTester {
  val device_under_test = Module(new HiLoMultiplier)

  val c = device_under_test

  testBlock {
    for (t <- 0 until 4) {
      val rnd0: BigInt = rnd.nextInt(65535)
      val rnd1: BigInt = rnd.nextInt(65535)
      val ref_out = UInt(rnd0 * rnd1, width = 32)
      poke(c.io.A, rnd0.toInt)
      poke(c.io.B, rnd1.toInt)
      expect(c.io.Lo, ref_out(15, 0).litValue().toInt)
      expect(c.io.Hi, ref_out(31, 16).litValue().toInt)
      step(1)
    }
  }
}
