package hello

import Chisel._
import Chisel.testers._
import Chisel.hwiotesters._

class Hello extends Module {
  val io = new Bundle { 
    val out = UInt(OUTPUT, 8)
  }
  io.out := UInt(42)
}

class HelloTests extends SteppedHWIOTester {
  val device_under_test = Module(new Hello)
  val c = device_under_test
  step(1)
  expect(c.io.out, 42)
}

object Hello {
  def main(mainArgs: Array[String]): Unit = {
    implicit val args = mainArgs.slice(1, mainArgs.length)
    TesterDriver.execute { () => new HelloTests }
  }
}
