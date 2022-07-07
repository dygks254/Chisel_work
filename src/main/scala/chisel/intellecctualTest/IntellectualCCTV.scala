package yohan.intellecctualTest

import chisel3._
import chisel3.experimental._
import chisel3.internal.firrtl.Width
import chisel3.stage.ChiselStage
import chisel3.util._


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
  val oneFrame: Int = videoWidth*videoHeight
  val oneFrameColor: Int = oneFrame*colorDomain
  val pixel: Int = colorDomain*colorScale
  val inputBit : Int = pixel + 1
  val outputBit : Int = pixel + 1
  val getTime: Int = 50
}

class IntellectualCCTV(intellectualCCTVParams:IntellectualCCTVParams) extends Module {

  class VolumeIntegratorBundle extends Bundle {
    val videoInput: UInt = Input((UInt(intellectualCCTVParams.inputBit.W)))
    val getResult: Bool = Input(Bool())
    val AlertOutput: Bool = Output(Bool())
    val Output2: UInt = Output(UInt(intellectualCCTVParams.outputBit.W))
  }
  val io: VolumeIntegratorBundle = IO(new VolumeIntegratorBundle())


  //////////////////////////////////////////////////////////////////////// fsm Code
  //////////////////////////////////////////////////////////////////////// fsm Code

  object StateGet extends ChiselEnum {
    val sIdle, sGet = Value
  }
  val stateGet: StateGet.Type = RegInit(StateGet.sIdle)
  val counterGet: Counter = Counter(intellectualCCTVParams.getTime)
  switch(stateGet){
    is(StateGet.sIdle){
      counterGet.reset()
      io.AlertOutput := false.B
    }
    is(StateGet.sGet){
      counterGet.inc()
      io.AlertOutput := true.B
      when(counterGet.value === (intellectualCCTVParams.getTime-1).U){
        stateGet := StateGet.sIdle
      }
    }
  }

  object State extends ChiselEnum {
    val sIdle, sResult ,sAction, sDiff, sCharge, sRead = Value
  }
  val state: State.Type = RegInit(State.sIdle)

  val chargeIn = WireInit(false.B)
  val diffThreshold = WireInit(false.B)
  val actionWriteMem = WireInit(false.B)
  val resultCountMem = WireInit(false.B)

  switch(state){
    is(State.sIdle){
      when( io.videoInput === "h1_fff_fff_fff".U & chargeIn === false.B ){
        state := State.sCharge
      }.elsewhen(io.videoInput === "h1_fff_fff_fff".U){
        state := State.sRead
      }.elsewhen(io.getResult === true.B){
        state := State.sResult
      }
    }
    is(State.sCharge){
      when(io.videoInput === "h1_000_000_000".U){
        state := State.sIdle
      }
    }
    is(State.sRead){
      when(io.videoInput === "h1_000_000_000".U){
        state := State.sDiff
      }
    }
    is(State.sDiff){
      when(diffThreshold === true.B ) {
        state := State.sAction
      }.otherwise{
        state := State.sIdle
      }
    }
    is(State.sAction){
      when(actionWriteMem === true.B){
        state := State.sIdle
      }
    }
    is(State.sResult){
      when(resultCountMem === true.B){
        state := State.sIdle
      }
    }
  }

  //////////////////////////////////////////////////////////////////////// action Code
  //////////////////////////////////////////////////////////////////////// action Code

  val baseCount: Counter = Counter(intellectualCCTVParams.oneFrame)
  val baseCountMem: Counter = Counter(intellectualCCTVParams.oneFrame)

  ////////////////////////////////////////////////////////  base Reg
  val inVideoPixel: UInt = Wire(io.videoInput(intellectualCCTVParams.pixel-1 , 0))
  val basePixel: Vec[UInt] = RegInit(VecInit(Seq.fill(intellectualCCTVParams.oneFrame*intellectualCCTVParams.hardfixFrameCoefficient)(0.U(intellectualCCTVParams.pixel.W))))
  val regEnable: Vec[Bool] = VecInit( Seq.fill(intellectualCCTVParams.oneFrame)(false.B))

  regEnable.zipWithIndex.foreach({ case( value, index) =>
    when(value === true.B){
      basePixel(index) := inVideoPixel
      for(i <- 0 until  intellectualCCTVParams.hardfixFrameCoefficient){
        basePixel(i+1+index) := basePixel(i+index)
      }
    }
  })

  val sumReg: Seq[UInt] = VecInit(Seq.fill(intellectualCCTVParams.oneFrameColor)( Wire(0.U((intellectualCCTVParams.pixel).W))))
  for( i <- 0 until intellectualCCTVParams.oneFrame){
    val nowReg: Seq[UInt] = (0 until intellectualCCTVParams.hardfixFrameCoefficient).map({ x =>
      basePixel(i+x)
    })
    for (ii <- 0 until intellectualCCTVParams.colorDomain){
      val tmp: Int = i*intellectualCCTVParams.colorDomain + ii
      sumReg(i*intellectualCCTVParams.colorDomain + ii) := nowReg.map({ value =>
        value((ii+1)*intellectualCCTVParams,ii*intellectualCCTVParams.colorScale)
      }).reduce(_+&_)/intellectualCCTVParams.hardfixFrameCoefficient.U
    }
  }

  ////////////////////////////////////////////////////////  Charge Reg
  when(basePixel.last =/= 0.U){
    chargeIn := true.B
  }

  ////////////////////////////////////////////////////////  Diff Reg
  val diffReg: Vec[UInt] = RegInit(VecInit(Seq.fill(intellectualCCTVParams.oneFrame)(0.U(1.W))))
  val diffValue: Vec[UInt] = VecInit(Seq.fill(intellectualCCTVParams.colorDomain)( 0.U((intellectualCCTVParams.colorScale+1).W )))
//  val diffValue = WireInit(0.U((intellectualCCTVParams.pixel+intellectualCCTVParams.colorDomain).W))
  val absDiffValue: Vec[UInt] = VecInit(Seq.fill(intellectualCCTVParams.colorDomain)( 0.U((intellectualCCTVParams.colorScale+1).W)))

  val now_av: UInt = MuxLookup(baseCount.value, 0.U, (0 until intellectualCCTVParams.oneFrame).map({ x =>
    (x.U, sumReg(x))
  }))

  ////////////////////////////////////////////////////////  Mem
  val memAll: SyncReadMem[UInt] = SyncReadMem(intellectualCCTVParams.oneFrame, UInt(intellectualCCTVParams.oneFrame.W))

  (0 until intellectualCCTVParams.colorDomain).foreach({ x =>
    val indexHead = (x+1)*intellectualCCTVParams.colorScale
    val indexLast = x*intellectualCCTVParams.colorScale
    diffValue(x) := Cat(0.U(1.W), inVideoPixel(indexHead, indexLast)) - Cat(0.U(1.W), now_av(indexHead, indexLast))
    when(diffValue.head === 1.U){
      absDiffValue(x) := ~diffValue(x)
    }
  })

  switch(state){
    is(State.sIdle){
      baseCount.reset()
      baseCountMem.reset()
    }
    is(State.sCharge){
      baseCount.inc()
      regEnable.zipWithIndex.foreach({ case( value, index) =>
        when(index.U(intellectualCCTVParams.oneFrame.W) === baseCount.value){
          value := true.B
        }.otherwise{
          value := false.B
        }
      })
    }
    is(State.sRead){
      baseCount.inc()
      regEnable.zipWithIndex.foreach({ case( value, index) =>
        when(index.U(intellectualCCTVParams.oneFrame.W) === baseCount.value){
          value := true.B
        }.otherwise{
          value := false.B
        }
      })
      (0 until intellectualCCTVParams.oneFrame).foreach({ x =>
        diffReg(x) := 0.U
        when(x.U === baseCount.value){
          when(absDiffValue.reduce(_+&_) >= intellectualCCTVParams.hardfixFrameSensitivityThreshold.U ){
            diffReg(x) := 1.U
          }
        }
      })
    }
    is(State.sDiff){
      diffThreshold := false.B
      when( diffReg.reduce(_+&_) > intellectualCCTVParams.hardfixL1NormSensitivityThreshold.U ){
        diffThreshold := true.B
        stateGet := StateGet.sGet
      }
    }
    is(State.sAction){
      baseCountMem.inc()
      actionWriteMem := false.B
      when( baseCountMem.value === intellectualCCTVParams.oneFrame.U){
        actionWriteMem := true.B
      }

      val now_Reg = MuxLookup(baseCountMem.value, 0.U, basePixel.zipWithIndex.map({ case( value, index) =>
        (index.U, value)
      }))

      memAll.write(baseCountMem.value, now_Reg)
    }
    is(State.sResult){

    }
  }

}

object yohan_IntellectualCCTV extends App{
  println("Starting generate")
  (new ChiselStage).emitVerilog(new IntellectualCCTV( IntellectualCCTVParams()) )
}