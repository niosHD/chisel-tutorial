package examples

import Chisel.testers.UnitTestRunners

import scala.collection.mutable.ArrayBuffer

object Launcher extends UnitTestRunners{
  val list_of_tests = Map(
    "Adder"               -> (() => { new AdderUnitTester(8) }),
    "Adder4"              -> (() => { new Adder4UnitTester }),
    "ByteSelector"        -> (() => { new ByteSelectorUnitTester }),
    "Combinational"       -> (() => { new CombinationalUnitTester }),
    "DecoupledRouter"     -> (() => { new RouterDecoupledTester }),
    "EnableShiftRegister" -> (() => { new EnableShiftRegisterUnitTester }),
    "FullAdder"           -> (() => { new FullAdderUnitTester }),
    "FullAdder2"          -> (() => { new FullAdder2UnitTester }),
    "Functionality"       -> (() => { new FunctionalityUnitTester }),
    "GCD"                 -> (() => { new GCDUnitTester }),
    "HiLoMultiplier"      -> (() => { new HiLoMultiplierUnitTester }),
    "Life"                -> (() => { new LifeUnitTester }),
    "LogShifter"          -> (() => { new LogShifterUnitTester }),
    "MemorySearch"        -> (() => { new MemorySearchUnitTester }),
    "Parity"              -> (() => { new ParityUnitTester }),
    "ResetShiftRegister"  -> (() => { new ResetShiftRegisterUnitTester }),
    "Risc"                -> (() => { new RiscUnitTester }),
    "Router"              -> (() => { new RouterUnitTester }),
    "ShiftRegister"       -> (() => { new ShiftRegisterUnitTester }),
    "SimpleALU"           -> (() => { new SimpleALUUnitTester }),
    "Stack"               -> (() => { new StackUnitTester(8) }),
    "Tbl"                 -> (() => { new TblUnitTester }),
    "VecSearch"           -> (() => { new VecSearchUnitTester })
    //    "FIR"                 -> (() => { new FIRUnitTester }),
    //    "Darken"              -> (() => no Darken")
    )

  def main(args: Array[String]): Unit = {
    // Support Chisel2 arguments
    val optionIndex = args.indexWhere { x => x.startsWith("--") }
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

