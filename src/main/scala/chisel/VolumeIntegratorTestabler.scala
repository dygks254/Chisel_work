import chisel3._
import chisel3.stage.ChiselStage
import chisel3.util._
import functional.transmitter.ElementTransmitModule
import yohan.VolumeIntegrator1._

import scala.math._

class VolumeIntegratorTestabler extends Module{

  class VolumeIntegratorTestableBundle extends Bundle {
    val out: UInt = Output(UInt(1000.W)) //counting number depends on inputNumber.
  }

  def randomGen(num : Int): Int = scala.util.Random.nextInt(num)

  def aFakeWave(evHeight : Int, fluctuation:Double) : Seq[Double] = {
    val unitWave = 1 + randomGen(100)
    val partialWave = Seq.fill(unitWave) {
      val setHeight = evHeight * (1 + randomGen(100)) / 100
      val setFluctuation = fluctuation * (0.5 + (((randomGen(100) + 1)/100.0)/2.0))
      Seq.tabulate(360)(aState => setHeight + (setFluctuation * cos(aState * (Pi / 180))))
    }
    Seq.tabulate(360)( idx => partialWave.foldLeft(0.0)((cum, aSeq) => cum + aSeq(idx))/unitWave)
  }

  def seqRepeat(n: Int, target : Seq[Any]): Seq[Any]={
    if (n == 0) target
    else target ++ seqRepeat(n - 1, target)
  }

  def fakeWaves(num : Int, ranHeight : Int, randFluctuation:Int, duration : Int = 100): Seq[Seq[Int]] = {
    val waves = Seq.fill(num)(seqRepeat(duration, aFakeWave(evHeight = ranHeight*(max(randomGen(300)/100,1)), fluctuation = randFluctuation * (0.5 + (((randomGen(100) + 1)/100.0)/2.0)))))
    waves.map( aWave => aWave.map(aHeight => aHeight.asInstanceOf[Double].toInt))
  }

  val testableBundle:VolumeIntegratorTestableBundle = IO(new VolumeIntegratorTestableBundle())
  val volumeIntegrator: VolumeIntegrator = Module(new VolumeIntegrator(VolumeIntegratorParams(maxWaveHeight = 150, inputNumber = 30, timemainFactor = 10)))
  val transmit: Seq[ElementTransmitModule[UInt]] = fakeWaves(30, 150, 50, 20).map(aWaveSet =>
    Module(new ElementTransmitModule[UInt](aWaveSet.map(aWave => aWave.U)))
  )

  volumeIntegrator.io.option := 10.U

  transmit.zip(volumeIntegrator.io.in).foreach{ a =>
    a._1.io.generatedDecoupledOutput.ready := true.B
    a._2 := a._1.io.generatedSimpleOutput
  }

  testableBundle.out := volumeIntegrator.io.out

}

object VolumeIntegratorTestModule extends App{
  (new ChiselStage).emitVerilog(new VolumeIntegratorTestabler())
}

