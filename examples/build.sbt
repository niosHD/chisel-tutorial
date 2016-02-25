organization := "edu.berkeley.cs"

version := "2.3-SNAPSHOT"

name := "chisel-tutorial"

scalaVersion := "2.11.7"

// Provide a managed dependency on chisel.
// The default version is "latest.release".
// This may be overridden if -DchiselVersion="" is supplied on the command line.

val chiselVersion = System.getProperty("chiselVersion", "latest.release")

libraryDependencies ++= ( if (chiselVersion != "None" ) ("edu.berkeley.cs" %% "chisel" % chiselVersion) :: Nil; else Nil)

libraryDependencies ++= (
  if (chiselVersion != "None" && chiselVersion.charAt(0) > '2')
    ("edu.berkeley.cs" %% "chisel-hwiotesters" % chiselVersion) :: Nil;
  else Nil
  )

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-language:reflectiveCalls")

