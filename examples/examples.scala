package TutorialExamples

import Chisel._
import Chisel.testers._
import scala.collection.mutable.ArrayBuffer
import scala.collection.immutable.HashMap

object TutorialExamples {
  val allTests = Array(
     "GCD",
     "Combinational",
     "Functionality",
     "Parity",
     "Tbl",
     "Life",
     "Risc",
     "Router",
     "Darken",
     "Adder",
     "Adder4",
     "SimpleALU",
     "FullAdder",
     "FullAdder2",
     "ByteSelector",
     "HiLoMultiplier",
     "ShiftRegister",
     "ResetShiftRegister",
     "EnableShiftRegister",
     "LogShifter",
     "VecSearch",
     "MemorySearch",
     "Stack",
     "FIR"
  )

  def filterArgs(args: Array[String], amap: HashMap[String, String]): Array[String] = {
    val newArgs = ArrayBuffer[String]()
    for (arg <- args) {
      if (amap.contains(arg)) {
        newArgs += amap(arg)
      } else {
        newArgs += arg
      }
    }
    newArgs.toArray
  }

  def runTests(tutTests: Array[String], tutArgs: Array[String]) {
    implicit val args = tutArgs
    val tester = new UnitTester
    for (test <- tutTests) {
      test match {
        case "GCD" => tester.execute { new GCDUnitTester }
        case "Combinational" => tester.execute { new CombinationalUnitTester }
        case "Functionality" => tester.execute { new FunctionalityUnitTester }
        case "Parity" => tester.execute { new ParityUnitTester }
        case "Tbl" => tester.execute { new TblUnitTester }
        case "Life" => tester.execute { new LifeUnitTester }
        case "Risc" => tester.execute { new RiscUnitTester }
        case "Router" => tester.execute { new RouterUnitTester }
        case "Darken" => println("no Darken")
        case "Adder" => tester.execute { new AdderUnitTester(8) }
        case "Adder4" => tester.execute { new Adder4UnitTester }
        case "SimpleALU" => tester.execute { new SimpleALUUnitTester }
        case "FullAdder" => tester.execute { new FullAdderUnitTester }
        case "FullAdder2" => tester.execute { new FullAdder2UnitTester }
        case "ByteSelector" => tester.execute { new ByteSelectorUnitTester }
        case "HiLoMultiplier" => tester.execute { new HiLoMultiplierUnitTester }
        case "ShiftRegister" => tester.execute { new ShiftRegisterUnitTester }
        case "ResetShiftRegister" => tester.execute { new ResetShiftRegisterUnitTester }
        case "EnableShiftRegister" => tester.execute { new EnableShiftRegisterUnitTester }
        case "LogShifter" => tester.execute { new LogShifterUnitTester }
        case "VecSearch" => tester.execute { new VecSearchUnitTester }
        case "MemorySearch" => tester.execute { new MemorySearchUnitTester }
        case "Stack" => tester.execute { new StackUnitTester(8) }
        case "FIR" => tester.execute { new FIRUnitTester }
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    val testNames = if (args.length == 0 || args(0) == "all") {
      allTests
    } else {
      Array(args(0))
    }
    runTests(testNames, tutArgs)
  }
}

