module GripperInDelayNCycles(
  input         clock,
  input         reset,
  input  [16:0] io_in,
  output [16:0] io_out
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
`endif // RANDOMIZE_REG_INIT
  reg  delaCon_0; // @[VolumeIntegrator2.scala 35:37]
  reg  delaCon_1; // @[VolumeIntegrator2.scala 35:37]
  reg  delaCon_2; // @[VolumeIntegrator2.scala 35:37]
  reg  delaCon_3; // @[VolumeIntegrator2.scala 35:37]
  wire [1:0] _tempAdd_T = delaCon_0 + delaCon_1; // @[VolumeIntegrator2.scala 41:42]
  wire [1:0] _GEN_0 = {{1'd0}, delaCon_2}; // @[VolumeIntegrator2.scala 41:42]
  wire [2:0] _tempAdd_T_1 = _tempAdd_T + _GEN_0; // @[VolumeIntegrator2.scala 41:42]
  wire [2:0] _GEN_1 = {{2'd0}, delaCon_3}; // @[VolumeIntegrator2.scala 41:42]
  wire [3:0] tempAdd = _tempAdd_T_1 + _GEN_1; // @[VolumeIntegrator2.scala 41:42]
  wire [16:0] _GEN_2 = reset ? 17'h0 : io_in; // @[VolumeIntegrator2.scala 35:{37,37} 36:16]
  assign io_out = {{13'd0}, tempAdd}; // @[VolumeIntegrator2.scala 42:12]
  always @(posedge clock) begin
    delaCon_0 <= _GEN_2[0]; // @[VolumeIntegrator2.scala 35:{37,37} 36:16]
    if (reset) begin // @[VolumeIntegrator2.scala 35:37]
      delaCon_1 <= 1'h0; // @[VolumeIntegrator2.scala 35:37]
    end else begin
      delaCon_1 <= delaCon_0; // @[VolumeIntegrator2.scala 38:9]
    end
    if (reset) begin // @[VolumeIntegrator2.scala 35:37]
      delaCon_2 <= 1'h0; // @[VolumeIntegrator2.scala 35:37]
    end else begin
      delaCon_2 <= delaCon_1; // @[VolumeIntegrator2.scala 38:9]
    end
    if (reset) begin // @[VolumeIntegrator2.scala 35:37]
      delaCon_3 <= 1'h0; // @[VolumeIntegrator2.scala 35:37]
    end else begin
      delaCon_3 <= delaCon_2; // @[VolumeIntegrator2.scala 38:9]
    end
  end
// Register and memory initialization
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  delaCon_0 = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  delaCon_1 = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  delaCon_2 = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  delaCon_3 = _RAND_3[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
endmodule
module VolumeIntegrator(
  input        clock,
  input        reset,
  input  [3:0] io_in_0,
  input  [3:0] io_in_1,
  input  [3:0] io_in_2,
  input  [3:0] io_in_3,
  input  [3:0] io_in_4,
  input  [3:0] io_in_5,
  input  [3:0] io_in_6,
  input  [3:0] io_in_7,
  input  [3:0] io_in_8,
  input  [3:0] io_in_9,
  input  [3:0] io_in_10,
  input  [3:0] io_in_11,
  input  [3:0] io_in_12,
  input  [3:0] io_in_13,
  input  [3:0] io_in_14,
  input  [3:0] io_in_15,
  input  [3:0] io_in_16,
  input  [3:0] io_in_17,
  input  [3:0] io_in_18,
  input  [3:0] io_in_19,
  input  [3:0] io_in_20,
  input  [3:0] io_in_21,
  input  [3:0] io_in_22,
  input  [3:0] io_in_23,
  input  [3:0] io_in_24,
  input  [3:0] io_in_25,
  input  [3:0] io_in_26,
  input  [3:0] io_in_27,
  input  [3:0] io_in_28,
  input  [3:0] io_in_29,
  output [3:0] io_out
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
`endif // RANDOMIZE_REG_INIT
  wire  bufferReg_clock; // @[VolumeIntegrator2.scala 87:54]
  wire  bufferReg_reset; // @[VolumeIntegrator2.scala 87:54]
  wire [16:0] bufferReg_io_in; // @[VolumeIntegrator2.scala 87:54]
  wire [16:0] bufferReg_io_out; // @[VolumeIntegrator2.scala 87:54]
  reg [16:0] allSReg_0; // @[VolumeIntegrator2.scala 56:35]
  reg [16:0] allSReg_1; // @[VolumeIntegrator2.scala 56:35]
  reg [16:0] allSReg_2; // @[VolumeIntegrator2.scala 56:35]
  reg [16:0] allSReg_3; // @[VolumeIntegrator2.scala 56:35]
  wire [17:0] _allSReg_3_T = allSReg_0 + allSReg_1; // @[VolumeIntegrator2.scala 81:77]
  wire [17:0] _GEN_0 = {{1'd0}, allSReg_2}; // @[VolumeIntegrator2.scala 81:77]
  wire [18:0] _allSReg_3_T_1 = _allSReg_3_T + _GEN_0; // @[VolumeIntegrator2.scala 81:77]
  wire [4:0] _allSReg_0_T = io_in_0 + io_in_1; // @[VolumeIntegrator2.scala 85:112]
  wire [4:0] _GEN_1 = {{1'd0}, io_in_2}; // @[VolumeIntegrator2.scala 85:112]
  wire [5:0] _allSReg_0_T_1 = _allSReg_0_T + _GEN_1; // @[VolumeIntegrator2.scala 85:112]
  wire [5:0] _GEN_2 = {{2'd0}, io_in_3}; // @[VolumeIntegrator2.scala 85:112]
  wire [6:0] _allSReg_0_T_2 = _allSReg_0_T_1 + _GEN_2; // @[VolumeIntegrator2.scala 85:112]
  wire [6:0] _GEN_3 = {{3'd0}, io_in_4}; // @[VolumeIntegrator2.scala 85:112]
  wire [7:0] _allSReg_0_T_3 = _allSReg_0_T_2 + _GEN_3; // @[VolumeIntegrator2.scala 85:112]
  wire [7:0] _GEN_4 = {{4'd0}, io_in_5}; // @[VolumeIntegrator2.scala 85:112]
  wire [8:0] _allSReg_0_T_4 = _allSReg_0_T_3 + _GEN_4; // @[VolumeIntegrator2.scala 85:112]
  wire [8:0] _GEN_5 = {{5'd0}, io_in_6}; // @[VolumeIntegrator2.scala 85:112]
  wire [9:0] _allSReg_0_T_5 = _allSReg_0_T_4 + _GEN_5; // @[VolumeIntegrator2.scala 85:112]
  wire [9:0] _GEN_6 = {{6'd0}, io_in_7}; // @[VolumeIntegrator2.scala 85:112]
  wire [10:0] _allSReg_0_T_6 = _allSReg_0_T_5 + _GEN_6; // @[VolumeIntegrator2.scala 85:112]
  wire [10:0] _GEN_7 = {{7'd0}, io_in_8}; // @[VolumeIntegrator2.scala 85:112]
  wire [11:0] _allSReg_0_T_7 = _allSReg_0_T_6 + _GEN_7; // @[VolumeIntegrator2.scala 85:112]
  wire [11:0] _GEN_8 = {{8'd0}, io_in_9}; // @[VolumeIntegrator2.scala 85:112]
  wire [12:0] _allSReg_0_T_8 = _allSReg_0_T_7 + _GEN_8; // @[VolumeIntegrator2.scala 85:112]
  wire [4:0] _allSReg_1_T = io_in_10 + io_in_11; // @[VolumeIntegrator2.scala 85:112]
  wire [4:0] _GEN_9 = {{1'd0}, io_in_12}; // @[VolumeIntegrator2.scala 85:112]
  wire [5:0] _allSReg_1_T_1 = _allSReg_1_T + _GEN_9; // @[VolumeIntegrator2.scala 85:112]
  wire [5:0] _GEN_10 = {{2'd0}, io_in_13}; // @[VolumeIntegrator2.scala 85:112]
  wire [6:0] _allSReg_1_T_2 = _allSReg_1_T_1 + _GEN_10; // @[VolumeIntegrator2.scala 85:112]
  wire [6:0] _GEN_11 = {{3'd0}, io_in_14}; // @[VolumeIntegrator2.scala 85:112]
  wire [7:0] _allSReg_1_T_3 = _allSReg_1_T_2 + _GEN_11; // @[VolumeIntegrator2.scala 85:112]
  wire [7:0] _GEN_12 = {{4'd0}, io_in_15}; // @[VolumeIntegrator2.scala 85:112]
  wire [8:0] _allSReg_1_T_4 = _allSReg_1_T_3 + _GEN_12; // @[VolumeIntegrator2.scala 85:112]
  wire [8:0] _GEN_13 = {{5'd0}, io_in_16}; // @[VolumeIntegrator2.scala 85:112]
  wire [9:0] _allSReg_1_T_5 = _allSReg_1_T_4 + _GEN_13; // @[VolumeIntegrator2.scala 85:112]
  wire [9:0] _GEN_14 = {{6'd0}, io_in_17}; // @[VolumeIntegrator2.scala 85:112]
  wire [10:0] _allSReg_1_T_6 = _allSReg_1_T_5 + _GEN_14; // @[VolumeIntegrator2.scala 85:112]
  wire [10:0] _GEN_15 = {{7'd0}, io_in_18}; // @[VolumeIntegrator2.scala 85:112]
  wire [11:0] _allSReg_1_T_7 = _allSReg_1_T_6 + _GEN_15; // @[VolumeIntegrator2.scala 85:112]
  wire [11:0] _GEN_16 = {{8'd0}, io_in_19}; // @[VolumeIntegrator2.scala 85:112]
  wire [12:0] _allSReg_1_T_8 = _allSReg_1_T_7 + _GEN_16; // @[VolumeIntegrator2.scala 85:112]
  wire [4:0] _allSReg_2_T = io_in_20 + io_in_21; // @[VolumeIntegrator2.scala 85:112]
  wire [4:0] _GEN_17 = {{1'd0}, io_in_22}; // @[VolumeIntegrator2.scala 85:112]
  wire [5:0] _allSReg_2_T_1 = _allSReg_2_T + _GEN_17; // @[VolumeIntegrator2.scala 85:112]
  wire [5:0] _GEN_18 = {{2'd0}, io_in_23}; // @[VolumeIntegrator2.scala 85:112]
  wire [6:0] _allSReg_2_T_2 = _allSReg_2_T_1 + _GEN_18; // @[VolumeIntegrator2.scala 85:112]
  wire [6:0] _GEN_19 = {{3'd0}, io_in_24}; // @[VolumeIntegrator2.scala 85:112]
  wire [7:0] _allSReg_2_T_3 = _allSReg_2_T_2 + _GEN_19; // @[VolumeIntegrator2.scala 85:112]
  wire [7:0] _GEN_20 = {{4'd0}, io_in_25}; // @[VolumeIntegrator2.scala 85:112]
  wire [8:0] _allSReg_2_T_4 = _allSReg_2_T_3 + _GEN_20; // @[VolumeIntegrator2.scala 85:112]
  wire [8:0] _GEN_21 = {{5'd0}, io_in_26}; // @[VolumeIntegrator2.scala 85:112]
  wire [9:0] _allSReg_2_T_5 = _allSReg_2_T_4 + _GEN_21; // @[VolumeIntegrator2.scala 85:112]
  wire [9:0] _GEN_22 = {{6'd0}, io_in_27}; // @[VolumeIntegrator2.scala 85:112]
  wire [10:0] _allSReg_2_T_6 = _allSReg_2_T_5 + _GEN_22; // @[VolumeIntegrator2.scala 85:112]
  wire [10:0] _GEN_23 = {{7'd0}, io_in_28}; // @[VolumeIntegrator2.scala 85:112]
  wire [11:0] _allSReg_2_T_7 = _allSReg_2_T_6 + _GEN_23; // @[VolumeIntegrator2.scala 85:112]
  wire [11:0] _GEN_24 = {{8'd0}, io_in_29}; // @[VolumeIntegrator2.scala 85:112]
  wire [12:0] _allSReg_2_T_8 = _allSReg_2_T_7 + _GEN_24; // @[VolumeIntegrator2.scala 85:112]
  wire [16:0] _io_out_T = bufferReg_io_out / 7'h78; // @[VolumeIntegrator2.scala 90:30]
  wire [18:0] _GEN_25 = reset ? 19'h0 : _allSReg_3_T_1; // @[VolumeIntegrator2.scala 56:{35,35} 80:49]
  GripperInDelayNCycles bufferReg ( // @[VolumeIntegrator2.scala 87:54]
    .clock(bufferReg_clock),
    .reset(bufferReg_reset),
    .io_in(bufferReg_io_in),
    .io_out(bufferReg_io_out)
  );
  assign io_out = _io_out_T[3:0]; // @[VolumeIntegrator2.scala 90:10]
  assign bufferReg_clock = clock;
  assign bufferReg_reset = reset;
  assign bufferReg_io_in = allSReg_3; // @[VolumeIntegrator2.scala 89:19]
  always @(posedge clock) begin
    if (reset) begin // @[VolumeIntegrator2.scala 56:35]
      allSReg_0 <= 17'h0; // @[VolumeIntegrator2.scala 56:35]
    end else begin
      allSReg_0 <= {{4'd0}, _allSReg_0_T_8}; // @[VolumeIntegrator2.scala 85:51]
    end
    if (reset) begin // @[VolumeIntegrator2.scala 56:35]
      allSReg_1 <= 17'h0; // @[VolumeIntegrator2.scala 56:35]
    end else begin
      allSReg_1 <= {{4'd0}, _allSReg_1_T_8}; // @[VolumeIntegrator2.scala 85:51]
    end
    if (reset) begin // @[VolumeIntegrator2.scala 56:35]
      allSReg_2 <= 17'h0; // @[VolumeIntegrator2.scala 56:35]
    end else begin
      allSReg_2 <= {{4'd0}, _allSReg_2_T_8}; // @[VolumeIntegrator2.scala 85:51]
    end
    allSReg_3 <= _GEN_25[16:0]; // @[VolumeIntegrator2.scala 56:{35,35} 80:49]
  end
// Register and memory initialization
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  allSReg_0 = _RAND_0[16:0];
  _RAND_1 = {1{`RANDOM}};
  allSReg_1 = _RAND_1[16:0];
  _RAND_2 = {1{`RANDOM}};
  allSReg_2 = _RAND_2[16:0];
  _RAND_3 = {1{`RANDOM}};
  allSReg_3 = _RAND_3[16:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
endmodule
