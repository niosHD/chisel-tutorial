package examples

import Chisel._
import Chisel.testers._

object DecoupledRouter {
  val addressWidth    = 32
  val dataWidth       = 64
  val headerWidth     =  8
  val routeTableSize  = 15
  val numberOfOutputs =  4
}

class DecoupledReadCmd extends Bundle {
  val addr = UInt(width = DecoupledRouter.addressWidth)
}

class DecoupledWriteCmd extends DecoupledReadCmd {
  val data = UInt(width = DecoupledRouter.addressWidth)
}

class DecoupledPacket extends Bundle {
  val header = UInt(width = DecoupledRouter.headerWidth)
  val body   = Bits(width = DecoupledRouter.dataWidth)
}

/**
  * This router circuit
  * It routes a packet placed on it's input to one of n output ports
  *
  * @param n is number of fanned outputs for the routed packet
  */
class DecoupledRouterIO(n: Int) extends Bundle {
  //  override def cloneType           = new DecoupledRouterIO(n).asInstanceOf[this.type]
  val read_routing_table_request   = new DeqIO(new DecoupledReadCmd())
  val read_routing_table_response  = new EnqIO(UInt(width = DecoupledRouter.addressWidth))
  val load_routing_table_request   = new DeqIO(new DecoupledWriteCmd())
  val in                           = new DeqIO(new DecoupledPacket())
  val outs                         = Vec(n, new EnqIO(new DecoupledPacket()))
}

/**
  * routes packets by using their header as an index into an externally loaded and readable table,
  * The number of addresses recognized does not need to match the number of outputs
  */
class DecoupledRouter extends Module {
  val depth = DecoupledRouter.routeTableSize
  val n     = DecoupledRouter.numberOfOutputs
  val io    = new DecoupledRouterIO(n)
  val tbl   = Mem(depth, UInt(width = BigInt(n).bitLength))

  when(reset) {
    tbl.indices.foreach { index =>
      tbl(index) := UInt(0, width = DecoupledRouter.addressWidth)
    }
  }

  io.read_routing_table_request.init()
  io.load_routing_table_request.init()
  io.read_routing_table_response.init()
  io.in.init()
  io.outs.foreach { out => out.init() }

  when(io.read_routing_table_request.valid && io.read_routing_table_response.ready) {
    io.read_routing_table_response.enq(tbl(
      io.read_routing_table_request.deq().addr
    ))
  }
    .elsewhen(io.load_routing_table_request.valid) {
      val cmd = io.load_routing_table_request.deq()
      tbl(cmd.addr) := cmd.data
      printf("setting tbl(%d) to %d\n", cmd.addr, cmd.data)
    }
    .elsewhen(io.in.valid) {
      val pkt = io.in.bits
      val idx = tbl(pkt.header(log2Up(DecoupledRouter.routeTableSize), 0))
      when(io.outs(idx).ready) {
        io.in.deq()
        io.outs(idx).enq(pkt)
        printf("got packet to route header %d, data %d, being routed to out(%d)\n", pkt.header, pkt.body, tbl(pkt.header))
      }
    }
}

class DecoupledRouterUnitTester(number_of_packets_to_send: Int) extends OrderedDecoupledHWIOTester {
  val device_under_test = Module(new DecoupledRouter)
  val c = device_under_test
  enable_all_debug = true

  rnd.setSeed(0)

  def readRoutingTable(addr: Int, data: Int): Unit = {
    inputEvent(c.io.read_routing_table_request.bits.addr -> addr)
    outputEvent(c.io.read_routing_table_response.bits -> data)
  }

  def writeRoutingTable(addr: Int, data: Int): Unit = {
    inputEvent(
      c.io.load_routing_table_request.bits.addr -> addr,
      c.io.load_routing_table_request.bits.data -> data
    )
  }

  def writeRoutingTableWithConfirm(addr: Int, data: Int): Unit = {
    writeRoutingTable(addr, data)
    readRoutingTable(addr, data)
  }

  def routePacket(header: Int, body: Int, routed_to: Int): Unit = {
    inputEvent(c.io.in.bits.header -> header, c.io.in.bits.body -> body)
    outputEvent(c.io.outs(routed_to).bits.body -> body)
    logScalaDebug(s"rout_packet $header $body should go to out($routed_to)")
  }

  readRoutingTable(0, 0) // confirm we initialized the routing table

  // load routing table, confirm each write as built
  for (i <- 0 until DecoupledRouter.numberOfOutputs) {
    writeRoutingTableWithConfirm(i, (i + 1) % DecoupledRouter.numberOfOutputs)
  }
  // check them in reverse order just for fun
  for (i <- DecoupledRouter.numberOfOutputs - 1 to 0 by -1) {
    readRoutingTable(i, (i + 1) % DecoupledRouter.numberOfOutputs)
  }

  // send some regular packets
  for (i <- 0 until DecoupledRouter.numberOfOutputs) {
    routePacket(i, i * 3, (i + 1) % 4)
  }

  // generate a new routing table
  val new_routing_table = Array.tabulate(DecoupledRouter.routeTableSize) { _ =>
    scala.util.Random.nextInt(DecoupledRouter.numberOfOutputs)
  }

  // load a new routing table
  for ((destination, index) <- new_routing_table.zipWithIndex) {
    writeRoutingTable(index, destination)
  }

  // send a bunch of packets, with random values
  for (i <- 0 to number_of_packets_to_send) {
    val data = rnd.nextInt(Int.MaxValue - 1)
    routePacket(i % DecoupledRouter.routeTableSize, data, new_routing_table(i % DecoupledRouter.routeTableSize))
  }
}

