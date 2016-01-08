package examples

import Chisel._
import Chisel.testers._

class DecoupledReadCmd extends Bundle {
  val addr = UInt(width = 32)
}

class DecoupledWriteCmd extends DecoupledReadCmd {
  val data = UInt(width = 32)
}

class DecoupledPacket extends Bundle {
  val header = UInt(width = 8)
  val body   = Bits(width = 64)
}

/**
  * This router circuit
  * It routes a packet placed on it's input to one of n output ports
  *
  * @param n is number of fanned outputs for the routed packet
  */
class DecoupledRouterIO(n: Int) extends Bundle {
  override def cloneType           = new DecoupledRouterIO(n).asInstanceOf[this.type]
  val read_routing_table_request   = new DecoupledIO(new DecoupledReadCmd(), do_flip = true)
  val read_routing_table_response  = new DecoupledIO(UInt(width = 8))
  val load_routing_table_request   = new DecoupledIO(new DecoupledWriteCmd(), do_flip = true)
  val in                           = new DecoupledIO(new DecoupledPacket(), do_flip = true)
  val outs                         = Vec(n, new DecoupledIO(new DecoupledPacket()))
}

class DecoupledRouter extends Module {
  val depth = 32
  val n     = 4
  val io    = new DecoupledRouterIO(n)
  val tbl   = Mem(depth, UInt(width = BigInt(n).bitLength))

  when(io.read_routing_table_request.valid && io.read_routing_table_response.ready) {
    val cmd = io.read_routing_table_request.bits
    io.read_routing_table_response.bits := tbl(cmd.addr)
  }

  when(io.load_routing_table_request.valid) {
    val cmd = io.load_routing_table_request.bits
    tbl(cmd.addr) := cmd.data
  }

  when(io.in.valid) {
    val pkt = io.in.bits
    val idx = tbl(pkt.header(0))
    when(io.outs(idx).ready) {
      io.in.ready := Bool(true)
      io.outs(idx).bits := pkt
    }
  }
  //  when(io.read_routing_table_request.valid && io.read_routing_table_response.ready) {
  //    val cmd = io.read_routing_table_request.deq()
  //    io.read_routing_table_response.enq(tbl(cmd.addr))
  //  }
  //  .elsewhen(io.load_routing_table_request.valid) {
  //    val cmd = io.load_routing_table_request.deq()
  //    tbl(cmd.addr) := cmd.data
  //  }
  //  .elsewhen(io.in.valid) {
  //    val pkt = io.in.bits
  //    val idx = tbl(pkt.header(0))
  //    when(io.outs(idx).ready) {
  //      io.in.deq()
  //      io.outs(idx).enq(pkt)
  //    }
  //  }
}

class DecoupledRouterUnitTester extends DecoupledTester {
  val device_under_test = Module(new DecoupledRouter)
  val c = device_under_test

  def rd(addr: Int, data: Int) = {
    input_event(List(c.io.read_routing_table_request.bits.addr -> addr))
    output_event(List(c.io.read_routing_table_response.bits -> data))

    //    poke(c.io.in.valid,        0)     // initialize the in queue
    //    poke(c.io.load_routing_table_request.valid,    0)     // initialize
    //    poke(c.io.read_routing_table_request.valid,    1)
    //    poke(c.io.read_routing_table_response.ready,   1)
    //    poke(c.io.read_routing_table_request.bits.addr, addr)
    //    expect(c.io.read_routing_table_response.bits, data)
    //    step(1)
  }

  def wr(addr: Int, data: Int)  = {
    input_event(List(
      c.io.load_routing_table_request.bits.addr -> addr,
      c.io.load_routing_table_request.bits.data -> data
    ))
    //    poke(c.io.in.valid,         0)
    //    poke(c.io.read_routing_table_request.valid,      0)
    //    poke(c.io.load_routing_table_request.valid,     1)
    //    poke(c.io.load_routing_table_request.bits.addr, addr)
    //    poke(c.io.load_routing_table_request.bits.data, data)
    //    step(1)
  }

  def rt(header: Int, body: Int)  = {
    //    for (out <- c.io.outs)
    //      poke(out.ready, 1)
    //    poke(c.io.read_routing_table_request.valid,    0)
    //    poke(c.io.load_routing_table_request.valid,   0)
    //    poke(c.io.in.valid,       1)
    //    poke(c.io.in.bits.header, header)
    //    poke(c.io.in.bits.body,   body)

    //    for (out <- c.io.outs) {
    //      when(out.valid) {
    //        printf("io.valid, io.pc %d\n", pc)
    ////      stop(0)
    //      } otherwise {
    //        step(1)
    //      }
    //    }
    //    expect(io.pc < UInt(10))
  }
  rd(0, 0)
  wr(0, 1)
  rd(0, 1)
  rt(0, 1)

  finish()
}
