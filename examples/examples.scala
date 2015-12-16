package TutorialExamples

import Chisel.testers.UnitTestRunners

object TutorialExamples extends UnitTestRunners{
  val list_of_tests = Array(
    "GCD" -> (() => { new GCDUnitTester }),
    "Combinational" -> (() => { new CombinationalUnitTester }),
    "Functionality" -> (() => { new FunctionalityUnitTester }),
    "Parity" -> (() => { new ParityUnitTester }),
    "Tbl" -> (() => { new TblUnitTester }),
    "Life" -> (() => { new LifeUnitTester }),
    "Risc" -> (() => { new RiscUnitTester }),
    "Router" -> (() => { new RouterUnitTester }),
    //      "Darken" -> (() => no Darken")
    "Adder" -> (() => { new AdderUnitTester(8) }),
    "Adder4" -> (() => { new Adder4UnitTester }),
    "SimpleALU" -> (() => { new SimpleALUUnitTester }),
    "FullAdder" -> (() => { new FullAdderUnitTester }),
    "FullAdder2" -> (() => { new FullAdder2UnitTester }),
    "ByteSelector" -> (() => { new ByteSelectorUnitTester }),
    "HiLoMultiplier" -> (() => { new HiLoMultiplierUnitTester }),
    "ShiftRegister" -> (() => { new ShiftRegisterUnitTester }),
    "ResetShiftRegister" -> (() => { new ResetShiftRegisterUnitTester }),
    "EnableShiftRegister" -> (() => { new EnableShiftRegisterUnitTester }),
    "LogShifter" -> (() => { new LogShifterUnitTester }),
    "VecSearch" -> (() => { new VecSearchUnitTester }),
    "MemorySearch" -> (() => { new MemorySearchUnitTester }),
    "Stack" -> (() => { new StackUnitTester(8) }),
    "FIR" -> (() => { new FIRUnitTester })
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

