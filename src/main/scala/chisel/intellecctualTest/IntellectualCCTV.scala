package yohan.IntellectualCCTV

import chisel3._
import chisel3.util._
import chisel3.internal.firrtl.Width


//(960 x 480) x 24bits
//=> 960 x (480 x 24) bits
case class IntellectualCCTVParams(
                                   videoWidth:Int = 960,
                                   videoHeight:Int = 480,
                                   colorDomain:Int = 3,
                                   colorScale:Int = 8 /**256bit*/,
                                   hardfixFrameCoefficient:Int = 50,
                                   hardfixL1NormSensitivityThreshold:Int = 20,
                                   hardfixFrameSensitivityThreshold:Int = 15
                                 )
{
  def bitSize(numb : Int): Int = log2Ceil(numb + 1)
  val inputBits: Int = colorDomain*colorScale
  val outputBits: Int = bitSize(videoWidth*videoHeight*colorDomain*colorScale)
}

class IntellectualCCTV(intellectualCCTVParams:IntellectualCCTVParams) extends Module {

  val videoLines:Int = intellectualCCTVParams.videoHeight
  val videoWidth:Int = intellectualCCTVParams.videoWidth
  val lineDataSize: Width = (intellectualCCTVParams.videoWidth * intellectualCCTVParams.inputBits).W

  class VolumeIntegratorBundle extends Bundle {
    val videoInput: Vec[UInt] = Input(Vec(videoLines, UInt(lineDataSize)))
    val getResult: Bool = Input(Bool())
    val AlertOutput: Bool = Output(Bool())
    val Output2: Vec[UInt] = Output(Vec(videoLines, UInt(lineDataSize)))
  }

  val io: VolumeIntegratorBundle = IO(new VolumeIntegratorBundle())

  /**아래에서 구현**/

}