package hello

import Chisel._
import Chisel.testers._

class Hello extends Module {
  val io = new Bundle { 
    val out = UInt(OUTPUT, 8)
  }
  io.out := UInt(42)
}

class HelloTests extends UnitTester {
  val c = Module(new Hello)
  step(1)
  expect(c.io.out, 42)
  install(c)
}

object Hello {
  def main(mainArgs: Array[String]): Unit = {
    implicit val args = mainArgs.slice(1, mainArgs.length)
    val tester = new UnitTester
    tester.execute { new HelloTests }
  }
}
