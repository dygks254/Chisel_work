package yohan.VolumeIntegrator2

import chisel3._
import chisel3.stage.ChiselStage
import chisel3.util._

import scala.annotation.tailrec

case class VolumeIntegratorParams(maxWaveHeight:Int, inputNumber:Int, timemainFactor:Int, maxOptionIs:Int=1000, addMax:Int = 4){
  def bitSize(numb : Int): Int = log2Ceil(numb + 1)
  val inputBits: Int = bitSize(inputNumber)
  val outputBits: Int = bitSize(maxWaveHeight)

  require( inputNumber <= maxOptionIs)
}

class VolumeIntegrator(volumeIntegratorParams:VolumeIntegratorParams) extends Module {

  class VolumeIntegratorBundle extends Bundle {
    val in: Vec[UInt] = Input(Vec(volumeIntegratorParams.inputNumber, UInt(volumeIntegratorParams.outputBits.W)))
    val option: UInt = Input(UInt(volumeIntegratorParams.outputBits.W))
    val out: UInt = Output(UInt(volumeIntegratorParams.outputBits.W))
  }

  @tailrec
  final def AddDiv(inNum : Seq[Int] ): Seq[Int] ={
    if ( inNum.last < volumeIntegratorParams.addMax){ inNum }
    else{ AddDiv(inNum :+ (inNum.last/volumeIntegratorParams.addMax)+1 ) }
  }

  val allS: Seq[Int] = AddDiv(Seq.fill(1)(volumeIntegratorParams.inputNumber/4+1))
  val allSDiv =

  val adderSWire: Seq[UInt] = Seq.fill(allS.sum + volumeIntegratorParams.inputNumber)(Wire(UInt((volumeIntegratorParams.inputBits+volumeIntegratorParams.addMax*allS.size).W)))
  val adderSReg: Seq[UInt] = Seq.fill(allS.sum)(RegInit(0.U(volumeIntegratorParams.inputBits.W)))

//  allS.zipWithIndex.foreach { case(inNum, inIndex) =>
//    (0 until inNum-1).zipWithIndex.foreach{ case (in1, in2) =>
//      (0 until volumeIntegratorParams.addMax).foreach{ x =>
//        adderSReg(inNum) += adderSWire(in1*4 + x)
//      }
//    }
//  }

//  alls : 26, 7, 2
//  adderSWire : 101+26+7+2
//  adderSReg  : 26 + 7 + 2

  allS.zipWithIndex.foreach { case(allSNum, allSIndex) =>
    (0 until volumeIntegratorParams.addMax).foreach{ prevWire =>
      (0 until allSNum-1).foreach{ nowWire =>
        if (allSIndex > 1){
            adderSReg(allS(allSIndex)+nowWire) += adderSWire( (allS(allSIndex)-1)*4 + nowWire*4 + prevWire)
          }else{
            adderSReg(nowWire) += adderSWire(nowWire*4+prevWire)
        }
      }
    }
    (0 until )
    if (allSIndex > 1){
      adderSReg(allSNum) += adderSWire( (allS(allSIndex)-1)*4 + nowWire*4 + prevWire)
    }else{
      adderSReg(nowWire) += adderSWire(nowWire*4+prevWire)
    }
  }


}


object yohan_VolumeIntegrator extends App{
  println("Starting generate")
  (new ChiselStage).emitVerilog(new VolumeIntegrator( VolumeIntegratorParams(maxWaveHeight=10, inputNumber=5, timemainFactor=4) ) )
}