circuit VolumeIntegrator :
  module GripperInDelayNCycles :
    input clock : Clock
    input reset : Reset
    output io : { flip in : UInt<17>, out : UInt<17>}

    wire _delaCon_WIRE : UInt<1>[4] @[VolumeIntegrator2.scala 35:45]
    _delaCon_WIRE[0] <= UInt<1>("h0") @[VolumeIntegrator2.scala 35:45]
    _delaCon_WIRE[1] <= UInt<1>("h0") @[VolumeIntegrator2.scala 35:45]
    _delaCon_WIRE[2] <= UInt<1>("h0") @[VolumeIntegrator2.scala 35:45]
    _delaCon_WIRE[3] <= UInt<1>("h0") @[VolumeIntegrator2.scala 35:45]
    reg delaCon : UInt<1>[4], clock with :
      reset => (reset, _delaCon_WIRE) @[VolumeIntegrator2.scala 35:37]
    delaCon[0] <= io.in @[VolumeIntegrator2.scala 36:16]
    delaCon[1] <= delaCon[0] @[VolumeIntegrator2.scala 38:9]
    delaCon[2] <= delaCon[1] @[VolumeIntegrator2.scala 38:9]
    delaCon[3] <= delaCon[2] @[VolumeIntegrator2.scala 38:9]
    node _tempAdd_T = add(delaCon[0], delaCon[1]) @[VolumeIntegrator2.scala 41:42]
    node _tempAdd_T_1 = add(_tempAdd_T, delaCon[2]) @[VolumeIntegrator2.scala 41:42]
    node tempAdd = add(_tempAdd_T_1, delaCon[3]) @[VolumeIntegrator2.scala 41:42]
    io.out <= tempAdd @[VolumeIntegrator2.scala 42:12]

  module VolumeIntegrator :
    input clock : Clock
    input reset : UInt<1>
    output io : { flip in : UInt<4>[30], out : UInt<4>}

    wire _allSReg_WIRE : UInt<17>[4] @[VolumeIntegrator2.scala 56:43]
    _allSReg_WIRE[0] <= UInt<17>("h0") @[VolumeIntegrator2.scala 56:43]
    _allSReg_WIRE[1] <= UInt<17>("h0") @[VolumeIntegrator2.scala 56:43]
    _allSReg_WIRE[2] <= UInt<17>("h0") @[VolumeIntegrator2.scala 56:43]
    _allSReg_WIRE[3] <= UInt<17>("h0") @[VolumeIntegrator2.scala 56:43]
    reg allSReg : UInt<17>[4], clock with :
      reset => (reset, _allSReg_WIRE) @[VolumeIntegrator2.scala 56:35]
    node _allSReg_3_T = add(allSReg[0], allSReg[1]) @[VolumeIntegrator2.scala 81:77]
    node _allSReg_3_T_1 = add(_allSReg_3_T, allSReg[2]) @[VolumeIntegrator2.scala 81:77]
    allSReg[3] <= _allSReg_3_T_1 @[VolumeIntegrator2.scala 80:49]
    node _allSReg_0_T = add(io.in[0], io.in[1]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_0_T_1 = add(_allSReg_0_T, io.in[2]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_0_T_2 = add(_allSReg_0_T_1, io.in[3]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_0_T_3 = add(_allSReg_0_T_2, io.in[4]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_0_T_4 = add(_allSReg_0_T_3, io.in[5]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_0_T_5 = add(_allSReg_0_T_4, io.in[6]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_0_T_6 = add(_allSReg_0_T_5, io.in[7]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_0_T_7 = add(_allSReg_0_T_6, io.in[8]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_0_T_8 = add(_allSReg_0_T_7, io.in[9]) @[VolumeIntegrator2.scala 85:112]
    allSReg[0] <= _allSReg_0_T_8 @[VolumeIntegrator2.scala 85:51]
    node _allSReg_1_T = add(io.in[10], io.in[11]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_1_T_1 = add(_allSReg_1_T, io.in[12]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_1_T_2 = add(_allSReg_1_T_1, io.in[13]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_1_T_3 = add(_allSReg_1_T_2, io.in[14]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_1_T_4 = add(_allSReg_1_T_3, io.in[15]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_1_T_5 = add(_allSReg_1_T_4, io.in[16]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_1_T_6 = add(_allSReg_1_T_5, io.in[17]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_1_T_7 = add(_allSReg_1_T_6, io.in[18]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_1_T_8 = add(_allSReg_1_T_7, io.in[19]) @[VolumeIntegrator2.scala 85:112]
    allSReg[1] <= _allSReg_1_T_8 @[VolumeIntegrator2.scala 85:51]
    node _allSReg_2_T = add(io.in[20], io.in[21]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_2_T_1 = add(_allSReg_2_T, io.in[22]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_2_T_2 = add(_allSReg_2_T_1, io.in[23]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_2_T_3 = add(_allSReg_2_T_2, io.in[24]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_2_T_4 = add(_allSReg_2_T_3, io.in[25]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_2_T_5 = add(_allSReg_2_T_4, io.in[26]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_2_T_6 = add(_allSReg_2_T_5, io.in[27]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_2_T_7 = add(_allSReg_2_T_6, io.in[28]) @[VolumeIntegrator2.scala 85:112]
    node _allSReg_2_T_8 = add(_allSReg_2_T_7, io.in[29]) @[VolumeIntegrator2.scala 85:112]
    allSReg[2] <= _allSReg_2_T_8 @[VolumeIntegrator2.scala 85:51]
    inst bufferReg of GripperInDelayNCycles @[VolumeIntegrator2.scala 87:54]
    bufferReg.clock <= clock
    bufferReg.reset <= reset
    bufferReg.io.in <= allSReg[3] @[VolumeIntegrator2.scala 89:19]
    node _io_out_T = div(bufferReg.io.out, UInt<7>("h78")) @[VolumeIntegrator2.scala 90:30]
    io.out <= _io_out_T @[VolumeIntegrator2.scala 90:10]

