package solutions

import Chisel._
import Chisel.testers.UnitTester

class RealGCDInput extends Bundle {
  val a = Bits(width = 16)
  val b = Bits(width = 16)
}

class RealGCD extends Module {
  val io  = new Bundle {
    val in  = Decoupled(new RealGCDInput()).flip()
    val out = Valid(Bits(width = 16))
  }

  val x = Reg(UInt())
  val y = Reg(UInt())
  val p = Reg(init=Bool(false))

  io.in.ready := !p

  when (io.in.valid && !p) {
    x := io.in.bits.a
    y := io.in.bits.b
    p := Bool(true)
  } 

  when (p) {
    when (x > y)  { x := y; y := x } 
    .otherwise    { y := y - x }
  }

  io.out.bits  := x
  io.out.valid := y === Bits(0) && p
  when (io.out.valid) {
    p := Bool(false)
  }
}

class RealGCDTests extends UnitTester {
  def compute_gcd(a: Int, b: Int): Tuple2[Int, Int] = {
    var x = a
    var y = b
    var depth = 0
    do {
      if (x > y) {
        val t = x; x = y; y = t
      }
      else {
        y -= x
      }
      depth += 1
    } while(y > 0 )
    return (x, depth)
  }

  val c = Module(new RealGCD)
  val inputs = List( (48, 32), (7, 3), (100, 10) )

  for ( (a, b) <- inputs) {
      poke(c.io.in.bits.a, a)
      poke(c.io.in.bits.b, b)
      poke(c.io.in.valid,  1)
      step(1)
      poke(c.io.in.valid,  0)

      val (expected_gcd, steps) = compute_gcd(a, b)
      step(steps) // -1 is because we step(1) already to toggle the enable
      expect(c.io.out.bits, expected_gcd)
      expect(c.io.out.valid, 1 )
      step(1)

  }
  install(c)
}
