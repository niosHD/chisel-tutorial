package TutorialSolutions

import Chisel._
import Chisel.testers.{UnitTester, UnitTestRunners}

object TutorialSolutions extends UnitTestRunners {
  val list_of_tests = Array(
  // TODO: uncomment this first Accumulator, the one at the bottom just helps with commas during work here
    "Accumulator"            -> (() => { new AccumulatorTests }),
    "LFSR16"                 -> (() => { new LFSR16Tests }),
    "SingleEvenFilter"       -> (() => { new SingleEvenFilterTests(16)) }),
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
    "VendingMachineSwitch"   -> (() => { new VendingMachineSwitchTests )},
  ).toMap

  def main(args: Array[String]): Unit = {
    val to_call = if( args.length > 0) args else list_of_tests.keys.toArray

    for( arg <- args ) {
      if (list_of_tests.contains(arg)) {
        execute( list_of_tests(arg)() )
      }
    }
  }
}

