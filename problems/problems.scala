package TutorialProblems

import Chisel._
import Chisel.testers._

object problems {
  val allTests = Array(
    "Accumulator",
    "LFSR16",
    "SingleEvenFilter",
    "VecShiftRegister",
    "VecShiftRegisterSimple",
    "VecShiftRegisterParam",
    "Max2",
    "MaxN",
    "Adder",
    "DynamicMemorySearch",
    "RealGCD" ,
    "Mux2" ,
    "Mux4",
    "Memo" ,
    "Mul" ,
    "Counter",
    "VendingMachine"
  )

  def runTests(tutTests: Array[String], tutArgs: Array[String]) {
    implicit val args = tutArgs
    val tester = new UnitTester
    for (test <- tutTests) {
      test match {
        case "Accumulator" => tester.execute { new AccumulatorTests }
        case "LFSR16" => tester.execute { new LFSR16Tests }
        case "SingleEvenFilter" => tester.execute { new SingleEvenFilterTests(16) }
        case "VecShiftRegister" => tester.execute { new VecShiftRegisterTests }
        case "VecShiftRegisterSimple" => tester.execute { new VecShiftRegisterSimpleTests }
        case "VecShiftRegisterParam" => tester.execute { new VecShiftRegisterParamTests(8, 4) }
        case "Max2" => tester.execute { new Max2Tests }
        case "MaxN" => tester.execute { new MaxNTests(8, 16) }
        case "Adder" => tester.execute { new AdderTests(8) }
        case "DynamicMemorySearch" => tester.execute { new DynamicMemorySearchTests(8, 4) }
        case "RealGCD" =>  tester.execute { new RealGCDTests }
        case "Mux2" =>  tester.execute { new Mux2Tests }
        case "Mux4" => tester.execute { new Mux4Tests }
        case "Memo" =>  tester.execute { new MemoTests }
        case "Mul" =>  tester.execute { new MulTests }
        case "Counter" => tester.execute { new CounterTest }
        case "VendingMachine" => tester.execute { new VendingMachineTests }
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

