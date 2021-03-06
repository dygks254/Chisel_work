import chisel3._
import chisel3.internal.firrtl._
import chisel3.stage.ChiselStage
import chisel3.util._

case class IntellectualCCTVParams(
  videoWidth:Int = 960, videoHeight:Int = 480, colorDomain:Int = 3,
  colorScale:Int = 8/**256bit*/, hardfixFrameCoefficient : Int, hardfixSensitivityThreshold : Int){

  def bitSize(numb : Int): Int = log2Ceil(numb + 1)
  val inputBits: Int = colorDomain*colorScale
  val outputBits: Int = bitSize(videoWidth*videoHeight*colorDomain*colorScale)
}

class IntellectualCCTV(intellectualCCTVParams:IntellectualCCTVParams) extends Module {

  class VolumeIntegratorBundle extends Bundle {
    val videoFrame: Int = intellectualCCTVParams.videoWidth*intellectualCCTVParams.videoHeight
    val dataSize: Width = intellectualCCTVParams.inputBits.W
    val videoInput: Vec[UInt] = Input(Vec(videoFrame, UInt(dataSize)))
    val getResult: Bool = Input(Bool())
    val AlertOutput: Vec[UInt] = Output(Vec(videoFrame, UInt(dataSize)))
    val Output2: DecoupledIO[Vec[UInt]] = Decoupled(Vec(videoFrame, UInt(dataSize)))
  }


}

object yohan_IntellectualCCTV extends App{
  println("Starting generate")
  (new ChiselStage).emitVerilog(new IntellectualCCTV( IntellectualCCTVParams(hardfixFrameCoefficient=10,hardfixSensitivityThreshold=10)) )
}