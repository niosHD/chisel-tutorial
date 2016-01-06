package examples

import Chisel.testers.UnitTestRunners

object Launcher extends UnitTestRunners{
  val list_of_tests = Array(
    "Adder"               -> (() => { new AdderUnitTester(8) }),
    "Adder4"              -> (() => { new Adder4UnitTester }),
    "ByteSelector"        -> (() => { new ByteSelectorUnitTester }),
    "Combinational"       -> (() => { new CombinationalUnitTester }),
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
    ).toMap

  def main(args: Array[String]): Unit = {
    val to_call = if( args.length > 0) args else list_of_tests.keys.toArray

    for( arg <- to_call ) {
      if (list_of_tests.contains(arg)) {
        execute( list_of_tests(arg)() )
      }
      else {
        println(s"Error: $arg not found in list of tests")
        println("option\n"+ list_of_tests.keys.toList.sorted.mkString(", "))
      }
    }
  }
}

