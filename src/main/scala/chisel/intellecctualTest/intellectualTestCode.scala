//
//package yohan.intellecctualTestCode
//
//import yohan.intellecctualTest._
//
//import chisel3.util._
//import chisel3._
//import chisel3.internal.firrtl.Width
//import chisel3.stage.ChiselStage
////import chisel3.tester.{testableClock, testableData}
//import chiselExample.problems.imageProcesser.ImageLoader
//import chiseltest.RawTester.test
//
//import java.awt.Color
//import java.awt.image.BufferedImage
//import java.io.File
//import javax.imageio.ImageIO
//import scala.collection.mutable
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.duration.Duration
//import scala.concurrent.{Await, Future}
//import scala.reflect.io
//
//
//object IntellectualCCTV extends App {
//
//  //  generating(new IntellectualCCTV(IntellectualCCTVParams(hardfixFrameCoefficient = 5, hardfixL1NormSensitivityThreshold = 20, hardfixFrameSensitivityThreshold = 35)))
//  val getResult: Bool = Input(Bool())
//
//  // 주의!! 해당 테스트 예제는 colorDomain = 3(R,G,B), scale 8bits에 hardfix 되어있습니다
//  val colorDomain:Int = 3
//  val colorScale:Int = 8
//  /***Image Load***/
//  var frames : Array[Seq[Seq[mutable.Buffer[Int]]]] = Array()
//  var videoHeight:Int = 0
//  var videoWidth:Int = 0
//  val aFuturable = Future {
//    new ImageLoader().loadImages(
//      folderPath = "src/main/scala/chiselExample/problems/imageProcesser/imageSet",
//      scaleFactor = 2,
//      response = { (getInfo, response) =>
//        frames = response.map(_._2)
//        videoHeight = getInfo._1
//        videoWidth = getInfo._2
//      })
//  }
//
//  Await.result(aFuturable, Duration.Inf)
//  /***Image Load Complete***/
//  test(new IntellectualCCTV(IntellectualCCTVParams(
//    videoHeight = videoHeight,
//    videoWidth = videoWidth,
//    hardfixFrameCoefficient = 5,
//    hardfixL1NormSensitivityThreshold = 20,
//    hardfixFrameSensitivityThreshold = 35)
//  )) { c =>
//
//    c.io.getResult.poke(false.B)
//
//    frames.foreach{ aFrame =>
//
//      for (yy <- 0 until videoHeight) {
//        var aCompress = "b"
//        for (xx <- 0 until videoWidth) {
//          aCompress += "%08d".format(aFrame.head(yy)(xx).toBinaryString.toInt) /*+ getZero(num = 24*xx)   */  //R
//          aCompress += "%08d".format(aFrame(1)(yy)(xx).toBinaryString.toInt)   /*+ getZero(num = 8+24*xx) */  //G
//          aCompress += "%08d".format(aFrame(2)(yy)(xx).toBinaryString.toInt)   /*+ getZero(num = 16+24*xx)*/  //B
//        }
//        //          c.io.videoInput(yy).poke(aCompress.U)
//      }
//      c.clock.step()
//
//    }
//
//  }
//
//}