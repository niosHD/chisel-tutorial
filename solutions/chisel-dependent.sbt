// Provide a managed dependency on chisel.
// The default version is "latest.release".
// This may be overridden if -DchiselVersion="" is supplied on the command line.

val chiselVersion_h = System.getProperty("chiselVersion", "latest.release")

libraryDependencies ++= ( if (chiselVersion_h != "None" ) {
  List("chisel", "chisel_library") map ("edu.berkeley.cs" %% _ % chiselVersion_h)
 } else Nil
)
