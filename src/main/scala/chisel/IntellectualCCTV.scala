import Chisel.log2Ceil
import chisel3._
import chisel3.internal.firrtl.Width
import chisel3.stage.ChiselStage
import chiselExample.problems.imageProcesser.ImageLoader

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.reflect.io

//(960 x 480) x 24bits
//=> 960 x (480 x 24) bits
case class IntellectualCCTVParams(
                                   videoWidth:Int = 9,
                                   videoHeight:Int = 10,
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

object yohan_IntellectualCCTV extends App{
  println("Starting generate")
  (new ChiselStage).emitVerilog(new IntellectualCCTV( IntellectualCCTVParams()) )
}




/*
object IntellectualCCTV extends App {

  val getResult: Bool = Input(Bool())

  // 주의!! 해당 테스트 예제는 colorDomain = 3(R,G,B), scale 8bits에 hardfix 되어있습니다
  val colorDomain:Int = 3
  val colorScale:Int = 8
  /***Image Load***/
  var frames : Array[Seq[Seq[mutable.Buffer[Int]]]] = Array()
  var videoHeight:Int = 0
  var videoWidth:Int = 0
  val aFuturable = Future {
    new ImageLoader().loadImages(
      folderPath = "src\\main\\scala\\chiselExample\\problems\\imageProcesser\\imageSet",
      scaleFactor = 1,
      response = { (getInfo, response) =>
        frames = response.map(_._2)
        videoHeight = getInfo._1
        videoWidth = getInfo._2
      })
  }

  Await.result(aFuturable, Duration.Inf)
  /***Image Load Complete***/
  test(new IntellectualCCTV(IntellectualCCTVParams(
    videoHeight = videoHeight,
    videoWidth = videoWidth,
    hardfixFrameCoefficient = 5,
    hardfixL1NormSensitivityThreshold = 20,
    hardfixFrameSensitivityThreshold = 35)
  )) { c =>

    c.io.getResult.poke(false.B)

    frames.foreach{ aFrame =>

      for (yy <- 0 until videoHeight) {
        var aCompress = 0
        for (xx <- 0 until videoWidth) {
          aCompress += aFrame.head(yy)(xx) << (0*xx) //R
          aCompress += aFrame(1)(yy)(xx)   << (8*xx) //G
          aCompress += aFrame(2)(yy)(xx)   << (16*xx) //B
        }
        c.io.videoInput(yy).poke(aCompress.U)
      }

      c.clock.step()

    }

  }
}

