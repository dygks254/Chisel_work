package yohan.VolumeIntegrator2

import chisel3._
import chisel3.stage.ChiselStage
import chisel3.util._

import scala.annotation.tailrec

case class VolumeIntegratorParams(maxWaveHeight:Int, inputNumber:Int, timemainFactor:Int, maxOptionIs:Int=1000){
  def bitSize(numb : Int): Int = log2Ceil(numb + 1)
  val outputBits: Int = bitSize(maxWaveHeight)
  val divMax: Int = inputNumber*timemainFactor
  val bitMax: Int = maxWaveHeight + log2Ceil(divMax+1)
}


class VolumeIntegrator(volumeIntegratorParams:VolumeIntegratorParams) extends Module {

  val addMax = 10

  class VolumeIntegratorBundle extends Bundle {
    val in: Vec[UInt] = Input(Vec(volumeIntegratorParams.inputNumber, UInt(volumeIntegratorParams.outputBits.W)))
    val out: UInt = Output(UInt(volumeIntegratorParams.outputBits.W))
  }

  class GripperInDelayNCycles[T <: Data](data:T, gripper:Int)(cycle:Int = gripper) extends Module {

    class DelayNCyclesBundle() extends Bundle {
      val in: T = Input(data)
      val out: T = Output(data)
    }

    val delayedBundle: DelayNCyclesBundle = new DelayNCyclesBundle
    val io:DelayNCyclesBundle = IO(delayedBundle)
    val delaCon: Vec[UInt] = RegInit(VecInit(Seq.fill(cycle)(0.U(volumeIntegratorParams.bitMax.W))))
    delaCon(0) := io.in
    delaCon.tail.foldLeft(delaCon(0))((a,b) => {
      b := a
      b
    })
    val tempAdd: UInt = delaCon.reduce(_ +& _)
    io.out := tempAdd
  }

  @tailrec
  final def AddDiv(inNum : Seq[(Int,Int,Int)]) : Seq[(Int,Int,Int)] = {
    var tmpDiv = 0
    if((inNum.last._1)%addMax > 0)  {tmpDiv = 1}
    else                            {tmpDiv = 0}
    if ( inNum.last._1 < addMax ){ inNum :+ (1, inNum.last._1, inNum.last._1+(inNum.last._1/addMax)+1 ) }
    else{ AddDiv( inNum :+ ( (inNum.last._1/addMax)+tmpDiv, (inNum.last._1)%addMax, inNum.last._1+inNum.last._3 )) }
  }

  val io: VolumeIntegratorBundle = IO(new VolumeIntegratorBundle)
  val allS: Seq[(Int, Int, Int)] = AddDiv( Seq.fill(1)( (volumeIntegratorParams.inputNumber, 0, 0) ) ).tail
  val allSReg: Vec[UInt] = RegInit(VecInit(Seq.fill( allS.map({ case(_1,_,_) => _1 }).sum )(0.U(volumeIntegratorParams.bitMax.W))))
  val regFirst: Int = allS.head._1

//   println(allS)
//   println(allSReg)

  var allRegNow : Int = 0
  var allRegPrev = 0

  allS.tail.indices.foreach({ allSNum =>
    if (allSNum == 0) {
      allRegNow = regFirst
      allRegPrev = 0
    }
    else {
      allRegNow = (0 until allSNum+1).map({x:Int => allS(x)._1}).sum
      allRegPrev = (0 until allSNum).map({x:Int => allS(x)._1}).sum
    }
    (0 until allS(allSNum+1)._1-1).foreach({ allSRegNum =>
      allSReg(allRegNow + allSRegNum) := (0 until addMax).map({ x => allSReg(allRegPrev+allSRegNum*10+x) }).reduce(_+&_)
    })
    if( allS(allSNum+1)._2 == 0 ){
      allSReg(allRegNow + allS(allSNum+1)._1-1) := (0 until addMax).map({ x => allSReg(allRegPrev+(allS(allSNum+1)._1-1)*10+x) }).reduce(_+&_)
    }else{
      allSReg(allRegNow + allS(allSNum+1)._1-1) := (0 until allS(allSNum+1)._2).map({ x =>
        allSReg( allRegPrev+allS(allSNum+1)._1*10 - addMax + x ) }).reduce(_+&_)
    }
  })

  (0 until allS.head._1).foreach( x => allSReg(x) := (0 until addMax).map({ y => io.in(x*addMax + y)}).reduce(_+&_) )

  val bufferReg: GripperInDelayNCycles[UInt] = Module(new GripperInDelayNCycles(data = UInt(volumeIntegratorParams.bitMax.W), gripper = volumeIntegratorParams.timemainFactor)())

  bufferReg.io.in := allSReg.last
  io.out := bufferReg.io.out / (volumeIntegratorParams.divMax.U)


}


object yohan_VolumeIntegrator extends App{
  println("Starting generate")
  (new ChiselStage).emitVerilog(new VolumeIntegrator( VolumeIntegratorParams(maxWaveHeight=10, inputNumber=30, timemainFactor=4) ) )
}
