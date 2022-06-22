scalaVersion := "2.13.7"
addCompilerPlugin("edu.berkeley.cs" % "chisel3-plugin" % "3.5.0" cross CrossVersion.full) // library that is needed in compile time
libraryDependencies += "edu.berkeley.cs" %% "chisel3" % "3.5.0" // library that is needed in compile and execute time // main library for chisel project
libraryDependencies += "edu.berkeley.cs" %% "chiseltest" % "0.5.0" % "test" // library for chisel test. Test your module in chisel level
lazy val root = (project in file("."))