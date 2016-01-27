package solutions

import Chisel._
import Chisel.testers.SteppedHWIOTester

class VendingMachineSwitch extends Module {
  val io = new Bundle {
    val nickel = Bool(dir = INPUT)
    val dime   = Bool(dir = INPUT)
    val valid  = Bool(dir = OUTPUT)
  }
  val s_idle :: s_5 :: s_10 :: s_15 :: s_ok :: Nil = Enum(UInt(), 5)
  val state = Reg(init = s_idle)
  
  switch (state) {
    is (s_idle) {
      when (io.nickel) { state := s_5 }
      when (io.dime) { state := s_10 }
    }
    is (s_5) {
      when (io.nickel) { state := s_10 }
      when (io.dime) { state := s_15 }
    }
    is (s_10) {
      when (io.nickel) { state := s_15 }
      when (io.dime) { state := s_ok }
    }
    is (s_15) {
      when (io.nickel) { state := s_ok }
      when (io.dime) { state := s_ok }
    }
    is (s_ok) {
      state := s_idle
    }
  }
  io.valid := (state ===s_ok)
}

class VendingMachineSwitchTests extends SteppedHWIOTester {
  val c = Module(new VendingMachineSwitch)
  var money = 0
  var isValid = 0
  for (t <- 0 until 20) {
    val coin     = rnd.nextInt(3)*5
    val isNickel = if(coin == 5) 1 else 0
    val isDime   = if(coin == 10) 1 else 0

    // Advance circuit
    poke(c.io.nickel, isNickel)
    poke(c.io.dime,   isDime)
    step(1)

    // Advance model
    money = if (isValid == 1) 0 else (money + coin)
    isValid = if(money >= 20) 1 else 0

    // Compare
    expect(c.io.valid, isValid)
  }
  install(c)
}
