package TutorialSolutions

import chisel._
import chisel.util._
import chisel.iotesters._

abstract class Filter[T <: Data](dtype: T) extends Module {
  val io = new Bundle {
    val in  = Valid(dtype).asInput
    val out = Valid(dtype).asOutput
} }

class PredicateFilter[T <: Data](dtype: T, f: T => Bool) extends Filter(dtype) {
  io.out.valid := io.in.valid && f(io.in.bits)
  io.out.bits  := io.in.bits
}

object SingleFilter {
  def apply[T <: UInt](dtype: T) = 
    Module(new PredicateFilter(dtype, (x: T) => x <= UInt(9)))
}

object EvenFilter {
  def apply[T <: UInt](dtype: T) = 
    Module(new PredicateFilter(dtype, (x: T) => x(0).toBool))
}

class SingleEvenFilter[T <: UInt](dtype: T) extends Filter(dtype) {
  val single = SingleFilter(dtype)
  val even   = EvenFilter(dtype)
  single.io.in  := io.in
  even.io.in    := single.io.out
  io.out        := even.io.out
}

class SingleEvenFilterTests[T <: UInt](c: SingleEvenFilter[T], b: Option[Backend] = None) extends PeekPokeTester(c, _backend=b) {
  val maxInt  = 1 << 16
  for (i <- 0 until 10) {
    val in = rnd.nextInt(maxInt)
    poke(c.io.in.valid, 1)
    poke(c.io.in.bits, in)
    val isSingleEven = (in <= 9) && (in%2 == 1)
    step(1)
    expect(c.io.out.valid, if (isSingleEven) 1 else 0)
    expect(c.io.out.bits, in)
  }
}
