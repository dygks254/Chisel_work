import chisel3._
import chisel3.internal.firrtl._
import chisel3.stage.ChiselStage
import chisel3.util._

case class VolumeIntegratorParams(maxWaveHeight:Int, inputNumber:Int, timemainFactor:Int, maxOptionIs:Int=1000){
  def bitSize(numb : Int): Int = log2Ceil(numb + 1)
  val inputBits: Int = bitSize(inputNumber)
  val outputBits: Int = bitSize(maxWaveHeight)
  val volumeBits: Int = bitSize(timemainFactor)
}

class VolumeIntegrator(volumeIntegratorParams:VolumeIntegratorParams) extends Module {

  class VolumeIntegratorBundle extends Bundle {
    val in: Vec[UInt] = Input(Vec(volumeIntegratorParams.inputNumber, UInt(volumeIntegratorParams.outputBits.W)))
    val option: UInt = Input(UInt(volumeIntegratorParams.outputBits.W))
    val out: UInt = Output(UInt(volumeIntegratorParams.volumeBits.W))
    val outChecker : UInt = Output(UInt(100.W))
    val outDelayedChecker : UInt = Output(UInt(100.W))
  }

  class GripperInDelayNCycles[T <: Data](data:T, gripper:Int)(cycle:Int = gripper) extends Module {

    class DelayNCyclesBundle() extends Bundle {
      val in: T = Input(data)
      val out: T = Output(data)
    }

    val delayedBundle: DelayNCyclesBundle = new DelayNCyclesBundle
    val io:DelayNCyclesBundle = IO(delayedBundle)
    var lastConn:T = io.in
    for (i <- 0 until cycle) {
      lastConn = RegNext(lastConn)
      if(i == gripper) io.out := lastConn
    }
  }


  val io = IO(new VolumeIntegratorBundle)
  val maxBitSize = volumeIntegratorParams.bitSize(volumeIntegratorParams.inputNumber*volumeIntegratorParams.maxWaveHeight*volumeIntegratorParams.inputBits)
  val stateSaver = RegInit(0.U(maxBitSize.W))
  val currentInputSum = io.in.reduce{_+&_}

  val delayed = Module(new GripperInDelayNCycles(data=UInt(), gripper = volumeIntegratorParams.timemainFactor)(cycle=volumeIntegratorParams.inputNumber))
  delayed.io.in := currentInputSum
  when(stateSaver + currentInputSum > delayed.io.out){
    stateSaver := stateSaver + currentInputSum - delayed.io.out
  }.otherwise{
    stateSaver := stateSaver + delayed.io.out
  }

  val scale = (volumeIntegratorParams.inputNumber * volumeIntegratorParams.timemainFactor).U
  io.out := stateSaver / scale

  io.outChecker := stateSaver
  io.outDelayedChecker := delayed.io.out

}

object yohan_VolumeIntegrator extends App{
  println("Starting generate")
  (new ChiselStage).emitVerilog(new VolumeIntegrator( VolumeIntegratorParams(maxWaveHeight=10, inputNumber=5, timemainFactor=4) ) )
}