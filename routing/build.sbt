ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

ThisBuild / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x =>
    val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
    oldStrategy(x)
}

lazy val routing = (project in file("."))
//  .enablePlugins(AssemblyPlugin)
  .settings(
    name := "project-routing",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.0.13",
      "dev.zio" %% "zio-http" % "0.0.5",
      "com.github.pureconfig" %% "pureconfig" % "0.17.3"
    ),
    assembly / assemblyJarName := "routing.jar",
    assembly / assemblyOutputPath := baseDirectory.value / "target" / (assembly / assemblyJarName).value
  )
