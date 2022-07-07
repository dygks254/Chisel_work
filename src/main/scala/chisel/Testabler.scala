

import chisel3._
import chisel3.stage.ChiselStage
import yohan.Element.ElementTransmitModule

import scala.util.Random.nextGaussian

class Testabler extends Module{

  class TestableBundle extends Bundle{
    val out: Vec[UInt] = Output(Vec(100, UInt(1000.W)))
  }

  val testableBundle: TestableBundle = IO(new TestableBundle())
  val domainChanger: Domainchanger = Module(new Domainchanger((DomainParams(inputNumber = 1000, outputNumber = 100))))
  val transmit = Seq.fill(1000)(Module(new ElementTransmitModule[UInt](Seq(java.lang.Math.max(50+nextGaussian()*4, 0).toInt.U))))

  domainChanger.io.in.valid := true.B
  transmit.zipWithIndex.foreach{ case (aGenerator, index) =>
    aGenerator.io.generatedDecoupledOutput.ready := true.B
    domainChanger.io.in.bits(index) := aGenerator.io.generatedSimpleOutput
  }

  testableBundle.out.zip(domainChanger.io.out).foreach {case(out,in) => out := in}

}


object Testabler extends App{
  println("Starting generate")
  (new ChiselStage).emitVerilog(new Testabler)
}