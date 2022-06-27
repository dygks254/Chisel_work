import chisel3._
import chisel3.stage.ChiselStage
import chisel3.util._

case class DomainParams(inputNumber : Int, outputNumber : Int){
  def bitSize(numb : Int) = log2Ceil(numb + 1)
  val inputBits: Int = bitSize(inputNumber)
  val outputBits: Int = bitSize(outputNumber)
}

class Domainchanger(domainParams : DomainParams) extends Module {

  class DomainchangerBundle extends Bundle {
    val in: DecoupledIO[Vec[UInt]] = Flipped(Decoupled(Vec(domainParams.inputNumber, UInt(domainParams.outputBits.W)))) //input size depends on outputNumber
    val out: Vec[UInt] = Output(Vec(domainParams.outputNumber, UInt(domainParams.inputBits.W))) //counting number depends on inputNumber.
  }

  val io : DomainchangerBundle = IO(new DomainchangerBundle())
  val counterTop: Counter = Counter(domainParams.inputNumber+2)
  val readyReg: UInt = RegInit(1.U(1.W))
  val outReg: Seq[UInt] = Seq.fill(domainParams.outputNumber)(RegInit(0.U(domainParams.inputBits.W)))
  //  val resultNow: UInt = RegInit(0.U(domainParams.inputBits.W))

  //  println(counterTop.va)

  io.in.ready := readyReg

  when(io.in.fire){
    counterTop.inc()
  }.elsewhen(io.in.valid) {
    counterTop.reset()
  }

  when(reset.asBool === true.B){
    readyReg := 1.U
    counterTop.reset()
    outReg.map{ x => x := 0.U}
  }.elsewhen( counterTop.value >= domainParams.inputNumber.U - 1.U) {
    readyReg := 0.U
  }

  val  resultNow : UInt = MuxLookup(counterTop.value, 0.U, {
    val resultNowS = io.in.bits.zipWithIndex.map { case (inputValue : UInt, inputIndex : Int) =>
      (inputIndex.U, inputValue)
    }
    resultNowS
  })


  outReg.zipWithIndex.foreach{ case (outNow, index) =>
    when( resultNow === index.U & io.in.fire){
      outNow := outNow + 1.U
    }.otherwise{
      outNow := outNow
    }
    io.out(index) := outNow
  }
}

object yohan_Histogram extends App{
  println("Starting generate")
  (new ChiselStage).emitVerilog(new Domainchanger( DomainParams(1000, 100)) )
}