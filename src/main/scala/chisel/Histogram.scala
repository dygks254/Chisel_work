import chisel3._
import chisel3.stage.ChiselStage
import chisel3.util._
import sun.java2d.marlin.stats.Histogram

case class DomainParams(inputNumber : Int, outputNumber : Int){
  def bitSize(numb : Int) = log2Ceil(numb + 1)
  val inputBits: Int = bitSize(inputNumber)
  val outputBits: Int = bitSize(outputNumber)
}

class Domainchanger( domainParams: DomainParams) extends Module {

  class DomainchangerBundle extends Bundle {
    val in: DecoupledIO[Vec[UInt]] = Flipped(Decoupled(Vec(domainParams.inputNumber, UInt(domainParams.outputBits.W)))) //input size depends on outputNumber
    val out: Vec[UInt] = Output(Vec(domainParams.outputNumber, UInt(domainParams.inputBits.W))) //counting number depends on inputNumber.
  }

  val io : DomainchangerBundle = IO(new DomainchangerBundle)
  val readyReg : Bool = RegInit(true.B)

  val caseMatcher : Counter = Counter(domainParams.inputNumber + 1)

  when(io.in.fire){
    caseMatcher.inc()
  }.otherwise{
    caseMatcher.reset()
  }

  val endConditionNumber = 99999
  val  candidateValue : UInt = MuxLookup(caseMatcher.value, 0.U, {
    val indexedSeq = io.in.bits.zipWithIndex.map { case (inputValue : UInt, inputIndex : Int) =>
      (inputIndex.U, inputValue)
    }
    val finalState = indexedSeq :+ (indexedSeq.size.U, endConditionNumber.U)
    finalState
  })

  val filled: Seq[UInt] = Seq.fill(domainParams.outputNumber)(RegInit(UInt(domainParams.inputBits.W), 0.U))

  filled.zip(io.out).zipWithIndex.foreach{ case (value, index) =>
    when(io.in.fire){
      when(index.U === candidateValue){
        filled(index) := filled(index) + 1.U
      }.elsewhen(candidateValue === endConditionNumber.U){
        readyReg := false.B
      }
    }
    value._2 := value._1
  }

  when(reset.asBool === true.B){
    caseMatcher.reset()
    io.in.ready := false.B
    filled.map{ aReg => aReg := 0.U}
  }.otherwise{
    io.in.ready := true.B
  }

  io.in.ready := readyReg

}

object yohan_Histogram extends App{
  println("Starting generate")
  (new ChiselStage).emitVerilog(new Domainchanger( DomainParams(1000, 100)) )
}