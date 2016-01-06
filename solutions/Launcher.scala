package solutions

import Chisel.testers.UnitTestRunners

import scala.collection.mutable.ArrayBuffer

object Launcher extends UnitTestRunners {
  val list_of_tests = Map(
    "Accumulator"            -> (() => { new AccumulatorTests }),
    "LFSR16"                 -> (() => { new LFSR16Tests }),
    "SingleEvenFilter"       -> (() => { new SingleEvenFilterTests(16) }),
    "VecShiftRegister"       -> (() => { new VecShiftRegisterTests }),
    "VecShiftRegisterSimple" -> (() => { new VecShiftRegisterSimpleTests }),
    "VecShiftRegisterParam"  -> (() => { new VecShiftRegisterParamTests(8, 4) }),
    "Max2"                   -> (() => { new Max2Tests }),
    "MaxN"                   -> (() => { new MaxNTests(8, 16) }),
    "Adder"                  -> (() => { new AdderTests(8) }),
    "DynamicMemorySearch"    -> (() => { new DynamicMemorySearchTests(8, 4) }),
    "RealGCD"                -> (() => { new RealGCDTests }),
    "Mux2"                   -> (() => { new Mux2Tests }),
    "Mux4"                   -> (() => { new Mux4Tests }),
    "Memo"                   -> (() => { new MemoTests }),
    "Mul"                    -> (() => { new MulTests }),
    "Counter"                -> (() => { new CounterTest }),
    "VendingMachine"         -> (() => { new VendingMachineTests }),
    "VendingMachineSwitch"   -> (() => { new VendingMachineSwitchTests })
  )

  def main(args: Array[String]): Unit = {
    // Support Chisel2 arguments
    val optionIndex = args.indexWhere(_.startsWith("--"))
    implicit val optionArgs = if (optionIndex != -1) {
      args.slice(optionIndex, args.length)
    } else {
      Array[String]()
    }
    val nonOptionArgs = if (optionIndex == -1) {
      args
    } else {
      args.slice(0, optionIndex)
    }
    val to_call = if( nonOptionArgs.length > 0 ) {
      nonOptionArgs
    } else {
      list_of_tests.keys.toArray
    }

    val failed_tests = new ArrayBuffer[String]()
    for( arg <- to_call ) {
      if (list_of_tests.contains(arg)) {
        if(!execute( list_of_tests(arg)() )) {
          failed_tests += arg
        }
      }
      else {
        println(s"Error: $arg not found in list of tests")
        println("option\n"+ list_of_tests.keys.toList.sorted.mkString(", "))
      }
    }
    if(failed_tests.nonEmpty) {
      println("Following tests failed in some way\n" + failed_tests.sorted.mkString("\n"))
    }
  }
}

